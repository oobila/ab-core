package com.github.oobila.bukkit.gui.screens;

import com.github.oobila.bukkit.gui.Cell;
import com.github.oobila.bukkit.gui.CellCollection;
import com.github.oobila.bukkit.gui.Gui;
import com.github.oobila.bukkit.gui.cells.BlockedCell;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimpleGuiBuilder implements GuiBuilder<SimpleGuiBuilder,SimpleGui> {

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

    public SimpleGuiBuilder cells(Supplier<Cell> supplier, int copies) {
        cellList.addAll(Stream.generate(() -> supplier.get())
                .limit(copies)
                .collect(Collectors.toList()));
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

    public SimpleGui build(Player player) {
        CellCollection cellCollection = new CellCollection(plugin, player, cellList);
        cellCollection.setBlockedCell(blockedCell);
        return new SimpleGui(plugin, player, title, cellCollection) {
            @Override
            protected void onGuiLoad(Player player, Inventory inventory, Gui guiBase) {
                if (onLoad != null) {
                    onLoad.onChange(player, inventory, guiBase);
                }
            }

            @Override
            protected void onGuiClose(Player player, Inventory inventory, Gui guiBase) {
                if (onClose != null) {
                    onClose.onChange(player, inventory, guiBase);
                }
            }
        };
    }

}
