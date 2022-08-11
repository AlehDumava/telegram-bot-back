package com.example.telegrambotback.service.impl;

import com.example.telegrambotback.config.TelegramBotConfig;
import com.example.telegrambotback.service.TelegramBotService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramBotServiceImpl extends TelegramLongPollingBot implements TelegramBotService {

    private final TelegramBotConfig telegramBotConfig;
    private final WeatherServiceImpl weatherService;

    @Override
    public String getBotUsername() {
        return telegramBotConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return telegramBotConfig.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();

            long chatId = update.getMessage().getChatId();
            String userName = update.getMessage().getChat().getFirstName();

            switch (messageText) {
                case "/start":
                    startCommandReceived(chatId, userName);
                    break;
                case "/weather":
                    String jsonString = weatherService.getWeather();
                    sendMessage(chatId, "The current weather is: " + "\n" +
                        weatherService.parse(jsonString));

                    log.info("Send weather to user: " + userName + " " +
                        LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                    break;
                default:
                    sendMessage(chatId, "Sorry, this command was not recognized!");
            }
        }
    }

    @Override
    public void startCommandReceived(long chatId, String userName) {
        String answer = "Hi, " + userName + ", nice to meet you!";
        sendMessage(chatId, answer);
        log.info("Replied to user: " + userName + " " +
            LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    @Override
    public void sendMessage(long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(chatId));
            sendMessage.setText(textToSend);

        try {
            execute(sendMessage);

        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }
}
