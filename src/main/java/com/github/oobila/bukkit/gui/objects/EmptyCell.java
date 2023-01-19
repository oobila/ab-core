package com.github.oobila.bukkit.gui.objects;

import com.github.oobila.bukkit.gui.Cell;
import com.github.oobila.bukkit.gui.GuiBase;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class EmptyCell extends Cell {

    private static final ItemStack AIR = new ItemStack(Material.AIR);

    @Override
    public ItemStack getIcon() {
        return AIR;
    }

    @Override
    public void onClick(InventoryClickEvent e, Player player, Cell cell, GuiBase guiMenu) {
        //no action is needed on an empty cell
    }
}
