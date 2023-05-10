package com.github.oobila.bukkit.gui.cells;

import com.github.oobila.bukkit.gui.Cell;
import com.github.oobila.bukkit.gui.GuiItemStack;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class MenuItemCell extends Cell {

    private GuiItemStack itemStack;

    protected MenuItemCell(ItemStack itemStack) {
        setItemStack(itemStack);
    }

    public void setItemStack(ItemStack itemStack){
        this.itemStack = new GuiItemStack(itemStack);
    }

    public ItemStack getItemStack() {
        return itemStack.getCleanItemStack();
    }

    public void updateItemStack(ItemStack itemStack, Inventory inventory) {
        setItemStack(itemStack);
        inventory.setItem(getIndex(), itemStack);
        Bukkit.getScheduler().scheduleSyncDelayedTask(
                getPlugin(), () ->
                        inventory.getViewers().stream().forEach(humanEntity -> {
                            Player player = Bukkit.getPlayer(humanEntity.getUniqueId());
                            player.updateInventory();
                        })
                , 1);
    }

    @Override
    public ItemStack getIcon() {
        return itemStack;
    }
}