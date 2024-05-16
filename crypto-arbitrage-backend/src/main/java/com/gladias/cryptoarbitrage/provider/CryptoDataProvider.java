package com.gladias.cryptoarbitrage.provider;

import com.gladias.cryptoarbitrage.dto.FeeLevel;
import com.gladias.cryptoarbitrage.dto.MarketCurrentData;

public interface CryptoDataProvider {
    MarketCurrentData getCurrentMarketData(String coin, FeeLevel feeLevel);
}
