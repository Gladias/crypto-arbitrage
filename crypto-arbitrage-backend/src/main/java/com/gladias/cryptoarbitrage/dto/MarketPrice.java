package com.gladias.cryptoarbitrage.dto;

import java.util.List;

public record MarketPrice(Market market, List<Price> prices) {}
