package com.github.oobila.bukkit.sidecar.persistence;

import com.github.oobila.bukkit.sidecar.AnnotationUtil;
import com.github.oobila.bukkit.sidecar.SidecarConfiguration;
import com.github.oobila.bukkit.sidecar.keyserializer.KeySerializer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.Bukkit;
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

        //load
        try {
            Map<S, Object> map = new HashMap<>();
            for (File file : parentFile.listFiles()) {
                T data = (T) SidecarConfiguration.load(
                        file,
                        subTypeClass,
                        subKeyType
                );
                S key = (S) SidecarConfiguration.getKeySerializers().get(keyType).deserialize(
                        FilenameUtils.removeExtension(file.getName())
                );
                map.put(key, data);
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
            for (File subFolder : parentFile.listFiles()) {
                File file = new File(subFolder, fileName);
                if (file.exists()) {
                    T data = (T) SidecarConfiguration.load(
                            file,
                            subTypeClass,
                            subKeyType
                    );
                    S key = (S) SidecarConfiguration.getKeySerializers().get(keyType).deserialize(
                            subFolder.getName()
                    );
                    map.put(key, data);
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

        //save
        try {
            Map<S, Object> map = (Map<S, Object>) field.get(null);
            for (Map.Entry<S, Object> entry : map.entrySet()) {
                String fileName = ((KeySerializer<S>) SidecarConfiguration.getKeySerializers().get(keyType))
                        .serialize(entry.getKey()) + ".yml";
                File file = new File(parentFile, fileName);
                SidecarConfiguration.save(
                        file,
                        entry.getValue(),
                        subKeyType
                );
            }
        } catch (IOException | IllegalAccessException e) {
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

        //save
        try {
            Map<S, Object> map = (Map<S, Object>) field.get(null);
            for (Map.Entry<S, Object> entry : map.entrySet()) {
                String nestFileName = ((KeySerializer<S>) SidecarConfiguration.getKeySerializers().get(keyType))
                        .serialize(entry.getKey());
                File nestFile = new File(parentFile, nestFileName);
                File file = new File(nestFile, fileName);
                nestFile.mkdirs();
                SidecarConfiguration.save(
                        file,
                        entry.getValue(),
                        subKeyType
                );
            }
        } catch (IOException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
