package com.gladias.cryptoarbitrage.provider;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.List;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class KucoinDataProvider {
    private static final String KUCOIN_BASE_URL = "https://api.kucoin.com";
    private static final String HMAC_ALGORITHM = "HmacSHA256";

    private final String kucoinApiKey;
    private final String kucoinApiSecret;
    private final String kucoinApiPassphrase;

    private final Mac mac;
    private final HttpClient httpClient;
    private final Base64.Encoder base64Encoder;

    @SneakyThrows({NoSuchAlgorithmException.class, InvalidKeyException.class})
    public KucoinDataProvider(
            @Value("${kucoin.api.key}") String kucoinApiKey,
            @Value("${kucoin.api.secret}") String kucoinApiSecret,
            @Value("${kucoin.api.passphrase}") String kucoinApiPassphrase) {
        this.kucoinApiKey = kucoinApiKey;
        this.kucoinApiSecret = kucoinApiSecret;
        this.kucoinApiPassphrase = kucoinApiPassphrase;

        this.mac = Mac.getInstance(HMAC_ALGORITHM);
        mac.init(new SecretKeySpec(this.kucoinApiSecret.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM));

        this.httpClient = HttpClient.newHttpClient();
        this.base64Encoder = Base64.getEncoder();
    }

    @SneakyThrows
    public String getPrices() {
        String endpoint = "/api/v1/prices";
        List<String> authHeaders = getAuthHeaders("GET", endpoint);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(KUCOIN_BASE_URL + endpoint))
                .headers(authHeaders.toArray(String[]::new))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    private List<String> getAuthHeaders(String method, String endpoint) {
        return getAuthHeaders(method, endpoint, "");
    }

    private List<String> getAuthHeaders(String method, String endpoint, String jsonBody) {
        long now = ZonedDateTime.now(ZoneId.of("UTC")).toInstant().toEpochMilli();

        String strToSign = now + method + endpoint + jsonBody;

        String encodedSignature = base64Encoder.encodeToString(mac.doFinal(strToSign.getBytes(StandardCharsets.UTF_8)));
        String encodedPassphrase =
                base64Encoder.encodeToString(mac.doFinal(kucoinApiPassphrase.getBytes(StandardCharsets.UTF_8)));

        return List.of(
                "KC-API-SIGN",
                encodedSignature,
                "KC-API-TIMESTAMP",
                String.valueOf(now),
                "KC-API-KEY",
                kucoinApiKey,
                "KC-API-PASSPHRASE",
                encodedPassphrase,
                "KC-API-KEY-VERSION",
                String.valueOf(3));
    }
}
