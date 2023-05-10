package com.github.oobila.bukkit.sidecar;

import com.github.oobila.bukkit.sidecar.keyserializer.KeySerializer;
import com.github.oobila.bukkit.sidecar.keyserializer.LocationSerializer;
import com.github.oobila.bukkit.sidecar.keyserializer.OfflinePlayerSerializer;
import com.github.oobila.bukkit.sidecar.keyserializer.StringSerializer;
import com.github.oobila.bukkit.sidecar.keyserializer.UUIDSerializer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.logging.Level;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Serialization {

    @Getter
    private static final Map<Class<?>, KeySerializer<?>> keySerializers = new HashMap<>();
    static {
        keySerializers.put(String.class, new StringSerializer());
        keySerializers.put(UUID.class, new UUIDSerializer());
        keySerializers.put(OfflinePlayer.class, new OfflinePlayerSerializer());
        keySerializers.put(Location.class, new LocationSerializer());
    }

    /**
     * Handles and returns the key serializer for a specific class type
     * @param type
     * @param <T>
     * @return
     */
    public static <T> KeySerializer<T> getKeySerializer(Class<T> type) {
        for (Map.Entry<Class<?>, KeySerializer<?>> entry : Serialization.getKeySerializers().entrySet()) {
            if (entry.getKey().isAssignableFrom(type)) {
                return (KeySerializer<T>) entry.getValue();
            }
        }
        throw new NullPointerException("There is no key serializer for type: " + type.getName());
    }

}
