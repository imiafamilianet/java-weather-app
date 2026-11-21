package ru.my.corp.weather.service.api;

import ru.my.corp.weather.model.WeatherReport;

/**
 * Парсер метеорологических данных.
 */
public interface WeatherParser {

    /**
     * Извлекает необходимые показатели из JSON-ответа и формирует отчет.
     *
     * @param rawJson необработанный JSON-ответ от API
     * @param limit   запрошенное количество суток прогноза
     * @return сформированный объект {@link WeatherReport}
     */
    WeatherReport parseReport(String rawJson, int limit);
}
