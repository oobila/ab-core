package com.github.oobila.bukkit.util;

import com.github.oobila.bukkit.CorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class TeleportUtil {

    public static void teleportWithRetry(Player player, Location location) {
        teleportWithRetry(player, location, 5);
    }

    private static void teleportWithRetry(Player player, Location location, int retry) {
        if (retry == 0) {
            Bukkit.getLogger().log(Level.SEVERE, "could not teleport player: " + player.getName());
        } else {
            final int newRetry = retry - 1;
            player.teleport(location);
            Bukkit.getScheduler().scheduleSyncDelayedTask(
                    CorePlugin.getInstance(),
                    () -> {
                        if (
                                player.getWorld().getUID() != location.getWorld().getUID() ||
                                        player.getLocation().distance(location) > 2
                        ) {
                            teleportWithRetry(player, location, newRetry);
                        }
                    },
                    2
            );
        }
    }

}
