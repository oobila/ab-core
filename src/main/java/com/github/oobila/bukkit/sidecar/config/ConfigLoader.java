package com.github.oobila.bukkit.sidecar.config;

import com.github.oobila.bukkit.CorePlugin;
import com.github.oobila.bukkit.sidecar.AnnotationUtil;
import com.github.oobila.bukkit.sidecar.SidecarConfiguration;
import com.github.oobila.bukkit.util.text.MessageBuilder;
import com.github.oobila.bukkit.util.text.NotificationManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.reflections.Reflections;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConfigLoader {

    private static final String CONFIG_RELOAD = "config-reload";
    private static List<ConfigInfo> configs = new ArrayList<>();
    private static Map<String, List<ConfigReloadObserver>> configReloadObservers = new HashMap<>();
    static {
        configReloadObservers.put(null, new ArrayList<>());
    }
    private static int iterator = 0;
    private static boolean fileListenerStarted = false;

    public static void load(Plugin plugin) {
        Reflections reflections = AnnotationUtil.getReflections(plugin);
        reflections.getFieldsAnnotatedWith(PluginConfig.class).forEach(field -> {
            //annotation
            PluginConfig annotation = field.getDeclaredAnnotation(PluginConfig.class);
            String path = annotation.path();

            //file
            File file = new File(plugin.getDataFolder(), path);
            configs.add(new ConfigInfo(plugin, path, file, field));
            startFileListener();

            //defaults
            if (!file.exists()) {
                copyDefaults(plugin, path, file);
            }

            //load
            loadIntoField(field, file);
        });
    }

    @SuppressWarnings("java:S3011")
    static void loadIntoField(Field field, File file) {
        try {
            ParameterizedType type = (ParameterizedType) field.getGenericType();
            field.setAccessible(true);
            field.set(
                    null,
                    SidecarConfiguration.load(
                            file,
                            field.getType(),
                            (Class<?>) type.getActualTypeArguments()[0]
                    )
            );
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void startFileListener() {
        if (!fileListenerStarted) {
            Bukkit.getScheduler().runTaskTimer(CorePlugin.getInstance(),() -> {
                iterator++;
                if (iterator >= configs.size()) {
                    iterator = 0;
                }

                ConfigInfo configInfo = configs.get(iterator);
                if (!configInfo.getPlugin().isEnabled()) {
                    return;
                }

                if (!configInfo.getFile().exists()) {
                    copyDefaults(configInfo.getPlugin(), configInfo.getPath(), configInfo.getFile());
                }

                if (configInfo.getFile().lastModified() != configInfo.lastModified) {
                    loadIntoField(configInfo.getField(), configInfo.getFile());
                    configInfo.lastModified = configInfo.getFile().lastModified();
                    notifyObservers(configInfo.getPath());
                    Bukkit.getOnlinePlayers().stream().filter(Player::isOp).forEach(op ->
                            NotificationManager.sendNotification(op,
                                    new MessageBuilder(CorePlugin.getLanguage().get(CONFIG_RELOAD))
                                            .variable(configInfo.getPath())
                                            .variable(configInfo.getPlugin().getName()))
                    );
                    Bukkit.getLogger().log(Level.INFO, "Config {} was reloaded for plugin: {}",
                            new String[]{configInfo.getPath(), configInfo.getPlugin().getName()});
                }
            },1000,100);
            fileListenerStarted = true;
        }
    }

    private static void copyDefaults(Plugin plugin, String path, File file) {
        file.getParentFile().mkdirs();
        try (InputStream inputStream = plugin.getResource(path);
             OutputStream outputStream = new FileOutputStream(file)) {
            if (inputStream != null) {
                byte[] buffer = new byte[1024];
                int len;
                while((len = inputStream.read(buffer))>0) {
                    outputStream.write(buffer,0,len);
                }
            }
            outputStream.flush();
        } catch (IOException e) {
            //silent catch in case no default file exists
        }
    }

    public static void addObserver(ConfigReloadObserver configReloadObserver) {
        addObserver(null, configReloadObserver);
    }

    public static void addObserver(String string, ConfigReloadObserver configReloadObserver) {
        configReloadObservers.computeIfAbsent(string, s -> new ArrayList<>());
        configReloadObservers.get(string).add(configReloadObserver);
    }

    private static void notifyObservers(String path) {
        configReloadObservers.get(null).forEach(configReloadObserver -> configReloadObserver.onReload(path));
        List<ConfigReloadObserver> observers = configReloadObservers.get(path);
        if (observers != null) {
            observers.forEach(configReloadObserver -> configReloadObserver.onReload(path));
        }
    }

}
