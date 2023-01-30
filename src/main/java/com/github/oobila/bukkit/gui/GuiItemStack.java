package com.github.oobila.bukkit.gui;

import com.github.oobila.bukkit.Constants;
import com.github.oobila.bukkit.CorePlugin;
import com.github.oobila.bukkit.util.ItemMetaUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GuiItemStack extends ItemStack{

    private static final String TRUE = "true";

    private ItemStack cleanItemStack;

    public GuiItemStack(ItemStack itemStack) {
        super(itemStack);
        this.cleanItemStack = itemStack.clone();
        ItemMeta itemMeta = getItemMeta();
        ItemMetaUtil.addString(itemMeta, new NamespacedKey(CorePlugin.getInstance(), Constants.GUI_KEY), TRUE);
        ItemMetaUtil.makeUnstackable(itemMeta);
        setItemMeta(itemMeta);
    }

    public ItemStack getCleanItemStack() {
        return cleanItemStack;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GuiItemStack) {
            return super.equals(obj);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
