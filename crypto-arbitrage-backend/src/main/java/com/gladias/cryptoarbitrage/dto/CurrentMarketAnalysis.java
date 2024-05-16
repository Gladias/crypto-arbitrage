package com.gladias.cryptoarbitrage.dto;

import java.util.List;

public record CurrentMarketAnalysis(List<MarketCurrentData> markets, List<ArbitrageOption> arbitrageOptions) {}
