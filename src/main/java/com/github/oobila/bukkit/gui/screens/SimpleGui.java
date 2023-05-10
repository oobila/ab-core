package com.github.oobila.bukkit.gui.screens;

import com.github.oobila.bukkit.gui.Cell;
import com.github.oobila.bukkit.gui.CellCollection;
import com.github.oobila.bukkit.gui.Gui;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.logging.Level;

public abstract class SimpleGui extends Gui {

    private int screenSize;
    private int internalSize;
    private int indexOffset;

    protected SimpleGui(Plugin plugin, Player player, String title, CellCollection cellCollection) {
        super(
                plugin,
                player,
                title == null ? "" : title,
                cellCollection
        );
        this.internalSize = cellCollection.getSize();
        this.screenSize = getScreenSize(cellCollection.getSize());

        if (internalSize > 54) {
            Bukkit.getLogger().log(Level.SEVERE, "GUI size can only go up to 54");
            return;
        }

        indexOffset = getIndexOffset();
    }

    private static int getScreenSize(int size) {
        if (size < 10) {
            return 9;
        } else if (size < 19) {
            return 18;
        } else if (size < 28) {
            return 27;
        } else if (size < 37) {
            return 36;
        } else if (size < 46) {
            return 45;
        } else {
            return 54;
        }
    }

    private int getIndexOffset() {
        if (internalSize < 9) {
            int diff = screenSize - internalSize;
            return (int) Math.ceil(diff / 2d);
        } else {
            return 0;
        }
    }

    @Override
    public Cell getInventoryCell(int position) {
        if (position >= indexOffset && (position - indexOffset) < internalSize) {
            return getCell(position - indexOffset);
        } else {
            return getBlockedCell();
        }
    }

    @Override
    public int getInventorySize() {
        return screenSize;
    }

    @Override
    public ItemStack[] getCellIcons() {
        ItemStack[] itemStacks = new ItemStack[screenSize];
        for (int i = 0; i < screenSize; i++){
            if (i < indexOffset || i >= internalSize + indexOffset) {
                itemStacks[i] = getBlockedCell().getIcon();
            } else {
                itemStacks[i] = getCell(i - indexOffset).getIcon();
            }
        }
        return itemStacks;
    }
}
