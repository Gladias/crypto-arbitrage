package com.gladias.cryptoarbitrage.provider;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gladias.cryptoarbitrage.dto.Market;
import com.gladias.cryptoarbitrage.dto.MarketPrice;
import com.gladias.cryptoarbitrage.dto.Price;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BinanceDataProvider implements CryptoDataProvider {
    private static final String BINANCE_BASE_URL = "https://api.binance.com";
    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final Market MARKET = Market.BINANCE;

    private final String binanceApiKey;
    private final String binanceApiSecret;

    private final HttpClient httpClient;
    private final Base64.Encoder base64Encoder;
    private final ObjectMapper objectMapper;

    public BinanceDataProvider(
            @Value("${binance.api.key}") String binanceApiKey,
            @Value("${binance.api.secret}") String binanceApiSecret) {
        this.binanceApiKey = binanceApiKey;
        this.binanceApiSecret = binanceApiSecret;

        this.httpClient = HttpClient.newHttpClient();
        this.base64Encoder = Base64.getEncoder();
        this.objectMapper = new ObjectMapper();
    }

    @SneakyThrows
    public MarketPrice getCurrentPrices() {
        String endpoint = "/api/v3/ticker/price";
        String encodedSymbols = URLEncoder.encode("[\"BTCUSDT\",\"ETHUSDT\"]", StandardCharsets.UTF_8);
        String params = "?symbols=" + encodedSymbols;
        List<String> authHeaders = getAuthHeaders();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BINANCE_BASE_URL + endpoint + params))
                .headers(authHeaders.toArray(String[]::new))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        List<Map<String, String>> responseData = objectMapper.readValue(response.body(), new TypeReference<>() {});

        // symbol is like "BTCUSDT" so split it to "BTC" and "USDT"
        List<Price> prices = responseData.stream()
                .map(record -> new Price(
                        record.get("symbol").substring(0, 3),
                        record.get("symbol").substring(3),
                        record.get("price")))
                .toList();

        return new MarketPrice(MARKET, prices);
    }

    private List<String> getAuthHeaders() {
        return List.of("X-MBX-APIKEY", binanceApiKey);
    }
}
