package com.github.oobila.bukkit.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

class InventoryCloseListener implements Listener {

    @EventHandler
    public void onGuiClose(InventoryCloseEvent e){
        Player player = Bukkit.getPlayer(e.getPlayer().getUniqueId());
        GuiBase gui = GuiManager.lastOpenedGui.get(player);
        if(gui != null && gui.getTitle().equals(e.getView().getTitle())){
            GuiManager.openGuis.remove(player);
            gui.onGuiClose(player, e.getInventory(), gui);
        }
    }

}
