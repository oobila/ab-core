package com.github.oobila.bukkit.util;

import com.github.oobila.bukkit.CorePlugin;
import com.github.oobila.bukkit.util.text.NotificationBuilder;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class VaultUtils {

    public static void givePlayerMoney(Player player, Double money) {
        CorePlugin.getInstance().getEconomy().depositPlayer(player, money);
        player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_STEP,1,1 );
        new NotificationBuilder(player, "{0} has been added to your balance")
                .money(money)
                .send();
    }

    public static void takePlayerMoney(Player player, Double money) {
        CorePlugin.getInstance().getEconomy().withdrawPlayer(player, money);
        player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_STEP,1,1 );
        new NotificationBuilder(player, "{0} has been taken from your balance")
                .money(money * -1)
                .send();
    }

}
