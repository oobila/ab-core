package com.github.oobila.bukkit.sidecar.config;

import lombok.Getter;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.lang.reflect.Field;

public class ConfigInfo {

    @Getter
    private Plugin plugin;

    @Getter
    private String path;

    @Getter
    private File file;

    @Getter
    private Field field;

    @Getter
    long lastModified;

    public ConfigInfo(Plugin plugin, String path, File file, Field field) {
        this.plugin = plugin;
        this.path = path;
        this.file = file;
        this.field = field;
        this.lastModified = file.lastModified();
    }
}
