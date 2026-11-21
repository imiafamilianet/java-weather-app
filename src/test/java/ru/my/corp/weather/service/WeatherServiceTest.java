package ru.my.corp.weather.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.my.corp.weather.service.api.WeatherParser;
import ru.my.corp.weather.service.impl.JacksonWeatherParser;
import ru.my.corp.weather.service.impl.WeatherServiceImpl;
import ru.my.corp.weather.service.stub.StubWeatherClient;

import static org.junit.jupiter.api.Assertions.*;

class WeatherServiceTest {

    private WeatherParser weatherParser;

    @BeforeEach
    void setUp() {
        weatherParser = new JacksonWeatherParser(new ObjectMapper());
    }

    @Test
    @DisplayName("Корректный парсинг текущей температуры и среднего прогноза за 5 суток")
    void shouldParseCurrentAndAverageTempForFiveDays() {
        var report = new WeatherServiceImpl(new StubWeatherClient(), weatherParser).getWeatherReport(55.75, 37.62, 5);
        assertAll(
                () -> assertNotNull(report),
                () -> assertEquals(StubWeatherClient.DEFAULT_VALID_JSON, report.rawJson()),
                () -> assertEquals(2, report.currentTemp(), "Текущая температура должна быть 2"),
                () -> assertEquals(5, report.daysCount(), "Должно быть учтено 5 суток"),
                () -> assertEquals(1.2, report.averageForecastTemp(), 0.001, "Средняя температура за 5 дней должна быть 1.2")
        );


    }

    @Test
    @DisplayName("Расчет среднего только по первым limit элементам (например, за 3 суток из 5)")
    void shouldCalculateAverageOnlyForRequestedLimitDays() {
        var stubClient = new StubWeatherClient();
        var weatherService = new WeatherServiceImpl(stubClient, weatherParser);

        var report = weatherService.getWeatherReport(55.75, 37.62, 3);

        assertEquals(3, report.daysCount());
        assertEquals(2.67, report.averageForecastTemp(), 0.01);
    }

    @Test
    @DisplayName("Если limit больше доступного числа дней в ответе, считаем по всем доступным дням")
    void shouldHandleLimitGreaterThanForecastArraySize() {
        var report = new WeatherServiceImpl(new StubWeatherClient(), weatherParser).getWeatherReport(55.75, 37.62, 10);

        assertEquals(5, report.daysCount(), "Фактическое число дней не должно превышать размер массива forecasts");
        assertEquals(1.2, report.averageForecastTemp(), 0.001);
    }

    @Test
    @DisplayName("Исключение при передаче некорректного параметра limit <= 0")
    void shouldThrowExceptionWhenLimitIsNotPositive() {
        assertThrows(IllegalArgumentException.class,
                () -> new WeatherServiceImpl(new StubWeatherClient(), weatherParser)
                        .getWeatherReport(55.75, 37.62, 0));
    }

    @Test
    @DisplayName("Исключение при отсутствии обязательного поля fact.temp в ответе API")
    void shouldThrowExceptionWhenFactTempIsMissing() {
        String brokenJson = """
                {
                  "fact": { "condition": "cloudy" },
                  "forecasts": []
                }
                """;
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> new WeatherServiceImpl(new StubWeatherClient(brokenJson), weatherParser).getWeatherReport(55.75, 37.62, 5));
        assertTrue(ex.getMessage().contains("fact.temp"));
    }

    @Test
    @DisplayName("Исключение при невалидном JSON в ответе")
    void shouldThrowExceptionOnMalformedJson() {
        assertThrows(RuntimeException.class,
                () -> new WeatherServiceImpl(new StubWeatherClient("{ invalid_json "), weatherParser).getWeatherReport(55.75, 37.62, 5));
    }
}
