package com.github.oobila.bukkit.gui.screens;

import com.github.oobila.bukkit.gui.Cell;
import com.github.oobila.bukkit.gui.cells.BlockedCell;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.function.Supplier;

public interface GuiBuilder<S,T> {

    S title(String title);
    S cell(Cell cell);
    S conditionalCell(boolean condition, Cell cell);
    S cells(Collection<Cell> cells);
    S cells(Supplier<Cell> supplier, int copies);
    S blockedCell(BlockedCell blockedCell);
    S onLoad(GuiStateChange guiStateChange);
    S onClose(GuiStateChange guiStateChange);
    T build(Player player);

}
