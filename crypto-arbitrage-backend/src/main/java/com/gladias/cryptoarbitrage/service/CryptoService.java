package com.gladias.cryptoarbitrage.service;

import com.gladias.cryptoarbitrage.dto.MarketPrice;
import com.gladias.cryptoarbitrage.dto.Prices;
import com.gladias.cryptoarbitrage.provider.BinanceDataProvider;
import com.gladias.cryptoarbitrage.provider.KrakenDataProvider;
import com.gladias.cryptoarbitrage.provider.KucoinDataProvider;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CryptoService {
    private final BinanceDataProvider binanceDataProvider;
    private final KucoinDataProvider kucoinDataProvider;
    private final KrakenDataProvider krakenDataProvider;

    public Prices getPrices() {
        MarketPrice binance = binanceDataProvider.getCurrentPrices();
        MarketPrice kraken = krakenDataProvider.getCurrentPrices();
        MarketPrice kucoin = kucoinDataProvider.getCurrentPrices();

        return new Prices(List.of(binance, kraken, kucoin));
    }
}
