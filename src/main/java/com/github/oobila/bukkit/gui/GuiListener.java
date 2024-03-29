package com.github.oobila.bukkit.gui;

import com.github.oobila.bukkit.util.text.NotificationManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GuiListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        GuiManager.removePlayerSelectionData(player);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        NotificationManager.resolveNotifications(event.getPlayer());
    }
}
