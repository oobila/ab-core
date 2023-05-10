package com.github.oobila.bukkit.gui.cells;

import com.github.oobila.bukkit.gui.Cell;
import com.github.oobila.bukkit.gui.Gui;
import com.github.oobila.bukkit.gui.screens.SelectionGui;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class SelectionButtonCell<T> extends ButtonCell {

    @Getter
    private T returnObject;

    private boolean disableSelection;

    public SelectionButtonCell(T returnObject, ItemStack itemStack) {
        super(itemStack, null);
        this.returnObject = returnObject;
    }

    @Override
    public void onClick(InventoryClickEvent e, Player player, Cell cell, Gui gui) {
        e.setCancelled(true);
        if (disableSelection) {
            return;
        }
        SelectionGui<T> selectionGui = (SelectionGui<T>) gui;
        selectionGui.onSelection(e, player, cell, gui, returnObject);
    }

    public void disableSelection() {
        disableSelection = true;
    }
}
