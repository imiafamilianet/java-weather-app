package ru.my.corp.weather;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.my.corp.weather.model.WeatherReport;
import ru.my.corp.weather.service.api.WeatherClient;
import ru.my.corp.weather.service.api.WeatherParser;
import ru.my.corp.weather.service.api.WeatherService;
import ru.my.corp.weather.service.impl.JacksonWeatherParser;
import ru.my.corp.weather.service.impl.WeatherServiceImpl;
import ru.my.corp.weather.service.impl.YandexWeatherClient;

import java.net.http.HttpClient;
import java.util.Locale;

/**
 * Точка входа в приложение получения метеорологических данных.
 */
public class Main {

    public static void main(String[] args) {
        String apiKey = System.getenv("YANDEX_WEATHER_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            apiKey = "КЛЮЧ_API_ЯНДЕКС_ПОГОДЫ";
        }

        WeatherClient weatherClient = new YandexWeatherClient(HttpClient.newHttpClient(), apiKey);
        WeatherParser weatherParser = new JacksonWeatherParser(new ObjectMapper());
        WeatherService weatherService = new WeatherServiceImpl(weatherClient, weatherParser);

        double lat = 55.75; // Широта (Москва)
        double lon = 37.62; // Долгота (Москва)
        int limit = 5;      // Количество суток прогноза

        try {
            WeatherReport report = weatherService.getWeatherReport(lat, lon, limit);

            System.out.println("=== Полный ответ сервиса (JSON) ===");
            System.out.println(report.rawJson());

            System.out.println("\n=== Текущая погода ===");
            System.out.println("Текущая температура (fact.temp): " + report.currentTemp() + " °C");

            System.out.println("\n=== Прогноз погоды ===");
            System.out.printf(Locale.US, "Средняя дневная температура за %d сут.: %.2f °C%n",
                    report.daysCount(), report.averageForecastTemp());

        } catch (Exception e) {
            System.err.println("Ошибка выполнения программы: " + e.getMessage());
        }
    }
}
