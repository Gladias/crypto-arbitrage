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
public class BinanceDataProvider implements CryptoDataProvider {
    private static final String BINANCE_BASE_URL = "https://api.binance.com";
    private static final Market MARKET = Market.BINANCE;

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public BinanceDataProvider() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public MarketCurrentData getCurrentMarketData(String coin, FeeLevel feeLevel) {
        String symbolForCoin = coin.toUpperCase() + "USDT";

        Price currentPrice = getCurrentPrice(symbolForCoin);
        Volume volume = getVolume(symbolForCoin);
        Fee fee = CryptoService.getFeeForCurrentPriceAndLevel(
                getFeesForGivenLevel(feeLevel), currentPrice.askPrice(), currentPrice.bidPrice());

        log.info("Fetched data from Binance");
        return new MarketCurrentData(MARKET, currentPrice, volume, fee);
    }

    @SneakyThrows
    public Price getCurrentPrice(String symbolForCoin) {
        String endpoint = "/api/v3/ticker/bookTicker";

        String responseBody = sendGETRequestToEndpoint(endpoint, symbolForCoin);
        Map<String, String> responseData = objectMapper.readValue(responseBody, new TypeReference<>() {});

        // symbol is like "BTCUSDT" so split it to "BTC" and "USDT"
        Price price = new Price(
                responseData.get("symbol").substring(0, 3),
                responseData.get("symbol").substring(3),
                responseData.get("bidPrice"),
                responseData.get("askPrice"),
                responseData.get("bidQty"),
                responseData.get("askQty"),
                CryptoService.getPriceDifference(responseData.get("bidPrice"), responseData.get("askPrice")));

        return price;
    }

    @SneakyThrows
    public Volume getVolume(String symbolForCoin) {
        String endpoint = "/api/v3/ticker/24hr";

        String responseBody = sendGETRequestToEndpoint(endpoint, symbolForCoin);
        Map<String, String> responseData = objectMapper.readValue(responseBody, new TypeReference<>() {});

        String volume = responseData.get("volume");

        return new Volume(volume);
    }

    public Pair<Double, Double> getFeesForGivenLevel(FeeLevel level) {
        Pair<Double, Double> fee;

        switch (level) {
            case REGULAR_USER -> fee = Pair.of(0.00100, 0.00100);
            case VIP_1 -> fee = Pair.of(0.00090, 0.00100);
            case VIP_2 -> fee = Pair.of(0.00080, 0.00100);
            case VIP_3 -> fee = Pair.of(0.00042, 0.00060);
            case VIP_4 -> fee = Pair.of(0.00042, 0.00054);
            case VIP_5 -> fee = Pair.of(0.00036, 0.00048);
            case VIP_6 -> fee = Pair.of(0.00030, 0.00042);
            case VIP_7 -> fee = Pair.of(0.00024, 0.00036);
            case VIP_8 -> fee = Pair.of(0.00018, 0.00030);
            case VIP_9 -> fee = Pair.of(0.00012, 0.00024);
            default -> fee = Pair.of(0D, 0D);
        }

        return fee;
    }

    @SneakyThrows
    private String sendGETRequestToEndpoint(String endpoint, String symbol) {
        String params = "?symbol=" + symbol;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BINANCE_BASE_URL + endpoint + params))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
