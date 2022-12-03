package com.gmail.vusketta;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FastScanner implements AutoCloseable {
    final private int BUFFER_SIZE = 1 << 7;
    private byte[] buffer = new byte[BUFFER_SIZE];
    private int bufferPointer, bytesRead;
    private InputStream in;

    public FastScanner(final InputStream inputStream) {
        in = inputStream;
        buffer = new byte[BUFFER_SIZE];
        bufferPointer = bytesRead = 0;
    }

    public FastScanner(File file) throws FileNotFoundException {
        in = new FileInputStream(file);
    }

    public FastScanner() {
        new FastScanner(System.in);
    }

    public FastScanner(String s) {
        in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        buffer = new byte[BUFFER_SIZE];
        bufferPointer = bytesRead = 0;
    }

    private char nextChar() throws IOException {
        char c;
        int octet1 = read();
        if (octet1 == -1) {
            return Character.MIN_VALUE;
        }
        int cntUnits = 0;
        for (int i = 7; i >= 0; i--) {
            if (((1 << i) & octet1) == 0) {
                break;
            }
            cntUnits++;
        }
        c = (char) (octet1 % (1 << (7 - cntUnits)));
        for (int i = 0; i < cntUnits - 1; i++) {
            int nextOctet = read();
            c = (char) ((c << 6) + (nextOctet % (1 << 6)));
        }
        return c;
    }

    public String next() throws IOException {
        StringBuilder str = new StringBuilder();
        char c = nextChar();
        while (Character.isWhitespace(c)) {
            c = nextChar();
        }
        while (!Character.isWhitespace(c)) {
            str.append(c);
            c = nextChar();
        }
        return str.toString();
    }

    public int nextInt() throws IOException {
        StringBuilder strToInt = new StringBuilder();
        char c = nextChar();
        while (Character.isWhitespace(c)) {
            c = nextChar();
        }
        while (!Character.isWhitespace(c)) {
            strToInt.append(c);
            c = nextChar();
        }
        return Integer.parseInt(strToInt.toString());
    }

    public long nextLong() throws IOException {
        StringBuilder strToLong = new StringBuilder();
        char c = nextChar();
        while (Character.isWhitespace(c)) {
            c = nextChar();
        }
        while (!Character.isWhitespace(c)) {
            strToLong.append(c);
            c = nextChar();
        }
        return Long.parseLong(strToLong.toString());
    }

    public double nextDouble() throws IOException {
        StringBuilder strToDouble = new StringBuilder();
        char c = nextChar();
        while (Character.isWhitespace(c)) {
            c = nextChar();
        }
        while (!Character.isWhitespace(c)) {
            strToDouble.append(c);
            c = nextChar();
        }
        return Double.parseDouble(strToDouble.toString());
    }

    public boolean hasNextLine() throws IOException {
        read();
        bufferPointer--;
        return bytesRead != -1;
    }

    public boolean hasNext() throws IOException {
        int c = read();
        int pointer = 1;
        while (Character.isWhitespace(c) && bufferPointer < bytesRead) {
            c = read();
            pointer++;
        }
        bufferPointer -= pointer;
        return !Character.isWhitespace(c) && bytesRead != -1;
    }

    public String nextLine() throws IOException {
        StringBuilder str = new StringBuilder();
        char lineSep = System.lineSeparator().charAt(0);
        char c = nextChar();
        while (bytesRead != -1) {
            if (c == lineSep)
                break;
            str.append(c);
            c = nextChar();
        }
        return str.toString();
    }

    public String readText() throws IOException {
        StringBuilder str = new StringBuilder();
        while (hasNextLine()) {
            str.append(nextLine());
        }
        return str.toString();
    }

    private void fillBuffer() throws IOException {
        do {
            bytesRead = in.read(buffer);
        } while (bytesRead == 0);
        bufferPointer = 0;
    }

    private int read() throws IOException {
        if (bufferPointer == bytesRead)
            fillBuffer();
        bufferPointer++;
        return (buffer[bufferPointer - 1] < 0) ? buffer[bufferPointer - 1] + (1 << 8) : buffer[bufferPointer - 1];
    }

    public void close() throws IOException {
        if (in == null)
            return;
        in.close();
    }
}