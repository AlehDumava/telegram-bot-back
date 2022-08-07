package com.example.telegrambotback.service.impl;

import com.example.telegrambotback.config.TelegramBotConfig;
import com.example.telegrambotback.service.TelegramBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@RequiredArgsConstructor
public class TelegramBotServiceImpl extends TelegramLongPollingBot implements TelegramBotService {

    private final TelegramBotConfig telegramBotConfig;

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

            long userId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start":
                    startCommandReceived(userId, update.getMessage().getChat().getFirstName());
                    break;
                default:
                    sendMessage(userId, "Sorry, this command not recognized!");
            }
        }
    }

    @Override
    public void startCommandReceived(long userId, String userName) {
        String answer = "Hi, " + userName + ", nice to meet you!";
        sendMessage(userId, answer);
    }

    @Override
    public void sendMessage(long userId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(userId));
        message.setText(textToSend);

        try {
            execute(message);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
