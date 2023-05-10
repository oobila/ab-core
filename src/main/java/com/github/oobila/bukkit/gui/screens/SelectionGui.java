package com.github.oobila.bukkit.gui.screens;

import com.github.oobila.bukkit.gui.Cell;
import com.github.oobila.bukkit.gui.CellCollection;
import com.github.oobila.bukkit.gui.Gui;
import com.github.oobila.bukkit.gui.cells.SelectionButtonCell;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public abstract class SelectionGui<T> extends SimpleGui {

    protected SelectionGui(Plugin plugin, Player player, String title, List<SelectionButtonCell<T>> cells) {
        super(plugin, player, title, new CellCollection(plugin, player, new ArrayList<>(cells)));
    }

    public void onSelection(InventoryClickEvent e, Player player, Cell cell, Gui gui, T object){}

}
