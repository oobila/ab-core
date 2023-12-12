package com.github.oobila.bukkit.gui.screens;

import com.github.oobila.bukkit.gui.Cell;
import com.github.oobila.bukkit.gui.CellCollection;
import com.github.oobila.bukkit.gui.Gui;
import com.github.oobila.bukkit.gui.cells.BlockedCell;
import com.github.oobila.bukkit.util.enums.BlockColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MultiPageGuiBuilder implements GuiBuilder<MultiPageGuiBuilder, MultiPageGui> {

    private Plugin plugin;
    private String title;
    private List<Cell> cellList = new ArrayList<>();
    private BlockedCell blockedCell = new BlockedCell();
    private GuiStateChange onLoad;
    private GuiStateChange onClose;
    private BlockColor blockColor;

    public MultiPageGuiBuilder(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public MultiPageGuiBuilder title(String title) {
        this.title = title;
        return this;
    }

    @Override
    public MultiPageGuiBuilder cell(Cell cell) {
        cellList.add(cell);
        return this;
    }

    @Override
    public MultiPageGuiBuilder conditionalCell(boolean condition, Cell cell) {
        if (condition) {
            cellList.add(cell);
        }
        return this;
    }

    @Override
    public MultiPageGuiBuilder cells(Collection<Cell> cells) {
        cellList.addAll(cells);
        return this;
    }

    @Override
    public MultiPageGuiBuilder cells(Supplier<Cell> supplier, int copies) {
        cellList.addAll(Stream.generate(() -> supplier.get())
                .limit(copies)
                .collect(Collectors.toList()));
        return this;
    }

    @Override
    public MultiPageGuiBuilder blockedCell(BlockedCell blockedCell) {
        this.blockedCell = blockedCell;
        return this;
    }

    @Override
    public MultiPageGuiBuilder onLoad(GuiStateChange guiStateChange) {
        onLoad = guiStateChange;
        return this;
    }

    @Override
    public MultiPageGuiBuilder onClose(GuiStateChange guiStateChange) {
        onClose = guiStateChange;
        return this;
    }

    public MultiPageGuiBuilder color(BlockColor color) {
        this.blockColor = color;
        return this;
    }

    @Override
    public MultiPageGui build(Player player) {
        CellCollection cellCollection = new CellCollection(plugin, player, cellList);
        cellCollection.setBlockedCell(blockedCell);
        return new MultiPageGui(plugin, player, title, cellCollection,
                blockColor == null ? BlockColor.WHITE : blockColor) {
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
