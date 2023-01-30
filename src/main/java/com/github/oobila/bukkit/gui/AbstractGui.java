package com.github.oobila.bukkit.gui;

import com.github.oobila.bukkit.gui.objects.BlockedCell;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public abstract class AbstractGui extends GuiBase {

    private int internalSize;
    private int indexOffset;

    protected AbstractGui(Plugin plugin, String title, Cell[] cells) {
        this(plugin, title, null, cells);
    }

    protected AbstractGui(Plugin plugin, String title, BlockedCell blockedCell, Cell[] cells) {
        super(
                plugin,
                title == null ? "" : title,
                getPhysicalSize(cells.length),
                blockedCell == null ? new BlockedCell() : blockedCell
        );

        this.internalSize = cells.length;
        indexOffset = getIndexOffset(this.size, internalSize);
        setInventoryType();
        setCells(cells);
    }

    private int getIndexOffset(int size, int internalSize){
        if(internalSize < 9) {
            int diff = size - internalSize;
            return (int) Math.ceil(diff / 2d);
        } else {
            return 0;
        }
    }

    private void setInventoryType(){
        if(size < 6){
            inventoryType = InventoryType.HOPPER;
        } else {
            inventoryType = InventoryType.PLAYER;
        }
    }

    @Override
    public void setCell(int position, Cell cell) {
        super.setCell(position + indexOffset, cell);
    }

    public void setAbsoluteCell(int position, Cell cell) {
        super.setCell(position, cell);
    }

    @Override
    public void setCells(Cell[] cells) {
        Cell[] allCells = new Cell[size];
        for(int i = 0; i < size; i++){
            if(i < indexOffset || i - indexOffset >= internalSize) {
                allCells[i] = getBlockedCell();
            } else {
                allCells[i] = cells[i - indexOffset];
            }
        }
        super.setCells(allCells);
    }

    public void setAbsoluteCells(Cell[] cells) {
        super.setCells(cells);
    }

    @Override
    public Cell[] getCells() {
        Cell[] allCells = getAbsoluteCells();
        Cell[] flexibleCells = new Cell[internalSize];
        for(int i = 0; i < internalSize; i++){
            flexibleCells[i] = allCells[i + indexOffset];
        }
        return flexibleCells;
    }

    public Cell[] getAbsoluteCells() {
        return super.getCells();
    }

    public ItemStack[] getItems(Inventory inventory) {
        ItemStack[] items = new ItemStack[internalSize];
        ItemStack[] inventoryContents = inventory.getContents();
        for(int i = 0; i < internalSize; i++){
            items[i] = inventoryContents[i + indexOffset];
        }
        return items;
    }

    @Override
    public int getSize() {
        return internalSize;
    }

    private static int getPhysicalSize(int size){
        if (size < 6) { return 5; //Hopper inventory
        } else if (size < 10) { return 9;
        } else if (size < 19) { return 18;
        } else if (size < 28) { return 27;
        } else if (size < 37) { return 36;
        } else if (size < 46) { return 45;
        } else { return 54;
        }
    }

    public ItemStack[] getItemStacks(Inventory inventory) {
        if (inventory.getSize() != getAbsoluteCells().length) {
            throw new IllegalArgumentException("Inventory must be the same size of the gui");
        }
        ItemStack[] ret = new ItemStack[internalSize];
        for(int i = 0; i < internalSize; i++){
            ret[i] = inventory.getItem(i + indexOffset);
        }
        return ret;
    }
}
