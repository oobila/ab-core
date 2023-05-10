package com.github.oobila.bukkit.gui;

import com.github.oobila.bukkit.gui.cells.BlockedCell;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;

public interface CellCollectionInterface {

    Plugin getPlugin();
    int getSize();
    BlockedCell getBlockedCell();

    List<Cell> getCells();
    Cell getCell(int position);
    void setCells(List<Cell> cells);
    void setCell(int position, Cell cell);

    List<ItemStack> getItemStacks();
    ItemStack[] getItemStackArray();

}
