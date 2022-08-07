package com.example.telegrambotback.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class TelegramBotConfig {

    @Value(value = "${spring.bot.name}")
    private String botName;

    @Value(value = "${spring.bot.token}")
    private String botToken;
}
