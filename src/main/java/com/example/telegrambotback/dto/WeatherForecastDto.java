package com.example.telegrambotback.dto;

import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherForecastDto {

    private String date;
    private int date_ts;
    private int week;
    private String sunrise;
    private String sunset;
    private int moon_code;
    private String moon_text;

    private ArrayList<WeatherForecastPartsDto> parts;

}
