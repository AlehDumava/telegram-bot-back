package com.example.telegrambotback.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherFactDto {

    private int obs_time;
    private int temp;
    private int feels_like;
    private String icon;
    private String condition;
    private int wind_speed;
    private String wind_dir;
    private int pressure_mm;
    private int pressure_pa;
    private int humidity;
    private String daytime;
    private Boolean polar;
    private String season;
    private int wing_gust;
}
