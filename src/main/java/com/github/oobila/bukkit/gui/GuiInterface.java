package com.github.oobila.bukkit.gui;

import org.bukkit.inventory.ItemStack;

public interface GuiInterface {

    ItemStack[] getCellIcons();
    int getInventorySize();
    Cell getInventoryCell(int position);

}
