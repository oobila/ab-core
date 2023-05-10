package com.github.oobila.bukkit.sidecar.persistence;

import com.github.oobila.bukkit.sidecar.AnnotationUtil;
import com.github.oobila.bukkit.sidecar.Serialization;
import com.github.oobila.bukkit.sidecar.SidecarConfiguration;
import com.github.oobila.bukkit.sidecar.keyserializer.KeySerializer;
import com.github.oobila.bukkit.sidecar.resource.CustomResourcePackCluster;
import com.github.oobila.bukkit.sidecar.resource.Resource;
import com.github.oobila.bukkit.sidecar.resource.ResourcePack;
import com.github.oobila.bukkit.sidecar.resource.ResourcePackLoader;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.Plugin;
import org.reflections.Reflections;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

@SuppressWarnings("java:S3011")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DataLoader {

    private static final String IS_NOT_A_MAP = "{}::{} is not a Map";

    public static void load(Plugin plugin) {
        Reflections reflections = AnnotationUtil.getReflections(plugin);
        reflections.getFieldsAnnotatedWith(CustomResourcePackCluster.class).forEach(field ->
                loadResourceCluster(plugin, field)
        );
        reflections.getFieldsAnnotatedWith(PluginPersistentData.class).forEach(field ->
            loadData(plugin, field)
        );
        reflections.getFieldsAnnotatedWith(PluginPersistentDataCluster.class).forEach(field ->
            loadDataCluster(plugin, field)
        );
        reflections.getFieldsAnnotatedWith(PluginPersistentDataClusterNest.class).forEach(field ->
            loadDataClusterNest(plugin, field)
        );
    }

    public static void save(Plugin plugin) {
        Reflections reflections = AnnotationUtil.getReflections(plugin);
        reflections.getFieldsAnnotatedWith(PluginPersistentData.class).forEach(field ->
            saveData(plugin, field)
        );
        reflections.getFieldsAnnotatedWith(PluginPersistentDataCluster.class).forEach(field ->
            saveDataCluster(plugin, field)
        );
        reflections.getFieldsAnnotatedWith(PluginPersistentDataClusterNest.class).forEach(field ->
            saveDataClusterNest(plugin, field)
        );
    }

    static void loadData(Plugin plugin, Field field) {
        //annotation
        PluginPersistentData annotation = field.getDeclaredAnnotation(PluginPersistentData.class);
        String path = annotation.path();

        //file
        File file = new File(plugin.getDataFolder(), path);

        //load
        try {
            ParameterizedType type = (field.getGenericType() instanceof ParameterizedType parameterizedType ?
                    parameterizedType : null);
            field.set(
                    null,
                    SidecarConfiguration.load(
                            file,
                            field.getType(),
                            type == null ? null : (Class<?>) type.getActualTypeArguments()[0]
                    )
            );
        } catch (IllegalAccessException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to load: " + file.getName());
            e.printStackTrace();
        }
    }

    static <P extends ResourcePack, S> void loadResourceCluster(Plugin plugin, Field field) {
        //validation
        if (!Map.class.isAssignableFrom(field.getType())) {
            Bukkit.getLogger().log(Level.SEVERE, IS_NOT_A_MAP,
                    new String[]{field.getClass().getName(), field.getName()});
            return;
        }
        ParameterizedType type = (ParameterizedType) field.getGenericType();
        Class<S> keyType = (Class<S>) type.getActualTypeArguments()[0];
        Class<P> subTypeClass = (Class<P>) type.getActualTypeArguments()[1];

        //annotation
        CustomResourcePackCluster annotation = field.getDeclaredAnnotation(CustomResourcePackCluster.class);
        String path = annotation.path();

        //file
        File parentFile = new File(plugin.getDataFolder(), path);

        //load
        try {
            Map<S, Object> map = new HashMap<>();
            if (parentFile.exists()) {
                KeySerializer<S> keySerializer = (KeySerializer<S>) Serialization.getKeySerializers().get(keyType);
                for (File file : parentFile.listFiles()) {
                    try {
                        P resourcePack = ResourcePackLoader.loadResourcePackMetaData(
                                file,
                                subTypeClass
                        );
                        S key = keySerializer.deserialize(FilenameUtils.removeExtension(file.getName()));
                        map.put(key, resourcePack);
                    } catch (Exception e) {
                        Bukkit.getLogger().log(Level.SEVERE, "Failed to load: " + file.getName());
                        e.printStackTrace();
                    }
                }
            }

            field.set(null, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    static <T,S> void loadDataCluster(Plugin plugin, Field field) {
        //validation
        if (!Map.class.isAssignableFrom(field.getType())) {
            Bukkit.getLogger().log(Level.SEVERE, IS_NOT_A_MAP,
                    new String[]{field.getClass().getName(), field.getName()});
            return;
        }
        ParameterizedType type = (ParameterizedType) field.getGenericType();
        Class<S> keyType = (Class<S>) type.getActualTypeArguments()[0];

        Class<S> subKeyType = null;
        Class<T> subTypeClass = (Class<T>) type.getActualTypeArguments()[1];
        if (Map.class.isAssignableFrom(subTypeClass)) {
            ParameterizedType subType = (ParameterizedType) type.getActualTypeArguments()[1];
            subKeyType = (Class<S>) subType.getActualTypeArguments()[0];
        }

        //annotation
        PluginPersistentDataCluster annotation = field.getDeclaredAnnotation(PluginPersistentDataCluster.class);
        String path = annotation.path();

        //file
        File parentFile = new File(plugin.getDataFolder(), path);

        //load
        try {
            Map<S, Object> map = new HashMap<>();
            if (parentFile.exists()) {
                KeySerializer<S> keySerializer = (KeySerializer<S>) Serialization.getKeySerializers().get(keyType);
                for (File file : parentFile.listFiles()) {
                    try {
                        T data = SidecarConfiguration.load(
                                file,
                                subTypeClass,
                                subKeyType
                        );
                        S key = keySerializer.deserialize(FilenameUtils.removeExtension(file.getName()));
                        map.put(key, data);
                    } catch (Exception e) {
                        Bukkit.getLogger().log(Level.SEVERE, "Failed to load: " + file.getName());
                        e.printStackTrace();
                    }
                }
            }

            field.set(null, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    static <T,S> void loadDataClusterNest(Plugin plugin, Field field) {
        //validation
        if (!Map.class.isAssignableFrom(field.getType())) {
            Bukkit.getLogger().log(Level.SEVERE, IS_NOT_A_MAP,
                    new String[]{field.getClass().getName(), field.getName()});
            return;
        }
        ParameterizedType type = (ParameterizedType) field.getGenericType();
        Class<S> keyType = (Class<S>) type.getActualTypeArguments()[0];

        Class<?> subKeyType = null;
        Class<?> subTypeClass = (Class<?>) type.getActualTypeArguments()[1];
        if (Map.class.isAssignableFrom(subTypeClass)) {
            ParameterizedType subType = (ParameterizedType) type.getActualTypeArguments()[1];
            subKeyType = (Class<?>) subType.getActualTypeArguments()[0];
        }

        //annotation
        PluginPersistentDataClusterNest annotation = field.getDeclaredAnnotation(PluginPersistentDataClusterNest.class);
        String path = annotation.path();
        String fileName = annotation.fileName();

        //file
        File parentFile = new File(plugin.getDataFolder(), path);
        parentFile.mkdirs();

        //load
        try {
            Map<S, Object> map = new HashMap<>();
            if (parentFile.exists()) {
                KeySerializer<S> keySerializer = (KeySerializer<S>) Serialization.getKeySerializers().get(keyType);
                for (File subFolder : parentFile.listFiles()) {
                    File file = new File(subFolder, fileName);
                    if (file.exists()) {
                        try {
                            T data = (T) SidecarConfiguration.load(
                                    file,
                                    subTypeClass,
                                    subKeyType
                            );
                            S key = keySerializer.deserialize(subFolder.getName());
                            map.put(key, data);
                        } catch (Exception e) {
                            Bukkit.getLogger().log(Level.SEVERE, "Failed to load: " + fileName);
                            e.printStackTrace();
                        }
                    }
                }
            }

            field.set(null, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    static void saveData(Plugin plugin, Field field) {
        //annotation
        PluginPersistentData annotation = field.getDeclaredAnnotation(PluginPersistentData.class);
        String path = annotation.path();

        //file
        File file = new File(plugin.getDataFolder(), path);

        //save
        try {
            ParameterizedType type = (field.getGenericType() instanceof ParameterizedType parameterizedType ?
                    parameterizedType : null);
            SidecarConfiguration.save(file, field.get(null), type == null ? null : (Class<?>) type.getActualTypeArguments()[0]);
        } catch (IOException | IllegalAccessException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to save: " + path);
            e.printStackTrace();
        }
    }

    static <S> void saveDataCluster(Plugin plugin, Field field) {
        //validation
        if (!Map.class.isAssignableFrom(field.getType())) {
            Bukkit.getLogger().log(Level.SEVERE, IS_NOT_A_MAP,
                    new String[]{field.getClass().getName(), field.getName()});
            return;
        }
        ParameterizedType type = (ParameterizedType) field.getGenericType();
        Class<S> keyType = (Class<S>) type.getActualTypeArguments()[0];

        Class<?> subKeyType = null;
        Class<?> subTypeClass = (Class<?>) type.getActualTypeArguments()[1];
        if (Map.class.isAssignableFrom(subTypeClass)) {
            ParameterizedType subType = (ParameterizedType) type.getActualTypeArguments()[1];
            subKeyType = (Class<?>) subType.getActualTypeArguments()[0];
        }

        //annotation
        PluginPersistentDataCluster annotation = field.getDeclaredAnnotation(PluginPersistentDataCluster.class);
        String path = annotation.path();

        //file
        File parentFile = new File(plugin.getDataFolder(), path);


        try {
            Map<S, Object> map = (Map<S, Object>) field.get(null);
            KeySerializer<S> keySerializer = (KeySerializer<S>) Serialization.getKeySerializers().get(keyType);

            //delete
            if (parentFile.exists()) {
                for (File file : parentFile.listFiles()) {
                    if (!map.containsKey(keySerializer.deserialize(file.getName()))){
                        file.delete();
                    }
                }
            }

            //save
            for (Map.Entry<S, Object> entry : map.entrySet()) {
                String fileName = keySerializer.serialize(entry.getKey()) + ".yml";
                File file = new File(parentFile, fileName);
                try {
                    SidecarConfiguration.save(
                            file,
                            entry.getValue(),
                            subKeyType
                    );
                } catch (Exception e) {
                    Bukkit.getLogger().log(Level.SEVERE, "Failed to save: " + fileName);
                    e.printStackTrace();
                }

            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    static <S> void saveDataClusterNest(Plugin plugin, Field field) {
        //validation
        if (!Map.class.isAssignableFrom(field.getType())) {
            Bukkit.getLogger().log(Level.SEVERE, IS_NOT_A_MAP,
                    new String[]{field.getClass().getName(), field.getName()});
            return;
        }
        ParameterizedType type = (ParameterizedType) field.getGenericType();
        Class<S> keyType = (Class<S>) type.getActualTypeArguments()[0];

        Class<?> subKeyType = null;
        Class<?> subTypeClass = (Class<?>) type.getActualTypeArguments()[1];
        if (Map.class.isAssignableFrom(subTypeClass)) {
            ParameterizedType subType = (ParameterizedType) type.getActualTypeArguments()[1];
            subKeyType = (Class<?>) subType.getActualTypeArguments()[0];
        }

        //annotation
        PluginPersistentDataClusterNest annotation = field.getDeclaredAnnotation(PluginPersistentDataClusterNest.class);
        String path = annotation.path();
        String fileName = annotation.fileName();

        //file
        File parentFile = new File(plugin.getDataFolder(), path);

        try {
            Map<S, Object> map = (Map<S, Object>) field.get(null);
            KeySerializer<S> keySerializer = (KeySerializer<S>) Serialization.getKeySerializers().get(keyType);

            //delete
            if (parentFile.exists()) {
                for (File subFolder : parentFile.listFiles()) {
                    if (!map.containsKey(keySerializer.deserialize(subFolder.getName()))){
                        FileUtils.deleteDirectory(subFolder);
                    }
                }
            }

            //save
            for (Map.Entry<S, Object> entry : map.entrySet()) {
                String nestFileName = keySerializer.serialize(entry.getKey());
                File nestFile = new File(parentFile, nestFileName);
                File file = new File(nestFile, fileName);
                nestFile.mkdirs();
                try {
                    SidecarConfiguration.save(
                            file,
                            entry.getValue(),
                            subKeyType
                    );
                } catch (Exception e) {
                    Bukkit.getLogger().log(Level.SEVERE, "Failed to save: " + nestFileName);
                    e.printStackTrace();
                }
            }
        } catch (IOException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void register(Class<?> type) {
        if(ConfigurationSerializable.class.isAssignableFrom(type)) {
            ConfigurationSerialization.registerClass((Class<? extends ConfigurationSerializable>) type);
        }
        if(Resource.class.isAssignableFrom(type)) {
            com.github.oobila.bukkit.sidecar.resource.ResourcePackLoader.register(
                    (Class<? extends Resource>) type
            );
        }
    }
}
