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

public class EndlessGuiBuilder implements GuiBuilder<EndlessGuiBuilder, EndlessGui> {

    private Plugin plugin;
    private String title;
    private List<Cell> cellList = new ArrayList<>();
    private BlockedCell blockedCell = new BlockedCell();
    private GuiStateChange onLoad;
    private GuiStateChange onClose;
    private BlockColor blockColor;

    public EndlessGuiBuilder(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public EndlessGuiBuilder title(String title) {
        this.title = title;
        return this;
    }

    @Override
    public EndlessGuiBuilder cell(Cell cell) {
        cellList.add(cell);
        return this;
    }

    @Override
    public EndlessGuiBuilder conditionalCell(boolean condition, Cell cell) {
        if (condition) {
            cellList.add(cell);
        }
        return this;
    }

    @Override
    public EndlessGuiBuilder cells(Collection<Cell> cells) {
        cellList.addAll(cells);
        return this;
    }

    @Override
    public EndlessGuiBuilder cells(Supplier<Cell> supplier, int copies) {
        cellList.addAll(Stream.generate(() -> supplier.get())
                .limit(copies)
                .collect(Collectors.toList()));
        return this;
    }

    @Override
    public EndlessGuiBuilder blockedCell(BlockedCell blockedCell) {
        this.blockedCell = blockedCell;
        return this;
    }

    @Override
    public EndlessGuiBuilder onLoad(GuiStateChange guiStateChange) {
        onLoad = guiStateChange;
        return this;
    }

    @Override
    public EndlessGuiBuilder onClose(GuiStateChange guiStateChange) {
        onClose = guiStateChange;
        return this;
    }

    public EndlessGuiBuilder color(BlockColor color) {
        this.blockColor = color;
        return this;
    }

    @Override
    public EndlessGui build(Player player) {
        CellCollection cellCollection = new CellCollection(plugin, player, cellList);
        cellCollection.setBlockedCell(blockedCell);
        return new EndlessGui(plugin, player, title, cellCollection,
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
