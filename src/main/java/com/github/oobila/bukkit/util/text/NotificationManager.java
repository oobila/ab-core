package com.github.oobila.bukkit.util.text;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NotificationManager {

    @Getter
    private static Map<UUID, List<String>> storedNotifications = new HashMap<>();

    public static void sendNotification(OfflinePlayer offlinePlayer, MessageBuilder messageBuilder) {
        sendNotification(offlinePlayer, messageBuilder.build());
    }

    public static void sendNotification(OfflinePlayer offlinePlayer, String string) {
        if (offlinePlayer.isOnline()) {
            offlinePlayer.getPlayer().sendMessage(string);
        } else {
            storedNotifications.computeIfAbsent(offlinePlayer.getUniqueId(), uuid -> new LinkedList<>());
            storedNotifications.get(offlinePlayer.getUniqueId())
                    .add(string);
        }
    }

    public static void resolveNotifications(Player player) {
        if (player.isOnline()) {
            List<String> notifications = storedNotifications.get(player.getUniqueId());
            if (notifications != null) {
                notifications.forEach(player::sendMessage);
                storedNotifications.remove(player.getUniqueId());
            }
        }
    }
}