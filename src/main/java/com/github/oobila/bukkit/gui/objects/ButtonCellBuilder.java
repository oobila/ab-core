package com.github.oobila.bukkit.gui.objects;

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

public class ButtonCellBuilder {

    private static final String EMPTY = "{0}";
    private static final String INSTRUCTION_PREFIX = "-> ";

    private Plugin plugin;
    private ItemStack itemStack;
    private String name;
    private List<String> lore = new ArrayList<>();
    private ButtonCell.ButtonClickAction buttonClickAction;
    private long seconds;

    public ButtonCellBuilder(Plugin plugin, ItemStack itemStack) {
        this.plugin = plugin;
        this.itemStack = itemStack;
    }

    public ButtonCellBuilder(Plugin plugin, Material material) {
        this(plugin, new ItemStack(material));
    }

    public ButtonCellBuilder name(String name) {
        this.name = ChatColor.YELLOW + name;
        return this;
    }

    public ButtonCellBuilder lore(String lore) {
        this.lore.add(new MessageBuilder(lore).build());
        return this;
    }

    public ButtonCellBuilder lore(MessageBuilder messageBuilder) {
        this.lore.add(messageBuilder.build());
        return this;
    }

    public ButtonCellBuilder loreInstruction(String instruction) {
        this.lore.add(new MessageBuilder(EMPTY).instruction(INSTRUCTION_PREFIX + instruction).build());
        return this;
    }

    public ButtonCellBuilder onClick(ButtonCell.ButtonClickAction buttonClickAction) {
        this.buttonClickAction = buttonClickAction;
        return this;
    }

    public ButtonCellBuilder addCooldown(long seconds) {
        this.seconds = seconds;
        return this;
    }

    public ButtonCell build() {
        CustomItemStack customItemStack = new CustomItemStackBuilder(plugin, itemStack)
                .lore(lore)
                .build();
        if (name != null) {
            ItemMeta itemMeta = customItemStack.getItemMeta();
            itemMeta.setDisplayName(name);
            customItemStack.setItemMeta(itemMeta);
        }
        ButtonCell buttonCell = new ButtonCell(customItemStack, buttonClickAction != null ?
                buttonClickAction : (e, player, button, guiMenu) -> {});
        buttonCell.addCooldown(seconds);
        return buttonCell;
    }

}
