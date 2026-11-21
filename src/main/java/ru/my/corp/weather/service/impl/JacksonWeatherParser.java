package ru.my.corp.weather.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import ru.my.corp.weather.model.WeatherReport;
import ru.my.corp.weather.service.api.WeatherParser;

/**
 * Реализация парсера метеорологических данных на базе библиотеки Jackson.
 */
@RequiredArgsConstructor
public class JacksonWeatherParser implements WeatherParser {

    private final ObjectMapper objectMapper;

    /**
     * Извлекает необходимые показатели из JSON-ответа и формирует отчет.
     *
     * @param rawJson необработанный JSON-ответ от API
     * @param limit   запрошенное количество суток прогноза
     * @return сформированный объект {@link WeatherReport}
     */
    @Override
    public WeatherReport parseReport(String rawJson, int limit) {
        try {
            var rootNode = objectMapper.readTree(rawJson);

            var factTempNode = rootNode.path("fact").path("temp");
            if (!factTempNode.isNumber()) {
                throw new RuntimeException("В ответе API отсутствует числовое поле fact.temp");
            }
            double currentTemp = factTempNode.asDouble();

            var forecastsNode = rootNode.path("forecasts");
            if (!forecastsNode.isArray() || forecastsNode.isEmpty()) {
                throw new RuntimeException("В ответе API отсутствует массив прогнозов forecasts");
            }

            int daysToCount = Math.min(limit, forecastsNode.size());
            double sumTempAvg = 0.0;
            int validDaysCount = 0;

            for (int i = 0; i < daysToCount; i++) {
                JsonNode tempAvgNode = forecastsNode.get(i)
                        .path("parts")
                        .path("day")
                        .path("temp_avg");

                if (tempAvgNode.isNumber()) {
                    sumTempAvg += tempAvgNode.asDouble();
                    validDaysCount++;
                }
            }

            if (validDaysCount == 0) {
                throw new RuntimeException("Не удалось извлечь значения parts.day.temp_avg из прогноза");
            }

            double averageTemp = sumTempAvg / validDaysCount;
            return new WeatherReport(rawJson, currentTemp, averageTemp, validDaysCount);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка парсинга JSON-ответа: " + e.getMessage(), e);
        }
    }
}
