package com.example.telegrambotback.config;

import com.example.telegrambotback.service.impl.TelegramBotServiceImpl;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class TelegramBotInitializerConfig {

    TelegramBotServiceImpl telegramBotServiceImpl;

    @EventListener({ContextRefreshedEvent.class})
    public  void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

        try {
            telegramBotsApi.registerBot(telegramBotServiceImpl);
        }
        catch (TelegramApiException e) {

        }
    }
}
