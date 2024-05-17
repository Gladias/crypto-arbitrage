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
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

@Slf4j
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

    @SneakyThrows
    public MarketCurrentData getCurrentMarketData(String coin, FeeLevel feeLevel) {
        String symbolForCoin = coin.replace("BTC", "XBT") + "USDT";

        String endpoint = "/0/public/Ticker";
        String params = "?pair=" + symbolForCoin;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(KRAKEN_BASE_URL + endpoint + params))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Map<String, Object> responseData = objectMapper.readValue(response.body(), new TypeReference<>() {});

        Map<String, Object> result = (Map<String, Object>) responseData.get("result");
        Map<String, Object> symbol = (Map<String, Object>) result.get(symbolForCoin);

        String askPrice = ((List<String>) symbol.get("a")).get(0);
        String bidPrice = ((List<String>) symbol.get("b")).get(0);

        String askQty = ((List<String>) symbol.get("a")).get(2);
        String bidQty = ((List<String>) symbol.get("b")).get(2);

        String volumeForLast24Hrs = ((List<String>) symbol.get("v")).get(1);

        Price price = new Price(
                coin, "USDT", bidPrice, askPrice, bidQty, askQty, CryptoService.getPriceDifference(askPrice, bidPrice));
        Fee fee = CryptoService.getFeeForCurrentPriceAndLevel(getFeesForGivenLevel(feeLevel), askPrice, bidPrice);
        Volume volume = new Volume(volumeForLast24Hrs);

        log.info("Fetched data from Kraken");
        return new MarketCurrentData(MARKET, price, volume, fee);
    }

    public Pair<Double, Double> getFeesForGivenLevel(FeeLevel level) {
        Pair<Double, Double> fee;

        switch (level) {
            case REGULAR_USER -> fee = Pair.of(0.0025, 0.0040);
            case VIP_1 -> fee = Pair.of(0.0020, 0.0035);
            case VIP_2 -> fee = Pair.of(0.0014, 0.0024);
            case VIP_3 -> fee = Pair.of(0.0012, 0.0022);
            case VIP_4 -> fee = Pair.of(0.0010, 0.0020);
            case VIP_5 -> fee = Pair.of(0.0008, 0.0018);
            case VIP_6 -> fee = Pair.of(0.0006, 0.0016);
            case VIP_7 -> fee = Pair.of(0.0004, 0.0014);
            case VIP_8 -> fee = Pair.of(0.0002, 0.0012);
            case VIP_9 -> fee = Pair.of(0.0000, 0.0010);
            default -> fee = Pair.of(0D, 0D);
        }

        return fee;
    }
}
