package com.example.telegrambotback.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Weather {

        private int now;

        private String now_dt;

        WeatherFact fact;
}
