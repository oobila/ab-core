package com.github.oobila.bukkit.gui;

import com.github.oobila.bukkit.gui.objects.BlockedCell;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.logging.Level;

public abstract class GuiBase {

    @Getter
    private Plugin plugin;

    @Getter
    protected String title;

    protected InventoryType inventoryType = InventoryType.PLAYER;

    @Getter
    int size;

    @Getter
    Cell[] cells;

    private ItemStack[] itemStacks;

    private boolean constructedCorrectly = true;

    @Getter @Setter
    private BlockedCell blockedCell = new BlockedCell(Material.LIGHT_GRAY_STAINED_GLASS_PANE);

    GuiBase(Plugin plugin, String title, int size, BlockedCell blockedCell) {
        if (size != 5 && (size <= 0 || size > 54 || size % 9 != 0)) {
            Bukkit.getLogger().log(Level.SEVERE, "GUI size needs to be between 9 and 54 and must be a multiple of 9");
            return;
        }
        this.plugin = plugin;
        this.size = size;
        this.title = title;
        this.blockedCell = blockedCell;
        this.cells = new Cell[size];
        this.itemStacks = new ItemStack[size];
    }

    public void setCell(int position, Cell cell) {
        baseSetCell(position, cell);
    }

    private void baseSetCell(int position, Cell cell) {
        if(cell == null) {
            cell = blockedCell;
        }
        cell.onCellAdd(plugin, this, position);
        cells[position] = cell;
        itemStacks[position] = cell.getIcon();
    }

    public void setCells(Cell[] cells){
        if(cells.length != size){
            Bukkit.getLogger().log(Level.SEVERE,
                    "cell length [{0}] does not match gui size [{1}]",
                    new Object[]{cells.length, size});
            constructedCorrectly = false;
            return;
        }
        for(int i = 0; i < cells.length; i++) {
            baseSetCell(i, cells[i]);
        }
    }

    protected void onGuiLoad(Player player, Inventory inventory, GuiBase guiBase){}

    protected void onGuiClose(Player player, Inventory inventory, GuiBase guiBase){}

    public GuiBase openGui(Player player) {

        if(!constructedCorrectly){
            Bukkit.getLogger().log(Level.SEVERE,
                    "tried to open a gui for {} however this gui was not constructed correctly",
                    player.getDisplayName());
            return null;
        }

        //create inventory
        Inventory inventory = createInventory(player);
        inventory.setContents(itemStacks);

        //listeners
        onGuiLoad(player, inventory, this);

        //tracking
        GuiManager.lastOpenedGui.put(player, this);
        GuiManager.openGuis.put(player, this);

        player.closeInventory();
        player.openInventory(inventory);
        return this;
    }

    public void reload(Player player) {
        if (GuiManager.openGuis.containsKey(player)) {
            GuiBase guiBase = GuiManager.openGuis.get(player);
            if (guiBase.equals(this)) {
                player.getOpenInventory().getTopInventory().setContents(itemStacks);
            }
        }
    }

    private Inventory createInventory(Player player){
        if(inventoryType.equals(InventoryType.PLAYER)){
            return Bukkit.createInventory(
                    player,
                    size,
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
