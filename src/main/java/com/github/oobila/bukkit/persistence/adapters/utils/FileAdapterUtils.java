package com.github.oobila.bukkit.persistence.adapters.utils;

import com.github.oobila.bukkit.persistence.caches.BaseCache;
import com.github.oobila.bukkit.persistence.caches.ConfigCache;
import com.github.oobila.bukkit.persistence.serializers.Serialization;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.logging.Level;

public class FileAdapterUtils {

    public static <V, K> void copyDefaults(BaseCache<K,V> cache, File file){
        file.getParentFile().mkdirs();
        try (InputStream inputStream = cache.getPlugin().getResource(getSimpleFileName(cache));
             OutputStream outputStream = new FileOutputStream(file)) {
            if (inputStream != null) {
                Bukkit.getLogger().log(Level.INFO, "Copying defaults for: " + cache.getName());
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

    public static <V, K> File getSaveFile(BaseCache<K,V> cache, OfflinePlayer player) {
        File fileLocation = cache.getPlugin().getDataFolder();

        //append data prefix
        if (!(cache instanceof ConfigCache<?,?>)) {
            fileLocation = new File(fileLocation, "data");
            try {
                FileUtils.forceMkdir(fileLocation);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //append player prefix
            if (player != null) {
                fileLocation = new File(fileLocation, Serialization.serialize(player));
                try {
                    FileUtils.forceMkdir(fileLocation);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return new File(fileLocation, getSimpleFileName(cache));
    }

    private static <V, K> String getSimpleFileName(BaseCache<K,V> cache) {
        return cache.getName() + ".yml";
    }
}
