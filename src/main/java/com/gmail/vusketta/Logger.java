package com.gmail.vusketta;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class Logger {
    private static String fileName;

    public Logger(final String fileName) {
        Logger.fileName = fileName;
    }

    public void logMessage(Message message) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("[").append(new Date()).append("] ");
            sb.append("user = \"").append(message.getFrom().getUserName()).append("\"");
            if (message.hasText()) sb.append(", ").append("text = \"").append(message.getText()).append("\"");
            if (message.hasSticker()) {
                Sticker sticker = message.getSticker();
                sb.append(", ").append("sticker = \"").append(sticker.getEmoji()).append(" ").append(sticker.getSetName()).append("\"");
            }
            log(sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void logException(Exception exception) {
        try {
            log(exception.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void log(String text) throws IOException {
        System.out.println(text);
        try {
            BufferedWriter logger = new BufferedWriter(
                    new FileWriter("src\\main\\resources\\" + fileName, true));
            logger.write(text);
            logger.newLine();
            logger.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
