package com.gladias.cryptoarbitrage.provider;

import com.gladias.cryptoarbitrage.dto.MarketPrice;

public interface CryptoDataProvider {
    MarketPrice getCurrentPrices();
}
