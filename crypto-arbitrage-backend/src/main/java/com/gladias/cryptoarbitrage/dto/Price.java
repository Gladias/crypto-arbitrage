package com.gladias.cryptoarbitrage.dto;

public record Price(
        String coin,
        String currency,
        String bidPrice,
        String askPrice,
        String bidQuantity,
        String askQuantity,
        String askAndBidPriceSpread) {}
