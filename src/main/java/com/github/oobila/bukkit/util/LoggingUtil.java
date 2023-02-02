package com.github.oobila.bukkit.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;

import java.util.concurrent.Callable;
import java.util.logging.Level;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoggingUtil {

    public static <T> T runWithLoggingOff(Callable<T> callable) {
        Level level = Bukkit.getLogger().getLevel();
        Bukkit.getLogger().setLevel(Level.OFF);
        try {
            return callable.call();
        } catch (Exception e) {
            Bukkit.getLogger().setLevel(level);
            Bukkit.getLogger().severe(e.getMessage());
            return null;
        } finally {
            Bukkit.getLogger().setLevel(level);
        }
    }

}
