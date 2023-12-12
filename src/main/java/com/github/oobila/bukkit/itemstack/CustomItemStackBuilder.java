package com.github.oobila.bukkit.itemstack;

import com.github.oobila.bukkit.util.ItemMetaUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class CustomItemStackBuilder {

    private CustomItemStack customItemStack;
    private ItemMeta meta;

    public CustomItemStackBuilder(Material material) {
        customItemStack = new CustomItemStack(material);
        meta = customItemStack.getItemMeta();
    }

    public CustomItemStackBuilder(ItemStack itemStack) {
        customItemStack = new CustomItemStack(itemStack);
        meta = customItemStack.getItemMeta();
    }

    public CustomItemStackBuilder displayName(String displayName) {
        meta.setDisplayName(displayName);
        return this;
    }

    public CustomItemStackBuilder itemCount(int i) {
        customItemStack.setAmount(i);
        return this;
    }

    public CustomItemStackBuilder lore(List<String> lore) {
        meta.setLore(lore);
        return this;
    }

    public CustomItemStackBuilder secretString(NamespacedKey namespacedKey, String string) {
        ItemMetaUtil.addString(meta, namespacedKey, string);
        return this;
    }

    public CustomItemStackBuilder secretUUID(NamespacedKey namespacedKey, UUID uuid) {
        ItemMetaUtil.addUUID(meta, namespacedKey, uuid);
        return this;
    }

    public CustomItemStackBuilder secretLocalDateTime(NamespacedKey namespacedKey, LocalDateTime localDateTime) {
        ItemMetaUtil.addLocalDateTime(meta, namespacedKey, localDateTime);
        return this;
    }

    public CustomItemStack build() {
        customItemStack.setItemMeta(meta);
        return customItemStack;
    }
}
