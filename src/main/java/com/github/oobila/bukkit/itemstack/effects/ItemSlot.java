package com.github.oobila.bukkit.itemstack.effects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
@Getter
public enum ItemSlot {

    MAINHAND(false),
    OFFHAND(false),
    HEAD(true),
    CHEST(true),
    LEGS(true),
    FEET(true);

    private boolean isArmor;

    public void breakItem(Player player) {
        updateItem(player, null);
    }

    public void updateItem(Player player, ItemStack itemStack) {
        switch (this) {
            case MAINHAND: player.getInventory().setItemInMainHand(itemStack);
                return;
            case OFFHAND: player.getInventory().setItemInOffHand(itemStack);
                return;
            case HEAD: player.getInventory().setHelmet(itemStack);
                return;
            case CHEST: player.getInventory().setChestplate(itemStack);
                return;
            case LEGS: player.getInventory().setLeggings(itemStack);
                return;
            case FEET:player.getInventory().setBoots(itemStack);
                return;
        }
    }
}
