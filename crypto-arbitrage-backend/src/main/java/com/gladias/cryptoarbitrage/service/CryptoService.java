package com.gladias.cryptoarbitrage.service;

import com.gladias.cryptoarbitrage.provider.KucoinDataProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CryptoService {
    private final KucoinDataProvider kucoinDataProvider;

    public String getPrices() {
        return kucoinDataProvider.getPrices();
    }
}
