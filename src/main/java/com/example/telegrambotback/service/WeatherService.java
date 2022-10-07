package com.example.telegrambotback.service;

public interface WeatherService {

    String getWeather();

    String parseWeatherCurrent(String jsonString);

    String parseWeatherForecast(String jsonString);
}
