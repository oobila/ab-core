package com.github.oobila.bukkit.sidecar;

import com.github.oobila.bukkit.sidecar.keyserializer.KeySerializer;
import com.github.oobila.bukkit.sidecar.keyserializer.OfflinePlayerSerializer;
import com.github.oobila.bukkit.sidecar.keyserializer.StringSerializer;
import com.github.oobila.bukkit.sidecar.keyserializer.UUIDSerializer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SidecarConfiguration {

    private static final String DATA = "data";

    public static <T, S> void save(Plugin plugin, String path, T data, Class<S> keyType) throws IOException {
        save(new File(plugin.getDataFolder(), path), data, keyType);
    }

    public static <T, S> void save(File file, T data, Class<S> keyType) throws IOException {
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
        fileConfiguration.getValues(false).forEach((s, o) ->
            fileConfiguration.set(s, null)
        );

        Map<String, Object> map = new HashMap<>();
        if (Map.class.isAssignableFrom(data.getClass())) {
            ((Map<S, Object>) data).forEach((s, o) ->
                map.put(((KeySerializer<S>) Serialization.getKeySerializers().get(keyType)).serialize(s), o)
            );
        } else if (List.class.isAssignableFrom(data.getClass())) {
            List<?> list = (List<?>) data;
            for (int i = 0; i < list.size(); i++) {
                map.put(i + "", list.get(i));
            }
        } else if (Set.class.isAssignableFrom(data.getClass())) {
            Set<?> set = (Set<?>) data;
            int i = 0;
            for (Object object : set) {
                map.put(i + "", object);
                i++;
            }
        } else {
            map.put(DATA, data);
        }

        map.forEach(fileConfiguration::set);
        fileConfiguration.save(file);
    }

    public static <T,S> T load(Plugin plugin, String path, Class<T> objectType, Class<S> keyType) {
        return load(new File(plugin.getDataFolder(), path), objectType, keyType);
    }

    public static <T,S> T load(InputStream inputStream, Class<T> objectType, Class<S> keyType) {
        return load(
                YamlConfiguration.loadConfiguration(new InputStreamReader(inputStream, StandardCharsets.UTF_8)),
                objectType,
                keyType
        );
    }

    public static <T,S> T load(File file, Class<T> objectType, Class<S> keyType) {
        return load(
                YamlConfiguration.loadConfiguration(file),
                objectType,
                keyType
        );
    }

    public static <T,S> T load(FileConfiguration fileConfiguration, Class<T> objectType, Class<S> keyType) {
        if (Map.class.isAssignableFrom(objectType)) {
            Map<S, Object> map = new HashMap<>();
            fileConfiguration.getValues(false).forEach((s, o) ->
                map.put((S) Serialization.getKeySerializers().get(keyType).deserialize(s), o)
            );
            return (T) map;
        } else if (List.class.isAssignableFrom(objectType)) {
            return (T) fileConfiguration.getValues(false).entrySet().stream()
                    .sorted(Comparator.comparing(entry -> Integer.parseInt(entry.getKey())))
                    .map(Map.Entry::getValue)
                    .toList();
        } else if (Set.class.isAssignableFrom(objectType)) {
            return (T) fileConfiguration.getValues(false);
        } else {
            return (T) fileConfiguration.get(DATA);
        }
    }
}
