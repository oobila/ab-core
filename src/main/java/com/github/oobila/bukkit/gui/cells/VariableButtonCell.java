package com.github.oobila.bukkit.gui.cells;

import com.github.oobila.bukkit.gui.Cell;
import com.github.oobila.bukkit.gui.Gui;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class VariableButtonCell extends MenuItemCell {

    private VariableActions variableActions;

    public VariableButtonCell(ItemStack itemStack, VariableActions variableActions) {
        super(itemStack);
        this.variableActions = variableActions;
    }

    @Override
    public void onClick(InventoryClickEvent e, Player player, Cell cell, Gui gui) {
        if (e.getClick().isRightClick()) {
            variableActions.onUp(e, this, gui);
        } else {
            variableActions.onDown(e, this, gui);
        }
        e.setCancelled(true);
    }

    public interface VariableActions {
        void onUp(InventoryClickEvent e, VariableButtonCell button, Gui gui);
        void onDown(InventoryClickEvent e, VariableButtonCell button, Gui gui);
    }

}
