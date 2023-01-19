package com.github.oobila.bukkit.gui.objects;

import com.github.oobila.bukkit.gui.Cell;
import com.github.oobila.bukkit.gui.GuiBase;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;

public abstract class SelectionGui<T> extends SimpleGui {

    protected SelectionGui(Plugin plugin, String title, SelectionButtonCell<T>[] cells) {
        super(plugin, title, cells);
    }

    protected SelectionGui(Plugin plugin, String title, BlockedCell blockedCell, SelectionButtonCell<T>[] cells) {
        super(plugin, title, blockedCell, cells);
    }

    protected void onSelection(InventoryClickEvent e, Player player, Cell cell, GuiBase guiMenu, T object){}

}
