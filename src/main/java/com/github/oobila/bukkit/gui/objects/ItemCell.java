package com.github.oobila.bukkit.gui.objects;

import com.github.oobila.bukkit.gui.Cell;
import com.github.oobila.bukkit.gui.GuiBase;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ItemCell extends Cell {

    private ItemStack itemStack;

    public ItemCell(ItemStack itemStack) {
        setItemStack(itemStack);
    }

    public void setItemStack(ItemStack itemStack){
        this.itemStack = itemStack;
    }

    public ItemStack getActiveItemStack(Inventory inventory) {
        ItemStack activeItemStack = inventory.getItem(getPosition());
        return activeItemStack == null ? new ItemStack(Material.AIR) : activeItemStack;
    }

    @Override
    public ItemStack getIcon() {
        return itemStack;
    }

    @Override
    public void onClick(InventoryClickEvent e, Player player, Cell cell, GuiBase guiMenu) {
        //no action is needed on an item cell
    }
}
