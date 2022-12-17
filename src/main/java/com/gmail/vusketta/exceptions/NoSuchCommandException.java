package com.gmail.vusketta.exceptions;

public class NoSuchCommandException extends Exception {
    public NoSuchCommandException(String command) {
        super("Do not support such command: " + command);
    }
}
