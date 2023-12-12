package com.github.oobila.bukkit.gui;

import com.github.oobila.bukkit.CorePlugin;
import com.github.oobila.bukkit.gui.cells.ItemCell;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

class InventoryInteractionListeners implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){

        //get player
        Player player = Bukkit.getPlayer(e.getWhoClicked().getUniqueId());

        //get gui
        Gui gui = GuiManager.lastOpenedGui.get(player);
        if(gui == null) return;
        if(!gui.getTitle().equals(e.getView().getTitle())) return;

        //check bounds of gui
        if(e.getRawSlot() < 0) return;

        //get Cell
        if (e.getRawSlot() < gui.getInventorySize()) { //in gui
            Cell cell = gui.getInventoryCell(e.getRawSlot());
            if(cell == null) {
                e.setCancelled(true);
                return;
            }

            //make on click action
            try {
                cell.onClick(e, player, cell, gui);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        //update item cells
        if (e.getRawSlot() < gui.getInventorySize() || e.isShiftClick()) {
            updateItemCells(gui, e.getView().getTopInventory());
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        //get player
        Player player = Bukkit.getPlayer(e.getWhoClicked().getUniqueId());

        //get gui
        Gui gui = GuiManager.lastOpenedGui.get(player);
        if(gui == null) return;
        if(!gui.getTitle().equals(e.getView().getTitle())) return;

        //check bounds of gui
        int firstSlot = e.getRawSlots().iterator().next();
        if(firstSlot < 0 || firstSlot >= gui.getInventorySize()) return;

        updateItemCells(gui, e.getView().getTopInventory());
    }

    private void updateItemCells(Gui gui, Inventory inventory) {
        gui.awaitingUpdate = true;
        Bukkit.getScheduler().scheduleSyncDelayedTask(CorePlugin.getInstance(), () -> {
            try {
                for (int i = 0; i < gui.getInventorySize(); i++) {
                    if (gui.getInventoryCell(i) instanceof ItemCell itemCell) {
                        itemCell.setItemStack(inventory.getItem(i));
                    }
                }
            } finally {
                gui.awaitingUpdate = false;
            }
        });
    }

}
