package com.github.oobila.bukkit.itemstack;

import com.github.oobila.bukkit.CorePlugin;
import com.github.oobila.bukkit.util.ItemMetaUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.Objects;
import java.util.UUID;

public class CustomItemStack extends ItemStack {

    private static final String TYPE = "type";
    private static final String ITEM_TYPE_SEPARATOR = "::";

    private Plugin plugin;

    protected CustomItemStack(Plugin plugin, Material material){
        super(material);
        this.plugin = plugin;
    }

    protected CustomItemStack(Plugin plugin, ItemStack itemStack){
        super(itemStack.getType());
        setItemMeta(itemStack.getItemMeta());
        setAmount(itemStack.getAmount());
        setData(itemStack.getData());
        this.plugin = plugin;
    }

    protected void setDisplayName(String displayName){
        ItemMeta meta = getItemMeta();
        meta.setDisplayName(displayName);
        setItemMeta(meta);
    }

    protected void addMeta(String name, Object value){
        ItemMeta meta = getItemMeta();
        if(value instanceof String string) {
            ItemMetaUtil.addString(meta, new NamespacedKey(plugin, name), string);
        } else if(value instanceof UUID uuid){
            ItemMetaUtil.addUUID(meta, new NamespacedKey(plugin, name), uuid);
        } else {
            ItemMetaUtil.addString(meta, new NamespacedKey(plugin, name), value.toString());
        }
        setItemMeta(meta);
    }

    protected void removeMeta(String name){
        ItemMeta meta = getItemMeta();
        ItemMetaUtil.remove(meta, new NamespacedKey(plugin, name));
        setItemMeta(meta);
    }

    protected void setType(Plugin plugin, String type){
        ItemMeta meta = getItemMeta();
        ItemMetaUtil.addString(meta, new NamespacedKey(CorePlugin.getInstance(), TYPE), plugin.getName() + ITEM_TYPE_SEPARATOR + type);
        setItemMeta(meta);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CustomItemStack that = (CustomItemStack) o;
        return Objects.equals(plugin, that.plugin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), plugin);
    }
}
