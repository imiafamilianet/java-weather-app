package ru.my.corp.weather.model;

/**
 * Модель отчета о метеорологических данных.
 *
 * @param rawJson             Полный необработанный JSON-ответ от API.
 * @param currentTemp         Текущая фактическая температура (fact.temp).
 * @param averageForecastTemp Средняя дневная температура по прогнозу за запрошенное количество суток.
 * @param daysCount           Фактическое количество суток, по которым рассчитана средняя температура.
 */
public record WeatherReport(String rawJson, double currentTemp, double averageForecastTemp, int daysCount) {}
