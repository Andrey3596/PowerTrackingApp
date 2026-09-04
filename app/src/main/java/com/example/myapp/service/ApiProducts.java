package com.example.myapp.service;
import com.example.myapp.model.Product;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiProducts {


    private static final String SEARCH_URL =
            "https://world.openfoodfacts.org/cgi/search.pl?search_terms=%s&json=1&page_size=1&fields=product_name_ru,product_name,nutriments";

    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    public Product search(String query) throws Exception {
        // Формируем URL
        String url = String.format(SEARCH_URL, query.replace(" ", "%20"));

        // Создаём запрос
        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", "MyApp/1.0")
                .get()
                .build();

        // Выполняем запрос и получаем ответ
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Ошибка API: " + response.code());
            }

            String json = response.body().string();

            // Парсим JSON (та же логика, что и раньше)
            JsonObject root = gson.fromJson(json, JsonObject.class);
            JsonArray products = root.getAsJsonArray("products");
            if (products.size() == 0) throw new RuntimeException("Не найдено");

            JsonObject product = products.get(0).getAsJsonObject();
            String name = product.has("product_name_ru")
                    ? product.get("product_name_ru").getAsString()
                    : product.get("product_name").getAsString();

            JsonObject nutriments = product.getAsJsonObject("nutriments");
            double calories = getDouble(nutriments, "energy-kcal_100g");
            double proteins = getDouble(nutriments, "proteins_100g");
            double fat = getDouble(nutriments, "fat_100g");
            double carbs = getDouble(nutriments, "carbohydrates_100g");

            return new Product(name, calories, proteins, fat, carbs);
        }
    }

    private double getDouble(JsonObject obj, String key) {
        return obj.has(key) ? obj.get(key).getAsDouble() : 0;
    }
}
