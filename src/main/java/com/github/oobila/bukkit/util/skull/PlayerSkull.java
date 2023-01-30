package com.github.oobila.bukkit.util.skull;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class PlayerSkull extends ItemStack {

    public PlayerSkull(OfflinePlayer player) {
        super(Material.PLAYER_HEAD);
        if (player != null) {
            SkullMeta headMeta = (SkullMeta) getItemMeta();
            headMeta.setOwningPlayer(player);
            setItemMeta(headMeta);
        }
    }
}
