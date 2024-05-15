package com.gladias.cryptoarbitrage.controller;

import com.gladias.cryptoarbitrage.dto.Prices;
import com.gladias.cryptoarbitrage.service.CryptoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CryptoController {
    private final CryptoService cryptoService;

    @GetMapping("/api/prices")
    public Prices getPrices() {
        return cryptoService.getPrices();
    }
}
