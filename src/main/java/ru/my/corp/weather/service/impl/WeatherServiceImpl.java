package ru.my.corp.weather.service.impl;

import lombok.RequiredArgsConstructor;
import ru.my.corp.weather.model.WeatherReport;
import ru.my.corp.weather.service.api.WeatherClient;
import ru.my.corp.weather.service.api.WeatherParser;
import ru.my.corp.weather.service.api.WeatherService;

/**
 * Реализация основного сервиса получения погоды.
 * Зависит исключительно от абстракций {@link WeatherClient} и {@link WeatherParser}.
 */
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    private final WeatherClient weatherClient;
    private final WeatherParser weatherParser;

    /**
     * Получает данные о погоде, рассчитывает среднюю температуру и формирует отчет.
     *
     * @param lat   широта точки
     * @param lon   долгота точки
     * @param limit количество суток для расчета среднего прогноза
     * @return готовый отчет {@link WeatherReport}
     */
    @Override
    public WeatherReport getWeatherReport(double lat, double lon, int limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException("Количество суток (limit) должно быть больше нуля.");
        }
        String rawJson = weatherClient.fetchForecastJson(lat, lon, limit);
        return weatherParser.parseReport(rawJson, limit);
    }
}
