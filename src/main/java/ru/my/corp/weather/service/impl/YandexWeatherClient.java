package ru.my.corp.weather.service.impl;

import ru.my.corp.weather.service.api.WeatherClient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;

/**
 * HTTP-клиент для взаимодействия с REST API Яндекс Погоды v2.
 */
public class YandexWeatherClient implements WeatherClient {

    private static final String API_URL = "https://yandex.ru";
    private static final String HEADER_API_KEY = "X-Yandex-Weather-Key";

    private final HttpClient httpClient;
    private final String apiKey;

    /**
     * Создание клиента с передачей ключа доступа и экземпляра HttpClient.
     *
     * @param httpClient настроенный HTTP-клиент
     * @param apiKey     ключ доступа к API Яндекс Погоды
     */
    public YandexWeatherClient(HttpClient httpClient, String apiKey) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("API-ключ не может быть пустым.");
        }
        this.httpClient = httpClient;
        this.apiKey = apiKey;
    }

    @Override
    public String fetchForecastJson(double lat, double lon, int limit) {
        String requestUrl = String.format(Locale.US, "%s?lat=%f&lon=%f&limit=%d", API_URL, lat, lon, limit);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestUrl))
                .header(HEADER_API_KEY, apiKey)
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RuntimeException("Ошибка API Яндекс Погоды. Код статуса: " + response.statusCode()
                        + ", тело ответа: " + response.body());
            }
            return response.body();
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Сетевая ошибка при обращении к API погоды: " + e.getMessage(), e);
        }
    }
}
