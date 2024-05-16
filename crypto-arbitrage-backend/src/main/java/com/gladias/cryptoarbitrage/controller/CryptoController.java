package com.gladias.cryptoarbitrage.controller;

import com.gladias.cryptoarbitrage.dto.CurrentMarketAnalysis;
import com.gladias.cryptoarbitrage.dto.FeeLevel;
import com.gladias.cryptoarbitrage.service.CryptoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CryptoController {
    private final CryptoService cryptoService;

    @GetMapping("/api/markets")
    public CurrentMarketAnalysis getCurrentMarketsData(@RequestParam String coin, @RequestParam FeeLevel feeLevel) {
        return cryptoService.getCurrentMarketsData(coin, feeLevel);
    }
}
