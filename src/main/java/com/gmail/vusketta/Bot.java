package com.gmail.vusketta;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.CopyMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.util.Scanner;

public class Bot extends TelegramLongPollingBot {
    private static Logger logger = new Logger();

    @Override
    public String getBotUsername() {
        return "PolyZXCBot";
    }

    @Override
    public String getBotToken() {
        String token;
        try {
            Scanner sc = new Scanner(new File("src/main/resources/token.txt"));
            token = sc.nextLine();
            sc.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return token; //Impossible to return null
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message msg = update.getMessage();
        User user = msg.getFrom();

        logger.logMessage(msg);

        copyMessage(user.getId(), msg.getMessageId());
    }

    private void sendText(Long chatId, String text) {
        SendMessage sm = SendMessage.builder()
                .chatId(chatId.toString())
                .text(text)
                .build();
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void copyMessage(Long chatId, Integer msgId) {
        CopyMessage cm = CopyMessage.builder()
                .fromChatId(chatId.toString())
                .chatId(chatId.toString())
                .messageId(msgId)
                .build();
        try {
            execute(cm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
