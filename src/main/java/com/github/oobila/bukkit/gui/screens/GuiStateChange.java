package com.github.oobila.bukkit.gui.screens;

import com.github.oobila.bukkit.gui.Gui;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface GuiStateChange {
    void onChange(Player player, Inventory inventory, Gui gui);
}
