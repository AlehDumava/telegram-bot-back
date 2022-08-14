package com.example.telegrambotback.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherDto {

        private int now;

        private String now_dt;

        private WeatherFactDto fact;

        private WeatherForecastDto forecast;
}
