package com.example.telegrambotback.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Data
@Configuration
public class TelegramBotConfig {

    public Environment environment;

    @Value(value = "${environment.bot.name}")
    private String botName;

    @Value(value = "${environment.bot.token}")
    private String botToken;
}
