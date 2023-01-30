package com.github.oobila.bukkit.gui.objects;

import com.github.oobila.bukkit.gui.Cell;
import com.github.oobila.bukkit.gui.GuiBase;
import com.github.oobila.bukkit.itemstack.CustomItemStack;
import com.github.oobila.bukkit.util.ItemStackUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class BlockedCell extends MenuItemCell {

    private static ItemStack defaultItemStack = new BlockedItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);

    public BlockedCell() {
        super(defaultItemStack);
    }

    public BlockedCell(ItemStack itemStack) {
        super(itemStack);
    }

    public BlockedCell(Material material) {
        super(new BlockedItemStack(material));
    }

    @Override
    public void onClick(InventoryClickEvent e, Player player, Cell cell, GuiBase guiMenu) {
        e.setCancelled(true);
    }

    private static class BlockedItemStack extends CustomItemStack {

        protected BlockedItemStack(Material material) {
            super(null, material);
            setDisplayName(" ");
            ItemStackUtil.makeUnstackable(this);
        }
    }
}
