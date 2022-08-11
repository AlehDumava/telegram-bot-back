package com.example.telegrambotback.service;

import java.io.IOException;

public interface WeatherService {

    String getWeather();

    String parse(String jsonString) throws IOException;
}
