package com.gmail.vusketta;

import com.gmail.vusketta.exceptions.NoSuchCommandException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.CopyMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
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
            FastScanner sc = new FastScanner(new File("src/main/resources/token.txt"));
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
        if (msg == null) return;
        if (msg.isCommand()) onCommand(msg);
        access.logMessage(msg);
    }

    private void onCommand(Message message) {
        String text = message.getText();
        Long chatId = message.getChatId();
        switch (text) {
            case "/start", "/start@PolyZXCBot" -> start(chatId);
            case "/help", "/help@PolyZXCBot" -> help(chatId);
            case "/all", "/all@PolyZXCBot" -> all(chatId);
            case "/spin", "/spin@PolyZXCBot" -> spin(message);
            case "/deadlines", "/deadlines@PolyZXCBot" -> deadlines(chatId);
            case "/pohuy", "/pohuy@PolyZXCBot" -> pohuy(message);
            default -> error.logException(new NoSuchCommandException());
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

    private void spin(Message message) {
        Long chatId = message.getChatId();
        deleteMessage(message);
        sendGif(chatId, "spin.mp4");
    }

    private void deadlines(Long chatId) {
        sendText(chatId, "Какая жалость... тут должны быть дедлайны, но их пока нет...");
    }

    private void pohuy(Message message) {
        Long chatId = message.getChatId();
        List<String> fileNames = List.of("pohuypapich.jpeg", "pohuycry.jpg", "pohuyvizl.jpg");
        String fileName = fileNames.get(
                new Random().nextInt(fileNames.size())
        );
        deleteMessage(message);
        sendImage(chatId, fileName);
    }

    private void sendImage(Long chatId, String fileName) {
        SendPhoto sp = SendPhoto.builder()
                .chatId(chatId.toString())
                .photo(new InputFile(new File("src/main/resources/" + fileName), fileName))
                .build();
        try {
            execute(sp);
        } catch (TelegramApiException e) {
            error.logException(e);
        }
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

    private void deleteMessage(Message message) {
        DeleteMessage dm = new DeleteMessage(message.getChatId().toString(), message.getMessageId());
        try {
            execute(dm);
        } catch (TelegramApiException e) {
            error.logException(e);
        }
    }
}
