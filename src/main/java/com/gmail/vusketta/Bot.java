package com.gmail.vusketta;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.CopyMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.util.*;

public class Bot extends TelegramLongPollingBot {
    private static final Logger access = new Logger("access.log");
    private static final Logger error = new Logger("error.log");

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
            error.logException(e);
            throw new RuntimeException(e);
        }
        return token; //Impossible to return null
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message msg = update.getMessage();
        User user = msg.getFrom();
        Integer messageId = msg.getMessageId();

        access.logMessage(msg);

        if (msg.isCommand()) {
            onCommand(msg);
        } else {
            // copyMessage(user.getId(), messageId);
        }
    }

    private void onCommand(Message message) {
        String text = message.getText();
        Long chatId = message.getChatId();
        switch (text) {
            case "/start", "/start@PolyZXCBot" -> start(chatId);
            case "/help" -> help(chatId);
            case "/all" -> all(chatId);
            case "/spin" -> spin(chatId);
            case "/deadlines" -> deadlines(chatId);
            default -> throw new IllegalArgumentException();
        }
    }

    private void start(Long chatId) {
        List<String> startMessages = List.of("Я пришел с миром!", "Какие люди и без охраны!",
                "Сколько лет, сколько зим!", "Отличное выглядишь!", "Салют!", "Мы знакомы?", "Здравствуйте, товарищи!",
                "Как настроение?", "Не верю своим глазам! Ты ли это?", "Гоп-стоп, мы подошли из-за угла.",
                "Мне кажется или я где-то вас видел?", "Какие планы на вечер?", "Привет, чуваки!", "Какие люди нарисовались!");
        String startMessage = startMessages.get(
                new Random().nextInt(startMessages.size())
        );
        sendText(chatId, startMessage);
    }

    private void help(Long chatId) {
        sendText(chatId, "Помоги себе сам.");
    }

    private void all(Long chatId) {
        Set<String> allMembers = Set.of(
                "@gachibasstruehardbass", "@Tilda_Tiltoria", "@simonsoff",
                "@Andrienno", "@bro_from_heaven", "@dzfennec",
                "@Ars1y", "@mariiafo", "@lrawd3",
                "@masturmater", "@ainzimba", "@Urran",
                "@uwugalaxie", "@genetyc", "@iljaps");
        String members = allMembers.toString();
        sendText(chatId, members.substring(1, members.length() - 1));
    }

    private void spin(Long chatId) {
        sendGif(chatId, "spin.mp4");
    }

    private void deadlines(Long chatId) {
//        String[] deadlines;
//
//        try {
//            FastScanner scanner = new FastScanner(new File("src/main/resources/deadlines.txt"));
//            deadlines = scanner.readText().split("</d/>");
//            scanner.close();
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        if (deadlines.length == 0) {
//            sendText(chatId, "Какая жалость... тут должны быть дедлайны, но их пока нет...");
//        } else {
//            StringBuilder stringBuilder = new StringBuilder();
//            for (int i = 1; i < deadlines.length + 1; i++) {
//                stringBuilder.append(i).append(") ").append(deadlines[i]);
//            }
//            sendText(chatId, stringBuilder.toString());
//        }
    }

    private void sendGif(Long chatId, String fileName) {
        SendAnimation sa = SendAnimation.builder()
                .chatId(chatId.toString())
                .animation(new InputFile(new File("src/main/resources/" + fileName), fileName))
                .build();
        try {
            execute(sa);
        } catch (TelegramApiException e) {
            error.logException(e);
        }
    }

    private void sendText(Long chatId, String text) {
        SendMessage sm = SendMessage.builder()
                .chatId(chatId.toString())
                .text(text)
                .build();
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            error.logException(e);
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
            error.logException(e);
        }
    }
}
