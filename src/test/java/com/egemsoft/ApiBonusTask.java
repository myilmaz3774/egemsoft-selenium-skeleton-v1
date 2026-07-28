package com.egemsoft;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/** Bonus görev: API'den gelen ilk 10 ürünün adını konsola yazdırır. */
public class ApiBonusTask {

    private static final String PRODUCTS_API_URL = "https://automationexercise.com/api/productsList";

    @Test
    public void testGetFirstTenProducts() throws Exception {
        HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(15)).build();
        HttpRequest request = HttpRequest.newBuilder(URI.create(PRODUCTS_API_URL))
                .GET().timeout(Duration.ofSeconds(30)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode(), "API HTTP 200 döndürmedi.");
        JsonObject responseBody = JsonParser.parseString(response.body()).getAsJsonObject();
        Assertions.assertEquals(200, responseBody.get("responseCode").getAsInt(),
                "API yanıtındaki responseCode beklenen değerde değil.");

        JsonArray products = responseBody.getAsJsonArray("products");
        Assertions.assertNotNull(products, "Ürün listesi API yanıtında bulunamadı.");
        Assertions.assertTrue(products.size() >= 10, "İlk 10 ürün için yeterli veri yok.");

        System.out.println("İlk 10 ürün:");
        for (int i = 0; i < 10; i++) {
            JsonObject product = products.get(i).getAsJsonObject();
            System.out.printf("%d. %s%n", i + 1, product.get("name").getAsString());
        }
    }
}
