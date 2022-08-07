package com.example.telegrambotback.service;

public interface TelegramBotService {

    void startCommandReceived(long userId, String userName);

    void sendMessage(long userId, String textToSend);
}
