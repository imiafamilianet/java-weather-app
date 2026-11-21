package ru.my.corp.weather.service.api;

/**
 * Клиент для получения сырых метеорологических данных из внешнего источника.
 */
public interface WeatherClient {

    /**
     * Выполняет запрос прогноза погоды по координатам.
     *
     * @param lat   широта точки
     * @param lon   долгота точки
     * @param limit количество суток прогноза
     * @return сырой ответ сервера в формате JSON
     */
    String fetchForecastJson(double lat, double lon, int limit);
}
