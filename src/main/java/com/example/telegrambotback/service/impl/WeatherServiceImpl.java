package com.example.telegrambotback.service.impl;

import static java.time.temporal.ChronoUnit.SECONDS;

import com.example.telegrambotback.entity.Weather;
import com.example.telegrambotback.service.WeatherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
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
    private String KEY;

    @Override
    public String getWeather() {

        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://api.weather.yandex.ru/v1/informers"))
                .header("lat", "55.1904")
                .header("lon", "30.2049")
                .header("X-Yandex-API-Key", KEY)
                .timeout(Duration.of(10, SECONDS))
                .GET()
                .build();

            HttpResponse<String> response = HttpClient.newBuilder()
                .build()
                .send(request, BodyHandlers.ofString());

            return response.body();

            } catch(URISyntaxException | IOException | InterruptedException e){
                e.printStackTrace();
            }
        return "Empty string";
    }

    @Override
    public String parse(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            String nowDt = jsonNode.get("now_dt").asText();

            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Weather weather = objectMapper.readValue(jsonString, Weather.class);

            String message = "Date and Time: " + weather.getNow_dt() + "\n" +
                "Temp: " + weather.getFact().getTemp() + "\n" +
                "Wind speed: " + weather.getFact().getWind_speed() + "\n" +
                "Pressure: " + weather.getFact().getPressure_mm() + "\n" +
                "Humidity: " + weather.getFact().getHumidity() + "\n" +
                "Condition: " +weather.getFact().getCondition();

            return message;

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "----";
    }
}
