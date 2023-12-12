package com.github.oobila.bukkit.gui.screens;

import com.github.oobila.bukkit.gui.Cell;
import com.github.oobila.bukkit.gui.CellCollection;
import com.github.oobila.bukkit.gui.Gui;
import com.github.oobila.bukkit.gui.cells.BlockedCell;
import com.github.oobila.bukkit.gui.cells.ButtonCell;
import com.github.oobila.bukkit.itemstack.CustomItemStackBuilder;
import com.github.oobila.bukkit.util.enums.ColoredMaterialType;
import com.github.oobila.bukkit.util.enums.BlockColor;
import com.github.oobila.bukkit.util.MaterialUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class EndlessGui extends Gui {

    public static final int PAGE_SIZE = 45;

    private Cell[] pageIcons = new Cell[9];
    private Material pageMaterial = Material.PAPER;
    private Material tabMaterial;
    private int pages;
    private int pageIndex = 0;
    private BlockColor color;

    public EndlessGui(Plugin plugin, Player player, String title, CellCollection cellCollection) {
        this(plugin, player, title, cellCollection, BlockColor.WHITE);
    }

    public EndlessGui(Plugin plugin, Player player, String title, CellCollection cellCollection, BlockColor blockColor) {
        super(plugin, player, title, cellCollection);

        this.pages = (int) Math.ceil((cellCollection.getSize()) / 45d);
        this.tabMaterial = MaterialUtil.getColoredMaterial(blockColor, ColoredMaterialType.STAINED_GLASS_PANE);
        setPageButtons();
    }

    private ButtonCell getPageIcon() {
        return new ButtonCell(new CustomItemStackBuilder(pageMaterial)
                .displayName(String.format("Page %s of %s", pageIndex + 1, pages))
                .itemCount(pageIndex + 1)
                .build(),
                (e, player, button, gui) -> {}
        );
    }

    private void setPageButtons() {
        for (int i = 0; i < 9; i++) {
            final int index = i;
            switch (index) {
                case 4:
                    pageIcons[i] = getPageIcon();
                    break;
                case 2:
                    pageIcons[i] = new ButtonCell(new CustomItemStackBuilder(tabMaterial)
                            .displayName("Previous Page")
                            .build(),
                            (e, player, button, gui) -> {
                                EndlessGui mpg = (EndlessGui) gui;
                                mpg.pageIndex = mpg.pageIndex == 0 ? mpg.pageIndex : mpg.pageIndex - 1;
                                pageIcons[4] = getPageIcon();
                                mpg.reload();
                            });
                    break;
                case 6:
                    pageIcons[i] = new ButtonCell(new CustomItemStackBuilder(tabMaterial)
                            .displayName("Next Page")
                            .build(),
                            (e, player, button, gui) -> {
                                EndlessGui mpg = (EndlessGui) gui;
                                mpg.pageIndex = mpg.pageIndex == (pages - 1) ? mpg.pageIndex : mpg.pageIndex + 1;
                                pageIcons[4] = getPageIcon();
                                mpg.reload();
                            });
                    break;
                default:
                    pageIcons[i] = new BlockedCell();
                    break;
            }
        }
    }

    @Override
    public int getInventorySize() {
        return 54;
    }

    @Override
    public Cell getInventoryCell(int position) {
        if (position < 9) {
            return pageIcons[position];
        } else if ((position - 9) < getSize()){
            return getCell(pageIndex * PAGE_SIZE + (position - 9));
        } else {
            return getBlockedCell();
        }
    }

    @Override
    public ItemStack[] getCellIcons() {
        ItemStack[] itemStacks = new ItemStack[54];
        for (int i = 0; i < 54; i++) {
            itemStacks[i] = getInventoryCell(i).getIcon();
        }
        return itemStacks;
    }
}
