package ru.my.corp.weather.service.api;

import ru.my.corp.weather.model.WeatherReport;

/**
 * Сервис получения и анализа метеорологических данных.
 */
public interface WeatherService {

    /**
     * Получает данные о погоде, рассчитывает среднюю температуру и формирует отчет.
     *
     * @param lat   широта точки
     * @param lon   долгота точки
     * @param limit количество суток для расчета среднего прогноза
     * @return готовый отчет {@link WeatherReport}
     */
    WeatherReport getWeatherReport(double lat, double lon, int limit);
}
