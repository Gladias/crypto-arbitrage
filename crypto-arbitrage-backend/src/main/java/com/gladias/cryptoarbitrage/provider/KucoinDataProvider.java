package com.gladias.cryptoarbitrage.provider;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gladias.cryptoarbitrage.dto.Fee;
import com.gladias.cryptoarbitrage.dto.FeeLevel;
import com.gladias.cryptoarbitrage.dto.Market;
import com.gladias.cryptoarbitrage.dto.MarketCurrentData;
import com.gladias.cryptoarbitrage.dto.Price;
import com.gladias.cryptoarbitrage.dto.Volume;
import com.gladias.cryptoarbitrage.service.CryptoService;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

@Slf4j
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

    public MarketCurrentData getCurrentMarketData(String coin, FeeLevel feeLevel) {
        String symbolForCoin = coin.toUpperCase() + "-USDT";

        Price currentPrice = getCurrentPrice(coin, symbolForCoin);
        Volume volume = getVolume(symbolForCoin);
        Fee fee = CryptoService.getFeeForCurrentPriceAndLevel(
                getFeesForGivenLevel(feeLevel), currentPrice.askPrice(), currentPrice.bidPrice());

        return new MarketCurrentData(MARKET, currentPrice, volume, fee);
    }

    @SneakyThrows
    public Price getCurrentPrice(String coin, String symbolForCoin) {
        String endpoint = "/api/v1/market/orderbook/level1";

        String responseBody = sendGETRequestToEndpoint(endpoint, symbolForCoin);
        Map<String, Object> responseData = objectMapper.readValue(responseBody, new TypeReference<>() {});
        Map<String, Object> innerResponseData = (Map<String, Object>) responseData.get("data");

        String askPrice = (String) innerResponseData.get("bestAsk");
        String bidPrice = (String) innerResponseData.get("bestBid");

        String askQty = (String) innerResponseData.get("bestAskSize");
        String bidQty = (String) innerResponseData.get("bestBidSize");

        log.info("Fetched data from KuCoin");
        return new Price(
                coin, "USDT", bidPrice, askPrice, bidQty, askQty, CryptoService.getPriceDifference(askPrice, bidPrice));
    }

    @SneakyThrows
    public Volume getVolume(String symbol) {
        String endpoint = "/api/v1/market/stats";

        String responseBody = sendGETRequestToEndpoint(endpoint, symbol);
        Map<String, Object> responseData = objectMapper.readValue(responseBody, new TypeReference<>() {});
        Map<String, Object> innerResponseData = (Map<String, Object>) responseData.get("data");

        String volume = (String) innerResponseData.get("vol");

        return new Volume(volume);
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

    public Pair<Double, Double> getFeesForGivenLevel(FeeLevel level) {
        Pair<Double, Double> fee;

        switch (level) {
            case REGULAR_USER -> fee = Pair.of(0.00100, 0.00100);
            case VIP_1 -> fee = Pair.of(0.00090, 0.00100);
            case VIP_2 -> fee = Pair.of(0.00075, 0.00090);
            case VIP_3 -> fee = Pair.of(0.00065, 0.00085);
            case VIP_4 -> fee = Pair.of(0.00045, 0.00065);
            case VIP_5 -> fee = Pair.of(0.00035, 0.00055);
            case VIP_6 -> fee = Pair.of(0.00025, 0.00045);
            case VIP_7 -> fee = Pair.of(0.00015, 0.00042);
            case VIP_8 -> fee = Pair.of(0.00010, 0.00040);
            case VIP_9 -> fee = Pair.of(0.00000, 0.00040);
            default -> fee = Pair.of(0D, 0D);
        }

        return fee;
    }

    @SneakyThrows
    private String sendGETRequestToEndpoint(String endpoint, String symbol) {
        String params = "?symbol=" + symbol;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(KUCOIN_BASE_URL + endpoint + params))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
