package com.github.oobila.bukkit.gui.objects;

import com.github.oobila.bukkit.gui.Cell;
import com.github.oobila.bukkit.gui.GuiBase;
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
    public void onClick(InventoryClickEvent e, Player player, Cell cell, GuiBase guiMenu) {
        if (e.getClick().isRightClick()) {
            variableActions.onUp(e, this, guiMenu);
        } else {
            variableActions.onDown(e, this, guiMenu);
        }
        e.setCancelled(true);
    }

    public interface VariableActions {
        void onUp(InventoryClickEvent e, VariableButtonCell button, GuiBase guiMenu);
        void onDown(InventoryClickEvent e, VariableButtonCell button, GuiBase guiMenu);
    }

}
