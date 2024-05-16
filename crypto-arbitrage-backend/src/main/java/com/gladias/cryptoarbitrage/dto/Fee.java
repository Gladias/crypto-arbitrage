package com.gladias.cryptoarbitrage.dto;

public record Fee(
        String constantFeeForMaker,
        String constantFeeForTaker,
        String makerFeeForCurrentAskPrice,
        String makerFeeForCurrentBidPrice,
        String takerFeeForCurrentAskPrice,
        String takerFeeForCurrentBidPrice) {

    public static Fee fromDoubles(
            Double constantFeeForMaker,
            Double constantFeeForTaker,
            Double makerFeeForCurrentAskPrice,
            Double makerFeeForCurrentBidPrice,
            Double takerFeeForCurrentAskPrice,
            Double takerFeeForCurrentBidPrice) {
        return new Fee(
                String.valueOf(constantFeeForMaker),
                String.valueOf(constantFeeForTaker),
                String.valueOf(makerFeeForCurrentAskPrice),
                String.valueOf(makerFeeForCurrentBidPrice),
                String.valueOf(takerFeeForCurrentAskPrice),
                String.valueOf(takerFeeForCurrentBidPrice));
    }
}
