package com.github.oobila.bukkit.gui.objects;

import com.github.oobila.bukkit.gui.Cell;
import com.github.oobila.bukkit.gui.GuiBase;
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
    public void onClick(InventoryClickEvent e, Player player, Cell cell, GuiBase guiMenu) {
        e.setCancelled(true);
        if (disableSelection) {
            return;
        }
        SelectionGui<T> gui = (SelectionGui<T>) guiMenu;
        gui.onSelection(e, player, cell, guiMenu, returnObject);
    }

    public void disableSelection() {
        disableSelection = true;
    }
}
