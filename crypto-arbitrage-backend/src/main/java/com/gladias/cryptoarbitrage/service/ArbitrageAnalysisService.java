package com.gladias.cryptoarbitrage.service;

import com.gladias.cryptoarbitrage.dto.ArbitrageOption;
import com.gladias.cryptoarbitrage.dto.MarketCurrentData;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class ArbitrageAnalysisService {
    public static List<ArbitrageOption> findBestArbitrageOptions(List<MarketCurrentData> markets, String coin) {
        TreeMap<Double, ArbitrageOption> opportunitiesByProfit = new TreeMap<>();

        for (MarketCurrentData buyCryptoAtMarket : markets) {
            for (MarketCurrentData sellCryptoAtMarket : markets) {
                if (!buyCryptoAtMarket.market().equals(sellCryptoAtMarket.market())) {
                    double maxAmount = Math.min(
                            Double.parseDouble(buyCryptoAtMarket.price().askQuantity()),
                            Double.parseDouble(sellCryptoAtMarket.price().bidQuantity()));

                    if (maxAmount > 0) {
                        double profit = arbitrageOptionProfit(buyCryptoAtMarket, sellCryptoAtMarket, maxAmount);

                        String description = String.format(
                                "Buy %.5f %s on %s and Sell %.5f %s on %s with profit %.5f USDT",
                                maxAmount,
                                coin,
                                buyCryptoAtMarket.market(),
                                maxAmount,
                                coin,
                                sellCryptoAtMarket.market(),
                                profit);

                        String color = profit > 0 ? "#80B918" : "#EE6055";
                        opportunitiesByProfit.put(profit, new ArbitrageOption(description, profit, color));
                    }
                }
            }
        }

        List<ArbitrageOption> top3ArbitrageOptions = new ArrayList<>();

        opportunitiesByProfit.descendingKeySet().stream()
                .limit(3)
                .forEach(key -> top3ArbitrageOptions.add(opportunitiesByProfit.get(key)));

        return top3ArbitrageOptions;
    }

    public static double arbitrageOptionProfit(
            MarketCurrentData buyCryptoAtMarket, MarketCurrentData sellCryptoAtMarket, double amount) {
        double buyCost = amount * Double.parseDouble(buyCryptoAtMarket.price().askPrice());
        double sellRevenue =
                amount * Double.parseDouble(sellCryptoAtMarket.price().bidPrice());

        double buyFee = buyCost * Double.parseDouble(buyCryptoAtMarket.fee().constantFeeForTaker());
        double sellFee =
                sellRevenue * Double.parseDouble(sellCryptoAtMarket.fee().constantFeeForTaker());

        double netProfit = sellRevenue - buyCost - buyFee - sellFee;
        return netProfit;
    }
}
