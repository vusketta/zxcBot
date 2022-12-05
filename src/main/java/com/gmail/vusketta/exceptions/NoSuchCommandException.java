package com.gmail.vusketta.exceptions;

public class NoSuchCommandException extends Exception {
    public NoSuchCommandException() {
        super("Do not support such command.");
    }
}
