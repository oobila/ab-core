package com.github.oobila.bukkit.gui.objects;

import com.github.oobila.bukkit.gui.Cell;
import com.github.oobila.bukkit.gui.GuiBase;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SimpleGuiBuilder {

    private Plugin plugin;
    private String title;
    private List<Cell> cellList = new ArrayList<>();
    private BlockedCell blockedCell = new BlockedCell();
    private GuiStateChange onLoad;
    private GuiStateChange onClose;

    public SimpleGuiBuilder(Plugin plugin) {
        this.plugin = plugin;
    }

    public SimpleGuiBuilder title(String title) {
        this.title = title;
        return this;
    }

    public SimpleGuiBuilder cell(Cell cell) {
        cellList.add(cell);
        return this;
    }

    public SimpleGuiBuilder conditionalCell(boolean condition, Cell cell) {
        if (condition) {
            cellList.add(cell);
        }
        return this;
    }

    public SimpleGuiBuilder cells(Collection<Cell> cells) {
        cellList.addAll(cells);
        return this;
    }

    public SimpleGuiBuilder blockedCell(BlockedCell blockedCell) {
        this.blockedCell = blockedCell;
        return this;
    }

    public SimpleGuiBuilder onLoad(GuiStateChange guiStateChange) {
        onLoad = guiStateChange;
        return this;
    }

    public SimpleGuiBuilder onClose(GuiStateChange guiStateChange) {
        onClose = guiStateChange;
        return this;
    }

    public SimpleGui build() {
        return new SimpleGui(plugin, title, blockedCell, cellList.toArray(new Cell[cellList.size()])) {

            @Override
            protected void onGuiLoad(Player player, Inventory inventory, GuiBase guiBase) {
                if (onLoad != null) {
                    onLoad.onChange(player, inventory, guiBase);
                }
            }

            @Override
            protected void onGuiClose(Player player, Inventory inventory, GuiBase guiBase) {
                if (onClose != null) {
                    onClose.onChange(player, inventory, guiBase);
                }
            }
        };
    }

    public interface GuiStateChange {
        void onChange(Player player, Inventory inventory, GuiBase gui);
    }
}
