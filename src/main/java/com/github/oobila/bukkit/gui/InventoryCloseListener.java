package com.github.oobila.bukkit.gui;

import com.github.oobila.bukkit.CorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

class InventoryCloseListener implements Listener {

    @EventHandler
    public void onGuiClose(InventoryCloseEvent e){
        Player player = Bukkit.getPlayer(e.getPlayer().getUniqueId());
        Gui gui = GuiManager.lastOpenedGui.get(player);
        if(gui != null && gui.getTitle().equals(e.getView().getTitle())){
            GuiManager.openGuis.remove(player);
            handleClose(player, e.getInventory(), gui);
        }
    }

    private void handleClose(Player player, Inventory inventory, Gui gui) {
        if (gui.awaitingUpdate) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(CorePlugin.getInstance(), () ->
                    handleClose(player, inventory, gui)
            );
        } else {
            gui.onGuiClose(player, inventory, gui);
        }
    }

}
