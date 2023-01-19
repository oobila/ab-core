package com.github.oobila.bukkit.util;

import com.github.oobila.bukkit.Constants;
import com.github.oobila.bukkit.CorePlugin;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class ItemMetaUtil {

    private ItemMetaUtil() {
    }

    /**
     * Removes an item from the persistent container
     * @param itemMeta
     * @param key
     */
    public static void remove(ItemMeta itemMeta, NamespacedKey key){
        itemMeta.getPersistentDataContainer().remove(key);
    }

    /**
     * Adds a string to the persistent container
     * @param itemMeta
     * @param key
     * @param value
     */
    public static void addString(ItemMeta itemMeta, NamespacedKey key, String value){
        itemMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);
    }

    /**
     * Gets a string from the persistent container
     * @param itemMeta
     * @param key
     * @return
     */
    public static String getString(ItemMeta itemMeta, NamespacedKey key){
        if(itemMeta == null)
            return null;
        return itemMeta.getPersistentDataContainer().get(key,PersistentDataType.STRING);
    }

    /**
     * Adds an integer to the persistent container
     * @param itemMeta
     * @param key
     * @param value
     */
    public static void addInt(ItemMeta itemMeta, NamespacedKey key, int value){
        itemMeta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, value);
    }

    /**
     * Gets an integer from the persistent container
     * @param itemMeta
     * @param key
     * @return
     */
    public static int getInt(ItemMeta itemMeta, NamespacedKey key){
        if(itemMeta == null)
            return 0;
        return itemMeta.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
    }

    /**
     * Adds a Calendar to the persistent container
     * @param itemMeta
     * @param key
     * @param localDateTime
     */
    public static void addLocalDateTime(ItemMeta itemMeta, NamespacedKey key, LocalDateTime localDateTime){
        itemMeta.getPersistentDataContainer().set(
                key,
                PersistentDataType.LONG,
                localDateTime.toEpochSecond(ZoneOffset.UTC)
        );
    }

    /**
     * Gets a Calendar from the persistent container
     * @param itemMeta
     * @param key
     * @return
     */
    public static LocalDateTime getLocalDateTime(ItemMeta itemMeta, NamespacedKey key){
        if(itemMeta == null) {
            return null;
        }
        long epochMillis = itemMeta.getPersistentDataContainer().get(key,PersistentDataType.LONG);
        LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(epochMillis, 0, ZoneOffset.UTC);
        return epochMillis <= 0 ? null : localDateTime;
    }

    /**
     * Adds a UUID to the persistent container
     * @param itemMeta
     * @param key
     * @param value
     */
    public static void addUUID(ItemMeta itemMeta, NamespacedKey key, UUID value){
        itemMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, value.toString());
    }

    /**
     * Gets a UUID from the persistent container
     * @param itemMeta
     * @param key
     * @return
     */
    public static UUID getUUID(ItemMeta itemMeta, NamespacedKey key){
        if(itemMeta == null)
            return null;
        String value = itemMeta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
        return value == null ? null : UUID.fromString(value);
    }

    /**
     * Adds metadata to itemMeta to make it unstackable
     * @param itemMeta
     */
    public static void makeUnstackable(ItemMeta itemMeta){
        ItemMetaUtil.addUUID(itemMeta, new NamespacedKey(CorePlugin.getInstance(), Constants.UNSTACKABLE_KEY), UUID.randomUUID());
    }

    /**
     * Removes the metadata on itemMeta that makes it unstackable
     * @param itemMeta
     */
    public static void makeStackable(ItemMeta itemMeta){
        ItemMetaUtil.remove(itemMeta, new NamespacedKey(CorePlugin.getInstance(), Constants.UNSTACKABLE_KEY));
    }
}
