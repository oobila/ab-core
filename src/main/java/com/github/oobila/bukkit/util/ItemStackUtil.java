package com.github.oobila.bukkit.util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Utility class to help with common functions for Minecraft ItemStacks
 */
public class ItemStackUtil {

    private ItemStackUtil() {
    }

    /**
     * Force give an item to a player, if their inventory is full it will drop the item on the ground infront of them.
     * @param player
     * @param itemStack
     */
    public static void givePlayer(Player player, ItemStack itemStack){
        if(player.getInventory().firstEmpty() < 0){
            player.getWorld().dropItem(player.getLocation(), itemStack);
        } else {
            player.getInventory().addItem(itemStack);
        }
    }

    /**
     * Force give items to a player, if their inventory is full it will drop the items on the ground infront of them.
     * @param player
     * @param itemStacks
     */
    public static void givePlayer(Player player, ItemStack[] itemStacks){
        for(ItemStack itemStack : itemStacks){
            givePlayer(player, itemStack);
        }
    }

    /**
     * Adds metadata to an ItemStack to make it unstackable
     * @param itemStack
     */
    public static void makeUnstackable(ItemStack itemStack){
        ItemMeta itemMeta = itemStack.getItemMeta();
        ItemMetaUtil.makeUnstackable(itemMeta);
        itemStack.setItemMeta(itemMeta);
    }

    /**
     * Removes the metadata on an ItemStack that makes it unstackable
     * @param itemStack
     */
    public static void makeStackable(ItemStack itemStack){
        ItemMeta itemMeta = itemStack.getItemMeta();
        ItemMetaUtil.makeStackable(itemMeta);
        itemStack.setItemMeta(itemMeta);
    }
}
