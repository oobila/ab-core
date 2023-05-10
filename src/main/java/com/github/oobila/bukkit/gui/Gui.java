package com.github.oobila.bukkit.gui;

import lombok.Getter;
import lombok.experimental.Delegate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;

public abstract class Gui implements GuiInterface, CellCollectionInterface {

    @Delegate(types = CellCollection.class)
    private CellCollection cellCollection;

    @Getter
    private Plugin plugin;

    @Getter
    private Player player;

    @Getter
    protected String title;

    protected InventoryType inventoryType = InventoryType.PLAYER;

    protected Gui(Plugin plugin, Player player, String title, CellCollection cellCollection) {
        this.plugin = plugin;
        this.player = player;
        this.title = title;
        this.cellCollection = cellCollection;
    }

    protected void onGuiLoad(Player player, Inventory inventory, Gui gui){}

    protected void onGuiClose(Player player, Inventory inventory, Gui gui){}

    public Gui openGui() {
        //create inventory
        Inventory inventory = createInventory();

        //listeners
        onGuiLoad(player, inventory, this);

        //set contents
        inventory.setContents(getCellIcons());

        //tracking
        GuiManager.lastOpenedGui.put(player, this);
        GuiManager.openGuis.put(player, this);

        player.closeInventory();
        player.openInventory(inventory);
        return this;
    }

    public void reload() {
        if (GuiManager.openGuis.containsKey(player)) {
            Gui guiBase = GuiManager.openGuis.get(player);
            if (guiBase.equals(this)) {
                player.getOpenInventory().getTopInventory().setContents(getCellIcons());
            }
        }
    }

    protected Inventory createInventory(){
        if(inventoryType.equals(InventoryType.PLAYER)){
            return Bukkit.createInventory(
                    player,
                    getInventorySize(),
                    getTitle()
            );
        } else {
            return Bukkit.createInventory(
                    player,
                    inventoryType,
                    getTitle()
            );
        }
    }
}
