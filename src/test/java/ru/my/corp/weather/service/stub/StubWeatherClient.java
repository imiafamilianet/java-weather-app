package ru.my.corp.weather.service.stub;

import ru.my.corp.weather.service.api.WeatherClient;

/**
 * Тестовая заглушка (Stub) погодного клиента, имитирующая ответы REST API Яндекс Погоды v2.
 */
public class StubWeatherClient implements WeatherClient {

    /**
     * Эталонный JSON-ответ на 5 суток:
     * fact.temp = 2
     * temp_avg по дням: 5, 2, 1, 0, -2
     */
    public static final String DEFAULT_VALID_JSON = """
            {
              "now": 1795277040,
              "now_dt": "2026-11-21T16:24:00.000Z",
              "info": {
                "lat": 55.75,
                "lon": 37.62,
                "url": "https://yandex.ru"
              },
              "fact": {
                "temp": 2,
                "feels_like": -2,
                "condition": "overcast"
              },
              "forecasts": [
                {
                  "date": "2026-11-21",
                  "parts": { "day": { "temp_avg": 5 } }
                },
                {
                  "date": "2026-11-22",
                  "parts": { "day": { "temp_avg": 2 } }
                },
                {
                  "date": "2026-11-23",
                  "parts": { "day": { "temp_avg": 1 } }
                },
                {
                  "date": "2026-11-24",
                  "parts": { "day": { "temp_avg": 0 } }
                },
                {
                  "date": "2026-11-25",
                  "parts": { "day": { "temp_avg": -2 } }
                }
              ]
            }
            """;

    private final String jsonToReturn;

    public StubWeatherClient() {
        this.jsonToReturn = DEFAULT_VALID_JSON;
    }

    public StubWeatherClient(String customJson) {
        this.jsonToReturn = customJson;
    }

    @Override
    public String fetchForecastJson(double lat, double lon, int limit) {
        return jsonToReturn;
    }
}
