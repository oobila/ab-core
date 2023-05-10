package com.github.oobila.bukkit.gui.cells;

import com.github.oobila.bukkit.itemstack.CustomItemStack;
import com.github.oobila.bukkit.itemstack.CustomItemStackBuilder;
import com.github.oobila.bukkit.util.text.MessageBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class SelectionButtonCellBuilder<T> {

    private Plugin plugin;
    private T returnObject;
    private ItemStack itemStack;
    private String name;
    private List<String> lore = new ArrayList<>();
    private boolean disableSelection;

    public SelectionButtonCellBuilder(Plugin plugin, T returnObject, ItemStack itemStack) {
        this.plugin = plugin;
        this.returnObject = returnObject;
        this.itemStack = itemStack;
    }

    public SelectionButtonCellBuilder(Plugin plugin, T returnObject, Material material) {
        this(plugin, returnObject, new ItemStack(material));
    }

    public SelectionButtonCellBuilder<T> name(String name) {
        this.name = ChatColor.YELLOW + name;
        return this;
    }

    public SelectionButtonCellBuilder<T> lore(String lore) {
        this.lore.add(new MessageBuilder(lore).build());
        return this;
    }

    public SelectionButtonCellBuilder<T> lore(MessageBuilder messageBuilder) {
        this.lore.add(messageBuilder.build());
        return this;
    }

    public SelectionButtonCellBuilder<T> disableSelection() {
        this.disableSelection = true;
        return this;
    }

    public SelectionButtonCell<T> build() {
        CustomItemStack customItemStack = new CustomItemStackBuilder(plugin, itemStack)
                .lore(lore)
                .build();
        if (name != null) {
            ItemMeta itemMeta = customItemStack.getItemMeta();
            itemMeta.setDisplayName(name);
            customItemStack.setItemMeta(itemMeta);
        }
        SelectionButtonCell<T> selectionButtonCell = new SelectionButtonCell<>(returnObject, customItemStack);
        if (disableSelection) {
            selectionButtonCell.disableSelection();
        }
        return selectionButtonCell;
    }
}
