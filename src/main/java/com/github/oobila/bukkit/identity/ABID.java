package com.github.oobila.bukkit.identity;

import org.apache.commons.lang.StringUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ABID {

    private static final char[] CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static long instant;
    private static AtomicInteger counter = new AtomicInteger();

    private String id;

    public ABID() {
        this.id = generate();
    }

    private ABID(String string) {
        this.id = string;
    }

    @Override
    public String toString() {
        return id;
    }

    public static ABID fromString(String string) {
        return new ABID(string);
    }

    private static String generate() {
        long now = getTimeElement();
        if (instant != now) {
            instant = now;
            counter.set(0);
        }
        int c = counter.getAndIncrement();
        if (c > (Math.pow(CHARS.length, 2) - 1)) {
            throw new RuntimeException("Too many IDs generated at the same time. New Ids could not be created as they will not be unique. Try creating fewer Ids within the same second");
        }
        String cString = StringUtils.rightPad(String.valueOf(now), 10, CHARS[0]);
        cString = StringUtils.rightPad(cString + c, 12, CHARS[0]);
        return encode(Long.valueOf(cString), CHARS);
    }

    private static long getTimeElement() {
        long now = Instant.now().toEpochMilli();
        int intValue = (int) Math.floor(now/1000);
        return intValue;
    }

    private static String encode(long victim, char[] chars) {
        final List<Character> list = new ArrayList<>();
        final int length = chars.length;

        do {
            list.add(chars[(int) (victim % length)]);
            victim /= length;
        } while (victim > 0);

        StringBuilder stringBuilder = new StringBuilder();
        list.forEach(stringBuilder::append);
        return stringBuilder.toString();
    }
}