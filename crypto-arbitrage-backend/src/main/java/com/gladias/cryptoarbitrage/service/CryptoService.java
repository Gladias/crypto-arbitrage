package com.gladias.cryptoarbitrage.service;

import com.gladias.cryptoarbitrage.dto.ArbitrageOption;
import com.gladias.cryptoarbitrage.dto.CurrentMarketAnalysis;
import com.gladias.cryptoarbitrage.dto.Fee;
import com.gladias.cryptoarbitrage.dto.FeeLevel;
import com.gladias.cryptoarbitrage.dto.MarketCurrentData;
import com.gladias.cryptoarbitrage.provider.BinanceDataProvider;
import com.gladias.cryptoarbitrage.provider.KrakenDataProvider;
import com.gladias.cryptoarbitrage.provider.KucoinDataProvider;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CryptoService {
    private final BinanceDataProvider binanceDataProvider;
    private final KrakenDataProvider krakenDataProvider;
    private final KucoinDataProvider kucoinDataProvider;

    public CurrentMarketAnalysis getCurrentMarketsData(String coin, FeeLevel feeLevel) {
        MarketCurrentData binance = binanceDataProvider.getCurrentMarketData(coin, feeLevel);
        MarketCurrentData kraken = krakenDataProvider.getCurrentMarketData(coin, feeLevel);
        MarketCurrentData kucoin = kucoinDataProvider.getCurrentMarketData(coin, feeLevel);

        List<MarketCurrentData> markets = List.of(binance, kraken, kucoin);
        List<ArbitrageOption> bestArbitrageOptions = ArbitrageAnalysisService.findBestArbitrageOptions(markets, coin);

        return new CurrentMarketAnalysis(markets, bestArbitrageOptions);
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
}
