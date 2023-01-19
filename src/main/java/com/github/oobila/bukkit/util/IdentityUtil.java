package com.github.oobila.bukkit.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IdentityUtil {

    private static final char[] CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final char[] CHARS_2 = "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();

    private static long instant;
    private static AtomicInteger counter = new AtomicInteger();

    public static String newId() {
        long now = Instant.now().toEpochMilli();
        if (instant != now) {
            instant = now;
            counter.set(0);
        }
        int c = counter.getAndIncrement();
        String cString = StringUtils.rightPad(String.valueOf(now), 13, CHARS[0]);
        cString = StringUtils.rightPad(cString + c, 15, CHARS[0]);
        return encode(Long.valueOf(cString), CHARS);
    }

    public static String newLowercaseId() {
        long now = Instant.now().toEpochMilli();
        if (instant != now) {
            instant = now;
            counter.set(0);
        }
        int c = counter.getAndIncrement();
        String cString = StringUtils.rightPad(String.valueOf(now), 13, CHARS_2[0]);
        cString = StringUtils.rightPad(cString + c, 15, CHARS_2[0]);
        return encode(Long.valueOf(cString), CHARS_2);
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