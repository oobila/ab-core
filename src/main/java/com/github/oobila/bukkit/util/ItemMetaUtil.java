package com.github.oobila.bukkit.util;

import com.github.oobila.bukkit.CorePlugin;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMetaUtil {

    private static final String UNSTACKABLE_KEY = "unstackable";
    private static final String LIST_DELIMITER = "#|#";

    public static void remove(ItemMeta itemMeta, NamespacedKey key){
        itemMeta.getPersistentDataContainer().remove(key);
    }

    public static void addString(ItemMeta itemMeta, NamespacedKey key, String value){
        itemMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);
    }

    public static String getString(ItemMeta itemMeta, NamespacedKey key){
        if(itemMeta == null) {
            return null;
        }
        return itemMeta.getPersistentDataContainer().get(key,PersistentDataType.STRING);
    }

    public static void addInt(ItemMeta itemMeta, NamespacedKey key, int value){
        itemMeta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, value);
    }

    public static int getInt(ItemMeta itemMeta, NamespacedKey key){
        if(itemMeta == null) {
            return 0;
        }
        Integer i = itemMeta.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
        return i == null ? 0 : i;
    }

    public static void addDouble(ItemMeta itemMeta, NamespacedKey key, double value){
        itemMeta.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, value);
    }

    public static double getDouble(ItemMeta itemMeta, NamespacedKey key){
        if(itemMeta == null) {
            return 0;
        }
        Double d = itemMeta.getPersistentDataContainer().get(key, PersistentDataType.DOUBLE);
        return d == null ? 0 : d;
    }

    public static void addLocalDateTime(ItemMeta itemMeta, NamespacedKey key, LocalDateTime localDateTime){
        itemMeta.getPersistentDataContainer().set(
                key,
                PersistentDataType.LONG,
                localDateTime.toEpochSecond(ZoneOffset.UTC)
        );
    }

    public static LocalDateTime getLocalDateTime(ItemMeta itemMeta, NamespacedKey key){
        if(itemMeta == null) {
            return null;
        }
        Long epochMillis = itemMeta.getPersistentDataContainer().get(key,PersistentDataType.LONG);
        return epochMillis == null || epochMillis <= 0 ?
                null :
                LocalDateTime.ofEpochSecond(epochMillis, 0, ZoneOffset.UTC);
    }

    public static void addUUID(ItemMeta itemMeta, NamespacedKey key, UUID value){
        itemMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, value.toString());
    }

    public static UUID getUUID(ItemMeta itemMeta, NamespacedKey key){
        if(itemMeta == null) {
            return null;
        }
        String value = itemMeta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
        return value == null ? null : UUID.fromString(value);
    }

    public static void addList(ItemMeta itemMeta, NamespacedKey key, List<String> value) {
        itemMeta.getPersistentDataContainer().set(
                key,
                PersistentDataType.STRING,
                value.stream().collect(Collectors.joining(LIST_DELIMITER))
        );
    }

    public static List<String> getList(ItemMeta itemMeta, NamespacedKey key){
        if(itemMeta == null) {
            return null;
        }
        String value = itemMeta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
        return value == null ? null : Arrays.stream(value.split(LIST_DELIMITER)).toList();
    }

    public static void makeUnstackable(ItemMeta itemMeta){
        ItemMetaUtil.addUUID(itemMeta, new NamespacedKey(CorePlugin.getInstance(), UNSTACKABLE_KEY), UUID.randomUUID());
    }

    public static void makeStackable(ItemMeta itemMeta){
        ItemMetaUtil.remove(itemMeta, new NamespacedKey(CorePlugin.getInstance(), UNSTACKABLE_KEY));
    }
}
