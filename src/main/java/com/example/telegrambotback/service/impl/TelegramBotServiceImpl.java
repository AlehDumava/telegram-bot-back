package com.example.telegrambotback.service.impl;

import com.example.telegrambotback.config.TelegramBotConfig;
import com.example.telegrambotback.service.TelegramBotService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Service
@Getter
public class TelegramBotServiceImpl extends TelegramLongPollingBot implements TelegramBotService {

    private final TelegramBotConfig telegramBotConfig;
    private final WeatherServiceImpl weatherService;

    static final String HELP_TEXT = "What can this bot do?";

    public TelegramBotServiceImpl(TelegramBotConfig telegramBotConfig, WeatherServiceImpl weatherService) {

        this.telegramBotConfig = telegramBotConfig;
        this.weatherService = weatherService;

        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "welcome message"));
        listOfCommands.add(new BotCommand("/help", "help message"));
        listOfCommands.add(new BotCommand("/mydata", "get your data"));
        listOfCommands.add(new BotCommand("/delete", "delete your data"));
        listOfCommands.add(new BotCommand("/weather", "get current weather"));
        listOfCommands.add(new BotCommand("/genres", "get genres"));
        listOfCommands.add(new BotCommand("/games", "get games"));

        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot's command list: " +
                    LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
    }

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

                case "/help":
                    sendMessage(chatId, HELP_TEXT);
                    log.info("Send help text to user: " + userName + " " +
                            LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                    break;

                case "/weather":
                    String jsonString = weatherService.getWeather();
                    sendMessage(chatId, "The current weather is: " + "\n" +
                            weatherService.parseWeatherCurrent(jsonString));

                    sendMessage(chatId, "The forecast weather is: " + "\n" +
                        weatherService.parseWeatherForecast(jsonString));

                    log.info("Send weather to user: " + userName + " == " +
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
