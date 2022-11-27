package com.gmail.vusketta;

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.HashMap;
import java.util.Map;

public class DeadlineCommand extends BotCommand {

    public DeadlineCommand() {
        super("deadline", "In process...");
    }

    private static final Map<String, Integer> GROUP_CHAT_ID = new HashMap<>();
}
