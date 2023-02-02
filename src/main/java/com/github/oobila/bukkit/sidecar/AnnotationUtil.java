package com.github.oobila.bukkit.sidecar;

import com.github.oobila.bukkit.util.LoggingUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AnnotationUtil {

    private static final Map<Plugin, Reflections> reflectionMap = new HashMap<>();

    public static Reflections getReflections(Plugin plugin) {
        return reflectionMap.computeIfAbsent(plugin, AnnotationUtil::loadReflections);
    }

    private static Reflections loadReflections(Plugin plugin) {
        URLClassLoader classLoader = URLClassLoader.newInstance(
                new URL[]{
                        plugin.getClass().getProtectionDomain().getCodeSource().getLocation()
                }
        );
        Thread.currentThread().setContextClassLoader(classLoader);
        return LoggingUtil.runWithLoggingOff(() ->
                new Reflections(ClasspathHelper.forClassLoader(classLoader), Scanners.FieldsAnnotated)
        );
    }

}
