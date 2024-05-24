package com.gladias.cryptoarbitrage.entity;

import com.gladias.cryptoarbitrage.dto.ArbitrageOption;
import com.gladias.cryptoarbitrage.dto.MarketCurrentData;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "market_data")
public class MarketDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataFetchedAt;

    private String coin;
    private String currency;
    private String selectedFeeLevel;

    private String mostValuableTradeAtTime;
    private String secondValuableTradeAtTime;
    private String thirdValuableTradeAtTime;

    // ############################
    //          BINANCE
    // ############################
    private String binanceBidPrice;
    private String binanceAskPrice;
    private String binanceBidQuantity;
    private String binanceAskQuantity;
    private String binanceAskAndBidPriceSpread;

    private String binanceLast24HrsVolume;
    private String binanceConstantFeeForMaker;
    private String binanceConstantFeeForTaker;
    private String binanceMakerFeeForCurrentAskPrice;
    private String binanceMakerFeeForCurrentBidPrice;
    private String binanceTakerFeeForCurrentAskPrice;
    private String binanceTakerFeeForCurrentBidPrice;

    // ############################
    //          KRAKEN
    // ############################
    private String krakenBidPrice;
    private String krakenAskPrice;
    private String krakenBidQuantity;
    private String krakenAskQuantity;
    private String krakenAskAndBidPriceSpread;

    private String krakenLast24HrsVolume;
    private String krakenConstantFeeForMaker;
    private String krakenConstantFeeForTaker;
    private String krakenMakerFeeForCurrentAskPrice;
    private String krakenMakerFeeForCurrentBidPrice;
    private String krakenTakerFeeForCurrentAskPrice;
    private String krakenTakerFeeForCurrentBidPrice;

    // ############################
    //          KUCOIN
    // ############################
    private String kucoinBidPrice;
    private String kucoinAskPrice;
    private String kucoinBidQuantity;
    private String kucoinAskQuantity;
    private String kucoinAskAndBidPriceSpread;

    private String kucoinLast24HrsVolume;
    private String kucoinConstantFeeForMaker;
    private String kucoinConstantFeeForTaker;
    private String kucoinMakerFeeForCurrentAskPrice;
    private String kucoinMakerFeeForCurrentBidPrice;
    private String kucoinTakerFeeForCurrentAskPrice;
    private String kucoinTakerFeeForCurrentBidPrice;

    public static MarketDataEntity of(
            MarketCurrentData binanceMarketData,
            MarketCurrentData krakenMarketData,
            MarketCurrentData kucoinMarketData,
            List<ArbitrageOption> bestArbitrageOptions,
            LocalDateTime dataFetchedAt,
            String coin,
            String feeLevel) {
        return MarketDataEntity.builder()
                .dataFetchedAt(dataFetchedAt)
                .coin(coin)
                .currency("USDT")
                .selectedFeeLevel(feeLevel)
                .mostValuableTradeAtTime(bestArbitrageOptions.getFirst().description())
                .secondValuableTradeAtTime(bestArbitrageOptions.get(1).description())
                .thirdValuableTradeAtTime(bestArbitrageOptions.getLast().description())
                // BINANCE
                .binanceBidPrice(binanceMarketData.price().bidPrice())
                .binanceAskPrice(binanceMarketData.price().askPrice())
                .binanceBidQuantity(binanceMarketData.price().bidQuantity())
                .binanceAskQuantity(binanceMarketData.price().askQuantity())
                .binanceAskAndBidPriceSpread(binanceMarketData.price().askAndBidPriceSpread())
                .binanceLast24HrsVolume(binanceMarketData.volume().last24HrsVolume())
                .binanceConstantFeeForMaker(binanceMarketData.fee().constantFeeForMaker())
                .binanceConstantFeeForTaker(binanceMarketData.fee().constantFeeForTaker())
                .binanceMakerFeeForCurrentAskPrice(binanceMarketData.fee().makerFeeForCurrentAskPrice())
                .binanceMakerFeeForCurrentBidPrice(binanceMarketData.fee().makerFeeForCurrentBidPrice())
                .binanceTakerFeeForCurrentAskPrice(binanceMarketData.fee().takerFeeForCurrentAskPrice())
                .binanceTakerFeeForCurrentBidPrice(binanceMarketData.fee().takerFeeForCurrentBidPrice())
                // KRAKEN
                .krakenBidPrice(krakenMarketData.price().bidPrice())
                .krakenAskPrice(krakenMarketData.price().askPrice())
                .krakenBidQuantity(krakenMarketData.price().bidQuantity())
                .krakenAskQuantity(krakenMarketData.price().askQuantity())
                .krakenAskAndBidPriceSpread(krakenMarketData.price().askAndBidPriceSpread())
                .krakenLast24HrsVolume(krakenMarketData.volume().last24HrsVolume())
                .krakenConstantFeeForMaker(krakenMarketData.fee().constantFeeForMaker())
                .krakenConstantFeeForTaker(krakenMarketData.fee().constantFeeForTaker())
                .krakenMakerFeeForCurrentAskPrice(krakenMarketData.fee().makerFeeForCurrentAskPrice())
                .krakenMakerFeeForCurrentBidPrice(krakenMarketData.fee().makerFeeForCurrentBidPrice())
                .krakenTakerFeeForCurrentAskPrice(krakenMarketData.fee().takerFeeForCurrentAskPrice())
                .krakenTakerFeeForCurrentBidPrice(krakenMarketData.fee().takerFeeForCurrentBidPrice())
                // KUCOIN
                .kucoinBidPrice(kucoinMarketData.price().bidPrice())
                .kucoinAskPrice(kucoinMarketData.price().askPrice())
                .kucoinBidQuantity(kucoinMarketData.price().bidQuantity())
                .kucoinAskQuantity(kucoinMarketData.price().askQuantity())
                .kucoinAskAndBidPriceSpread(kucoinMarketData.price().askAndBidPriceSpread())
                .kucoinLast24HrsVolume(kucoinMarketData.volume().last24HrsVolume())
                .kucoinConstantFeeForMaker(kucoinMarketData.fee().constantFeeForMaker())
                .kucoinConstantFeeForTaker(kucoinMarketData.fee().constantFeeForTaker())
                .kucoinMakerFeeForCurrentAskPrice(kucoinMarketData.fee().makerFeeForCurrentAskPrice())
                .kucoinMakerFeeForCurrentBidPrice(kucoinMarketData.fee().makerFeeForCurrentBidPrice())
                .kucoinTakerFeeForCurrentAskPrice(kucoinMarketData.fee().takerFeeForCurrentAskPrice())
                .kucoinTakerFeeForCurrentBidPrice(kucoinMarketData.fee().takerFeeForCurrentBidPrice())
                .build();
    }
}
