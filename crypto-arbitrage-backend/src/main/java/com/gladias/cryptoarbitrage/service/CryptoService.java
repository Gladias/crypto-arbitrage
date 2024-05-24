package com.gladias.cryptoarbitrage.service;

import com.gladias.cryptoarbitrage.dto.ArbitrageOption;
import com.gladias.cryptoarbitrage.dto.CurrentMarketAnalysis;
import com.gladias.cryptoarbitrage.dto.Fee;
import com.gladias.cryptoarbitrage.dto.FeeLevel;
import com.gladias.cryptoarbitrage.dto.MarketCurrentData;
import com.gladias.cryptoarbitrage.dto.Price;
import com.gladias.cryptoarbitrage.dto.Volume;
import com.gladias.cryptoarbitrage.entity.MarketDataEntity;
import com.gladias.cryptoarbitrage.provider.BinanceDataProvider;
import com.gladias.cryptoarbitrage.provider.KrakenDataProvider;
import com.gladias.cryptoarbitrage.provider.KucoinDataProvider;
import com.gladias.cryptoarbitrage.repository.MarketDataRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CryptoService {
    private final BinanceDataProvider binanceDataProvider;
    private final KrakenDataProvider krakenDataProvider;
    private final KucoinDataProvider kucoinDataProvider;
    private final MarketDataRepository marketDataRepository;

    @SneakyThrows
    public CurrentMarketAnalysis getCurrentMarketsData(String coin, FeeLevel feeLevel) {
        CompletableFuture<MarketCurrentData> binanceFuture =
                CompletableFuture.supplyAsync(() -> binanceDataProvider.getCurrentMarketData(coin, feeLevel));

        CompletableFuture<MarketCurrentData> krakenFuture =
                CompletableFuture.supplyAsync(() -> krakenDataProvider.getCurrentMarketData(coin, feeLevel));

        CompletableFuture<MarketCurrentData> kucoinFuture =
                CompletableFuture.supplyAsync(() -> kucoinDataProvider.getCurrentMarketData(coin, feeLevel));

        MarketCurrentData binanceCurrentData = binanceFuture.get();
        MarketCurrentData krakenCurrentData = krakenFuture.get();
        MarketCurrentData kucoinCurrentData = kucoinFuture.get();
        LocalDateTime dataFetchedAt = LocalDateTime.now();

        List<MarketCurrentData> markets = List.of(binanceCurrentData, krakenCurrentData, kucoinCurrentData);
        List<ArbitrageOption> bestArbitrageOptions = ArbitrageAnalysisService.findBestArbitrageOptions(markets, coin);

        MarketDataEntity marketDataEntity = MarketDataEntity.of(
                binanceCurrentData,
                krakenCurrentData,
                kucoinCurrentData,
                bestArbitrageOptions,
                dataFetchedAt,
                coin,
                feeLevel.name());
        marketDataRepository.save(marketDataEntity);

        List<MarketCurrentData> marketsWithRoundedPrices = getMarketsWithRoundedPrices(markets);

        return new CurrentMarketAnalysis(marketsWithRoundedPrices, bestArbitrageOptions);
    }

    public static String getPriceDifference(String firstPrice, String secondPrice) {
        Double firstPriceAsDouble = Double.valueOf(firstPrice);
        Double secondPriceAsDouble = Double.valueOf(secondPrice);

        return String.valueOf(Math.abs(firstPriceAsDouble - secondPriceAsDouble));
    }

    public static Fee getFeeForCurrentPriceAndLevel(
            Pair<Double, Double> feesForGivenLevel, String askPrice, String bidPrice) {
        Double makerFee = feesForGivenLevel.getLeft();
        Double takerFee = feesForGivenLevel.getRight();

        Double makerFeeForCurrentAskPrice = Double.parseDouble(askPrice) * makerFee;
        Double makerFeeForCurrentBidPrice = Double.parseDouble(bidPrice) * makerFee;

        Double takerFeeForCurrentAskPrice = Double.parseDouble(askPrice) * takerFee;
        Double takerFeeForCurrentBidPrice = Double.parseDouble(bidPrice) * takerFee;

        return Fee.fromDoubles(
                makerFee,
                takerFee,
                makerFeeForCurrentAskPrice,
                makerFeeForCurrentBidPrice,
                takerFeeForCurrentAskPrice,
                takerFeeForCurrentBidPrice);
    }

    private static List<MarketCurrentData> getMarketsWithRoundedPrices(List<MarketCurrentData> markets) {
        List<MarketCurrentData> marketsWithRoundedPrices = new ArrayList<>(3);

        for (MarketCurrentData market : markets) {
            Volume roundedVolume =
                    new Volume(convertTo5DecimalPlaces(market.volume().last24HrsVolume()));

            String constantFeeForTaker =
                    "%.4f".formatted(Double.parseDouble(market.fee().constantFeeForTaker()) * 100.0) + "%";
            String constantFeeForMaker =
                    "%.4f".formatted(Double.parseDouble(market.fee().constantFeeForMaker()) * 100.0) + "%";

            Fee roundedFee = new Fee(
                    constantFeeForMaker,
                    constantFeeForTaker,
                    convertTo5DecimalPlaces(market.fee().makerFeeForCurrentAskPrice()),
                    convertTo5DecimalPlaces(market.fee().makerFeeForCurrentBidPrice()),
                    convertTo5DecimalPlaces(market.fee().takerFeeForCurrentAskPrice()),
                    convertTo5DecimalPlaces(market.fee().takerFeeForCurrentBidPrice()));

            Price roundedPrice = new Price(
                    market.price().coin(),
                    market.price().currency(),
                    convertTo5DecimalPlaces(market.price().bidPrice()),
                    convertTo5DecimalPlaces(market.price().askPrice()),
                    convertTo5DecimalPlaces(market.price().bidQuantity()),
                    convertTo5DecimalPlaces(market.price().askQuantity()),
                    convertTo5DecimalPlaces(market.price().askAndBidPriceSpread()));

            marketsWithRoundedPrices.add(
                    new MarketCurrentData(market.market(), roundedPrice, roundedVolume, roundedFee));
        }

        return marketsWithRoundedPrices;
    }

    private static String convertTo5DecimalPlaces(String price) {
        return "%.5f".formatted(Double.parseDouble(price));
    }
}
