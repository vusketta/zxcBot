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
        if (!update.hasMessage()) return; // Attention
        Message msg = update.getMessage();
        if (msg == null) return;
        if (msg.isCommand()) onCommand(msg);
        access.logMessage(msg);
    }

    private void onCommand(Message message) {
        String text = message.getText().toLowerCase();
        Long chatId = message.getChatId();
        switch (text) {
            case "/start", "/start@PolyZXCBot" -> start(chatId);
            case "/help", "/help@PolyZXCBot" -> help(chatId);                 //In process... (now only for troll)
            case "/all", "/all@PolyZXCBot" -> all(chatId);
            case "/spin", "/spin@PolyZXCBot" -> spin(message);
            case "/deadlines", "/deadlines@PolyZXCBot" -> deadlines(message); //In process... (now only for troll)
            case "/pohuy", "/pohuy@PolyZXCBot" -> pohuy(message);
            case "/sourcecode", "/sourcecode@PolyZXCBot" -> sourcecode(message);
            case "/gelich", "/gelich@PolyZXCBot" -> gelich(chatId);
            default -> error.logException(new NoSuchCommandException(text));
        }
    }

    private void start(Long chatId) {
        List<String> startMessages = List.of("?? ???????????? ?? ??????????!", "?????????? ???????? ?? ?????? ????????????!",
                "?????????????? ??????, ?????????????? ??????!", "???????????????? ??????????????????!", "??????????!", "???? ???????????????", "????????????????????????, ????????????????!",
                "?????? ?????????????????????", "???? ???????? ?????????? ????????????! ???? ???? ???????", "??????-????????, ???? ?????????????? ????-???? ????????.",
                "?????? ?????????????? ?????? ?? ??????-???? ?????? ???????????", "?????????? ?????????? ???? ???????????", "????????????, ????????????!", "?????????? ???????? ????????????????????????!");
        String startMessage = startMessages.get(
                new Random().nextInt(startMessages.size())
        );
        sendText(chatId, startMessage);
    }

    private void help(Long chatId) {
        sendText(chatId, "???????????? ???????? ??????.");
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

    private void deadlines(Message message) {
        Long chatId = message.getChatId();
        deleteMessage(message);
        sendText(chatId, "?????????????? ???? ???????????? - ???????? ?????????????? ?? 23:59.");
        sendText(chatId, "?????????????? ???? ???????????? - ?????? ?????????????? ?? 23:59.");
        sendText(chatId, "?????????????? ???? ??????????, ?????? ?? ?????? ???? ?????????? https://openedu.ru/, - 25.12.2022.");
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

    private void sourcecode(Message message) {
        sendText(message.getChatId(), "https://github.com/vusketta/zxcBot/blob/master/src/main/java/com/gmail/vusketta/Bot.java");
    }

    private void gelich(Long chatId) {
        sendGif(chatId, "gelich.mp4");
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
