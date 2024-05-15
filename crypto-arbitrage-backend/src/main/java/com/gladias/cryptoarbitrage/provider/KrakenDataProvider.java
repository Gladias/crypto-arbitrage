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
public class KrakenDataProvider implements CryptoDataProvider {
    private static final String KRAKEN_BASE_URL = "https://api.kraken.com";
    private static final Market MARKET = Market.KRAKEN;

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public KrakenDataProvider() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public MarketPrice getCurrentPrices() {
        String btcUsdtEndpoint = "/0/public/Ticker?pair=XBTUSDT";
        String ethUsdtEndpoint = "/0/public/Ticker?pair=ETHUSDT";

        String btcUsdtCurrentPrice = getCoinCurrentPrice(btcUsdtEndpoint, "XBTUSDT");
        String ethUsdtCurrentPrice = getCoinCurrentPrice(ethUsdtEndpoint, "ETHUSDT");

        List<Price> prices =
                List.of(new Price("BTC", "USDT", btcUsdtCurrentPrice), new Price("ETH", "USDT", ethUsdtCurrentPrice));

        return new MarketPrice(MARKET, prices);
    }

    @SneakyThrows
    private String getCoinCurrentPrice(String endpoint, String pair) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(KRAKEN_BASE_URL + endpoint))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Map<String, Object> responseData = objectMapper.readValue(response.body(), new TypeReference<>() {});

        Map<String, Object> result = (Map<String, Object>) responseData.get("result");
        Map<String, Object> symbol = (Map<String, Object>) result.get(pair);
        List<String> askedPrice = (List<String>) symbol.get("a");
        String price = askedPrice.get(0);

        return price;
    }
}
