package com.github.oobila.bukkit.gui;

import lombok.Getter;
import org.bukkit.plugin.Plugin;

import java.awt.font.LineMetrics;

@Getter
public abstract class Cell implements GuiCell {

    @Getter
    private Plugin plugin;

    @Getter
    private CellCollection cellCollection;

    @Getter
    private int index;

    @Getter
    private int inventoryPosition;

    public void onCellAdd(Plugin plugin, CellCollection cellCollection, int position) {
        this.plugin = plugin;
        this.cellCollection = cellCollection;
        this.index = position;
    }

    public void onBind(int inventoryPosition) {
        this.inventoryPosition = inventoryPosition;
    }

    public void replace(Cell cell) {
        cellCollection.setCell(index, cell);
    }

}