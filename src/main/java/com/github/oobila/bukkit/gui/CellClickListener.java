package com.github.oobila.bukkit.gui;

import com.github.oobila.bukkit.gui.objects.BlockedCell;
import com.github.oobila.bukkit.gui.objects.ButtonCell;
import com.github.oobila.bukkit.gui.objects.ToggleButtonCell;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

class CellClickListener implements Listener {

    @EventHandler
    public void onSelect(InventoryClickEvent e){
        //get player
        Player player = Bukkit.getPlayer(e.getWhoClicked().getUniqueId());

        //get gui
        GuiBase guiMenu = GuiManager.lastOpenedGui.get(player);
        if(guiMenu == null) return;
        if(!guiMenu.getTitle().equals(e.getView().getTitle())) return;

        //check bounds of gui
        if(e.getRawSlot() < 0 || e.getRawSlot() >= guiMenu.size) return;

        //get Cell
        Cell cell = guiMenu.cells[e.getRawSlot()];
        if(cell == null) {
            e.setCancelled(true);
            return;
        }

        if(cell instanceof ButtonCell || cell instanceof ToggleButtonCell){
            try {
                cell.onClick(e, player, cell, guiMenu);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if(cell instanceof BlockedCell){
            e.setCancelled(true);
        }
    }

}
