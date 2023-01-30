package com.github.oobila.bukkit.sidecar;

import com.github.oobila.bukkit.sidecar.keyserializer.KeySerializer;
import com.github.oobila.bukkit.sidecar.keyserializer.OfflinePlayerSerializer;
import com.github.oobila.bukkit.sidecar.keyserializer.StringSerializer;
import com.github.oobila.bukkit.sidecar.keyserializer.UUIDSerializer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
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
    }

    /**
     * Loads a data file and returns the fileConfiguration
     * @param plugin
     * @param path
     */
    public static FileConfiguration loadDataFile(Plugin plugin, String path){
        return YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), path));
    }

    /**
     * Saves a data file from the fileConfiguration
     * @param plugin
     * @param path
     */
    public static void saveDataFile(Plugin plugin, String path, FileConfiguration fileConfiguration) throws IOException {
        File file = new File(plugin.getDataFolder(), path);
        try {
            if(fileConfiguration != null){
                fileConfiguration.save(file);
            }
        } catch (IOException e) {
            throw new IOException(MessageFormat.format("Could not save config file to {0}", file.getName()));
        }
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

    /**
     * Retrieves an object from the fileConfiguration
     * @param configuration
     * @param objectType
     * @param field
     * @return
     */
    public static <T> T getDataObject(FileConfiguration configuration, Class<T> objectType, Field field) {
        return getDataObject(configuration, objectType, field, "data");
    }

    /**
     * Retrieves an object from the fileConfiguration
     * @param configuration
     * @param objectType
     * @param field
     * @return
     */
    public static <S, T> T getDataObject(
            FileConfiguration configuration, Class<T> objectType, Field field, String dataName) {
        //getting value
        if ((configuration.get(dataName) != null) || (configuration.getConfigurationSection(dataName) != null)) {
            if (Map.class.isAssignableFrom(objectType)) {
                Class<S> keyType = (Class<S>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                return getDataObject(configuration, objectType, keyType);
            } else {
                return getDataObject(configuration, objectType);
            }
        }
        return null;
    }

    /**
     * Retrieves an object from the fileConfiguration
     * @param configuration
     * @param objectType
     * @param keyType
     * @return
     */
    public static <S, T> T getDataObject(FileConfiguration configuration, Class<T> objectType, Class<S> keyType) {
        return getDataObject(configuration, objectType, keyType, "data");
    }

    /**
     * Retrieves an object from the fileConfiguration
     * @param configuration
     * @param objectType
     * @param keyType
     * @return
     */
    public static <S, T> T getDataObject(
            FileConfiguration configuration, Class<T> objectType, Class<S> keyType, String dataName) {
        //getting value
        if ((configuration.get(dataName) != null) || (configuration.getConfigurationSection(dataName) != null)) {
            if (Map.class.isAssignableFrom(objectType)) {
                KeySerializer<S> keySerializer = getKeySerializer(keyType);
                LinkedHashMap<S, Object> map = new LinkedHashMap<>();
                configuration.getConfigurationSection(dataName).getKeys(false).forEach(k ->
                        map.put(keySerializer.deserialize(k), configuration.get(dataName + "." + k))
                );
                return (T) map;
            } else {
                return getDataObject(configuration, objectType);
            }
        }
        return null;
    }

    /**
     * Retrieves an object from the fileConfiguration
     * @param configuration
     * @param objectType
     * @return
     */
    public static <T> T getDataObject(FileConfiguration configuration, Class<T> objectType) {
        return getDataObject(configuration, objectType, "data");
    }

    /**
     * Retrieves an object from the fileConfiguration
     * @param configuration
     * @param objectType
     * @return
     */
    public static <T> T getDataObject(FileConfiguration configuration, Class<T> objectType, String dataName) {
        if ((configuration.get(dataName) != null) || (configuration.getConfigurationSection(dataName) != null)) {
            if (List.class.isAssignableFrom(objectType)) {
                return (T) configuration.getList(dataName);
            } else if (Set.class.isAssignableFrom(objectType)) {
                return (T) new HashSet<>(configuration.getList(dataName));
            } else if (!Map.class.isAssignableFrom(objectType)) {
                return (T) configuration.get(dataName);
            } else {
                throw new IllegalArgumentException("object type cannot be a Map, please use one of the overloaded methods");
            }
        }
        return null;
    }

    /**
     * Stores an object to the fileConfiguration
     * @param configuration
     * @return
     */
    public static <T> void setDataObject(T object, FileConfiguration configuration) {
        setDataObject(object, configuration, "data");
    }

    /**
     * Stores an object to the fileConfiguration
     * @param configuration
     * @return
     */
    public static <T, S> void setDataObject(T object, FileConfiguration configuration, String dataName) {
        if (object != null) {
            if (object instanceof Map) {
                configuration.set(dataName, null);
                Map<S, Object> map = (Map<S, Object>) object;
                if (map.size() > 0) {
                    S firstMapKey = map.keySet().iterator().next();
                    KeySerializer<S> keySerializer = (KeySerializer<S>) getKeySerializer(firstMapKey.getClass());
                    map.forEach((k, v) -> configuration.set(dataName + "." + keySerializer.serialize(k), v));
                }
            } else if (object instanceof List) {
                List<Object> list = (List<Object>) object;
                configuration.set(dataName, list.toArray());
            } else if (object instanceof Set) {
                Set<Object> set = (Set<Object>) object;
                configuration.set(dataName, set.toArray());
            } else {
                configuration.set(dataName, object);
            }
        }
    }

    public static void deserializeField(Callable<?> callable, Field field) {
        trySerialize(callable, field, "deserialize");
    }

    public static void serializeField(Callable<?> callable, Field field) {
        trySerialize(callable, field, "serialize");
    }

    private static void trySerialize(Callable<?> serializationMethod, Field field, String operationName) {
        try {
            serializationMethod.call();
        } catch (YAMLException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not {} {}::{}",
                    new String[]{operationName, field.getDeclaringClass().getName(), field.getName()});
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
        }
    }

}
