package com.gladias.cryptoarbitrage.provider;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gladias.cryptoarbitrage.dto.Market;
import com.gladias.cryptoarbitrage.dto.MarketPrice;
import com.gladias.cryptoarbitrage.dto.Price;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
public class KucoinDataProvider implements CryptoDataProvider {
    private static final String KUCOIN_BASE_URL = "https://api.kucoin.com";
    private static final Market MARKET = Market.KUCOIN;

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public KucoinDataProvider() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public MarketPrice getCurrentPrices() {
        String btcUsdtEndpoint = "/api/v1/market/orderbook/level1?symbol=BTC-USDT";
        String ethUsdtEndpoint = "/api/v1/market/orderbook/level1?symbol=ETH-USDT";

        String btcUsdtCurrentPrice = getCoinCurrentPrice(btcUsdtEndpoint);
        String ethUsdtCurrentPrice = getCoinCurrentPrice(ethUsdtEndpoint);

        List<Price> prices =
                List.of(new Price("BTC", "USDT", btcUsdtCurrentPrice), new Price("ETH", "USDT", ethUsdtCurrentPrice));

        return new MarketPrice(MARKET, prices);
    }

    @SneakyThrows
    private String getCoinCurrentPrice(String endpoint) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(KUCOIN_BASE_URL + endpoint))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Map<String, Object> responseData = objectMapper.readValue(response.body(), new TypeReference<>() {});

        Map<String, Object> data = (Map<String, Object>) responseData.get("data");
        String price = (String) data.get("price");

        return price;
    }
}
