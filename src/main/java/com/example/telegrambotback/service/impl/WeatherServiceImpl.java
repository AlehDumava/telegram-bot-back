package com.example.telegrambotback.service.impl;

import static java.time.temporal.ChronoUnit.SECONDS;

import com.example.telegrambotback.dto.WeatherDto;
import com.example.telegrambotback.dto.WeatherForecastDto;
import com.example.telegrambotback.service.WeatherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    @Value(value = "${environment.yandex.key}")
    private String key;

    @Override
    public String getWeather() {

        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://api.weather.yandex.ru/v1/informers"))
                .header("lat", "55.1904")
                .header("lon", "30.2049")
                .header("X-Yandex-API-Key", key)
                .timeout(Duration.of(10, SECONDS))
                .GET()
                .build();

            HttpResponse<String> response = HttpClient.newBuilder()
                .build()
                .send(request, BodyHandlers.ofString());

            return response.body();

        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return "Empty string";
    }

    @Override
    public String parseWeatherCurrent(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            WeatherDto weather = objectMapper.readValue(jsonString, WeatherDto.class);

            String message = "Date and Time: " + weather.getNow_dt() + "\n" +
                "Temp: " + weather.getFact().getTemp() + "\n" +
                "Wind speed: " + weather.getFact().getWind_speed() + "\n" +
                "Pressure: " + weather.getFact().getPressure_mm() + "\n" +
                "Humidity: " + weather.getFact().getHumidity() + "\n" +
                "Condition: " + weather.getFact().getCondition();

            return message;

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "Empty string";
    }

    @Override
    public String parseWeatherForecast(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            WeatherDto weather = objectMapper.readValue(jsonString, WeatherDto.class);

            String message = "Date of the forecast: " + weather.getForecast().getDate() + "\n" +
                "Time of the sunrise: " + weather.getForecast().getSunrise() + "\n" +
                "Time of the sunset: " + weather.getForecast().getSunset() + "\n" +
                "The lunar phase: " + weather.getForecast().getMoon_text() + "\n" +
                "Minimum temperature: " + weather.getForecast().getParts().get(0).getTemp_min() + "\n" +
                "Maximum temperature: " + weather.getForecast().getParts().get(0).getTemp_max() + "\n" +
                "Condition: " + weather.getForecast().getParts().get(0).getCondition();

            return message;

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "Empty string";
    }
}
