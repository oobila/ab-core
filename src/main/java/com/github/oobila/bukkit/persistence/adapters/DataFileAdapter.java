package com.github.oobila.bukkit.persistence.adapters;

import com.github.oobila.bukkit.persistence.adapters.utils.FileAdapterUtils;
import com.github.oobila.bukkit.persistence.caches.BaseCache;
import com.github.oobila.bukkit.persistence.caches.DataCache;
import com.github.oobila.bukkit.persistence.model.PersistedObject;
import com.github.oobila.bukkit.persistence.serializers.Serialization;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public class DataFileAdapter<K, V extends PersistedObject> implements DataCacheAdapter<K, V> {

    private final Map<K, V> localCache = new HashMap<>();

    @Override
    public void open(BaseCache<K, V> cache) {
        File saveFile = FileAdapterUtils.getSaveFile(cache, null);
        if (!saveFile.exists()) {
            Bukkit.getLogger().warning("savefile does not exist");
            FileAdapterUtils.copyDefaults(cache, saveFile);
        }
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(saveFile);
        fileConfiguration.getValues(false).entrySet().forEach(entry ->{
            K key = Serialization.deserialize(cache.getKeyType(), entry.getKey());
            V value = (V) entry.getValue();
            localCache.put(key, value);
        });
    }

    @Override
    public void close(BaseCache<K, V> cache) {
        File saveFile = FileAdapterUtils.getSaveFile(cache, null);
        if (saveFile.exists()) {
            saveFile.delete();
        }
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(saveFile);
        localCache.entrySet().forEach(entry -> {
            String key = Serialization.serialize(entry.getKey());
            fileConfiguration.set(key, entry.getValue());
        });
        try {
            fileConfiguration.save(saveFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void put(K key, V value, BaseCache<K, V> dataCache) {
        localCache.put(key, value);
    }

    @Override
    public V get(K key, BaseCache<K, V> dataCache) {
        return localCache.get(key);
    }

    @Override
    public void remove(K key, BaseCache<K, V> dataCache) {
        localCache.remove(key);
    }

    @Override
    public int removeBefore(ZonedDateTime zonedDateTime, BaseCache<K, V> dataCache) {
        Set<K> keysToRemove = new HashSet<>();
        localCache.forEach((k, v) -> {
            if (v.getCreatedDate().isBefore(zonedDateTime)) {
                keysToRemove.add(k);
            }
        });
        keysToRemove.forEach(k -> this.remove(k, dataCache));
        return 0;
    }

    public void forEach(BiConsumer<K,V> action) {
        localCache.forEach(action);
    }

    public void putIfAbsent(K key, V value) {
        localCache.putIfAbsent(key, value);
    }
}
