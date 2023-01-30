package com.github.oobila.bukkit.gui.objects;

import com.github.oobila.bukkit.gui.Cell;
import com.github.oobila.bukkit.gui.GuiBase;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SelectionGuiBuilder<T> {

    private Plugin plugin;
    private String title;
    private List<SelectionButtonCell<T>> cellList = new ArrayList<>();
    private BlockedCell blockedCell = new BlockedCell();
    private Runnable onLoad;
    private Runnable onClose;

    private Collection<T> collection;
    private CellBuilderInstruction<T> cellBuilderInstruction;
    private SelectionAction<T> selectionAction;

    public SelectionGuiBuilder(Plugin plugin) {
        this.plugin = plugin;
    }

    public SelectionGuiBuilder<T> title(String title) {
        this.title = title;
        return this;
    }

    public SelectionGuiBuilder<T> cell(SelectionButtonCell<T> cell) {
        cellList.add(cell);
        return this;
    }

    public SelectionGuiBuilder<T> cells(Collection<SelectionButtonCell<T>> cells) {
        cellList.addAll(cells);
        return this;
    }

    public SelectionGuiBuilder<T> collection(Collection<T> collection) {
        this.collection = collection;
        return this;
    }

    public SelectionGuiBuilder<T> buttonBuilder(CellBuilderInstruction<T> cellBuilderInstruction) {
        this.cellBuilderInstruction = cellBuilderInstruction;
        return this;
    }

    public SelectionGuiBuilder<T> blockedCell(BlockedCell blockedCell) {
        this.blockedCell = blockedCell;
        return this;
    }

    public SelectionGuiBuilder<T> onLoad(Runnable runnable) {
        onLoad = runnable;
        return this;
    }

    public SelectionGuiBuilder<T> onClose(Runnable runnable) {
        onClose = runnable;
        return this;
    }

    public SelectionGuiBuilder<T> onSelection(SelectionAction<T> selectionAction) {
        this.selectionAction = selectionAction;
        return this;
    }

    public SelectionGui<T> build() {
        cellList.addAll(collection.stream().map(t -> cellBuilderInstruction.build(t)).toList());

        return new SelectionGui<T>(plugin, title, blockedCell, cellList.toArray(new SelectionButtonCell[cellList.size()])) {
            @Override
            protected void onGuiLoad(Player player, Inventory inventory, GuiBase guiBase) {
                if (onLoad != null) {
                    onLoad.run();
                }
            }

            @Override
            protected void onGuiClose(Player player, Inventory inventory, GuiBase guiBase) {
                if (onClose != null) {
                    onClose.run();
                }
            }

            @Override
            protected void onSelection(InventoryClickEvent e, Player player, Cell cell, GuiBase guiMenu, T object) {
                selectionAction.onSelection(e, player, cell, guiMenu, object);
            }
        };
    }

    public interface CellBuilderInstruction<T> {
        SelectionButtonCell<T> build(T object);
    }

    public interface SelectionAction<T> {
        void onSelection(InventoryClickEvent e, Player player, Cell cell, GuiBase guiMenu, T object);
    }
}
