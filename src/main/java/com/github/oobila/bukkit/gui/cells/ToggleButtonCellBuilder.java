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

public class ToggleButtonCellBuilder {

    private static final String EMPTY = "{0}";
    private static final String INSTRUCTION_PREFIX = "-> ";

    private ItemStack enabledItemStack;
    private ItemStack disabledItemStack;
    private boolean enabled;
    private String enabledName;
    private String disabledName;
    private List<String> enabledLore = new ArrayList<>();
    private List<String> disabledLore = new ArrayList<>();
    private ToggleButtonCell.ButtonClickAction enableButtonClickAction;
    private ToggleButtonCell.ButtonClickAction disableButtonClickAction;

    public ToggleButtonCellBuilder(ItemStack enabledItemStack, ItemStack disabledItemStack) {
        this.enabledItemStack = enabledItemStack;
        this.disabledItemStack = disabledItemStack;
    }

    public ToggleButtonCellBuilder(Material enabledMaterial, Material disabledMaterial) {
        this(new ItemStack(enabledMaterial), new ItemStack(disabledMaterial));
    }

    public ToggleButtonCellBuilder enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public ToggleButtonCellBuilder enabledName(String name) {
        this.enabledName = ChatColor.AQUA + name;
        return this;
    }

    public ToggleButtonCellBuilder disabledName(String name) {
        this.disabledName = ChatColor.DARK_GRAY + name;
        return this;
    }

    public ToggleButtonCellBuilder enabledLore(String lore) {
        this.enabledLore.add(new MessageBuilder(lore).build());
        return this;
    }

    public ToggleButtonCellBuilder disabledLore(String lore) {
        this.disabledLore.add(new MessageBuilder(lore).build());
        return this;
    }

    public ToggleButtonCellBuilder enabledLore(MessageBuilder messageBuilder) {
        this.enabledLore.add(messageBuilder.build());
        return this;
    }

    public ToggleButtonCellBuilder disabledLore(MessageBuilder messageBuilder) {
        this.disabledLore.add(messageBuilder.build());
        return this;
    }

    public ToggleButtonCellBuilder enabledLoreInstruction(String instruction) {
        this.enabledLore.add(new MessageBuilder(EMPTY).instruction(INSTRUCTION_PREFIX + instruction).build());
        return this;
    }

    public ToggleButtonCellBuilder disabledLoreInstruction(String instruction) {
        this.disabledLore.add(new MessageBuilder(EMPTY).instruction(INSTRUCTION_PREFIX + instruction).build());
        return this;
    }

    public ToggleButtonCellBuilder onEnable(ToggleButtonCell.ButtonClickAction buttonClickAction) {
        this.enableButtonClickAction = buttonClickAction;
        return this;
    }

    public ToggleButtonCellBuilder onDisable(ToggleButtonCell.ButtonClickAction buttonClickAction) {
        this.disableButtonClickAction = buttonClickAction;
        return this;
    }

    public ToggleButtonCell build() {
        CustomItemStack builtEnabledItemStack = new CustomItemStackBuilder(this.enabledItemStack)
                .lore(enabledLore)
                .build();
        if (enabledName != null) {
            ItemMeta itemMeta = builtEnabledItemStack.getItemMeta();
            itemMeta.setDisplayName(enabledName);
            builtEnabledItemStack.setItemMeta(itemMeta);
        }

        CustomItemStack builtDisabledItemStack = new CustomItemStackBuilder(this.disabledItemStack)
                .lore(disabledLore)
                .build();
        if (disabledName != null) {
            ItemMeta itemMeta = builtDisabledItemStack.getItemMeta();
            itemMeta.setDisplayName(disabledName);
            builtDisabledItemStack.setItemMeta(itemMeta);
        }

        return new ToggleButtonCell(enabled, builtEnabledItemStack, builtDisabledItemStack,
                enableButtonClickAction, disableButtonClickAction);
    }

}