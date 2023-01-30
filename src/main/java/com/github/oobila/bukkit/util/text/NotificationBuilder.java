package com.github.oobila.bukkit.util.text;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NotificationBuilder {

    private List<OfflinePlayer> playersToNotify = new ArrayList<>();
    private boolean isOnlineOnly;
    private MessageBuilder messageBuilder;

    public NotificationBuilder(List<OfflinePlayer> players, String pattern) {
        messageBuilder = new MessageBuilder(pattern);
        playersToNotify = players;
    }

    public NotificationBuilder(OfflinePlayer player, String pattern) {
        messageBuilder = new MessageBuilder(pattern);
        playersToNotify.add(player);
    }

    public NotificationBuilder onlineOnly(boolean isOnlineOnly) {
        this.isOnlineOnly = isOnlineOnly;
        return this;
    }

    public NotificationBuilder variable(String variable) {
        messageBuilder.variable(variable);
        return this;
    }

    public NotificationBuilder booleanVariable(boolean b, String variable) {
        if (b) {
            return trueVariable(variable);
        } else {
            return falseVariable(variable);
        }
    }

    public NotificationBuilder trueVariable(String variable) {
        messageBuilder.trueVariable(variable);
        return this;
    }

    public NotificationBuilder falseVariable(String variable) {
        messageBuilder.falseVariable(variable);
        return this;
    }

    public NotificationBuilder variable(int variable) {
        messageBuilder.variable(variable);
        return this;
    }

    public NotificationBuilder variable(double variable) {
        messageBuilder.variable(variable);
        return this;
    }

    public NotificationBuilder variable(long variable) {
        messageBuilder.variable(variable);
        return this;
    }

    public NotificationBuilder variable(boolean variable) {
        messageBuilder.variable(variable);
        return this;
    }

    public NotificationBuilder money(String money) {
        messageBuilder.money(money);
        return this;
    }

    public NotificationBuilder money(double money) {
        messageBuilder.money(money);
        return this;
    }

    public NotificationBuilder money(int money) {
        messageBuilder.money(money);
        return this;
    }

    public NotificationBuilder color(String string, ChatColor chatColor) {
        messageBuilder.color(string, chatColor);
        return this;
    }

    public NotificationBuilder location(Location location) {
        messageBuilder.location(location);
        return this;
    }

    public NotificationBuilder player(OfflinePlayer player) {
        messageBuilder.player(player);
        return this;
    }

    public NotificationBuilder player(UUID playerId) {
        messageBuilder.player(playerId);
        return this;
    }

    public NotificationBuilder date(LocalDate expiryDate) {
        messageBuilder.date(expiryDate);
        return this;
    }

    public NotificationBuilder time(ZonedDateTime expiryDate) {
        messageBuilder.time(expiryDate);
        return this;
    }

    public NotificationBuilder dateAndTime(ZonedDateTime expiryDate) {
        messageBuilder.dateAndTime(expiryDate);
        return this;
    }

    public NotificationBuilder dateAndTime(ZonedDateTime expiryDate, String string) {
        messageBuilder.dateAndTime(expiryDate, string);
        return this;
    }

    public NotificationBuilder instruction(String instruction) {
        messageBuilder.instruction(instruction);
        return this;
    }

    public NotificationBuilder defaultColor(ChatColor yellow) {
        messageBuilder.defaultColor(yellow);
        return this;
    }

    public NotificationBuilder append(MessageBuilder messageBuilder) {
        this.messageBuilder.append(messageBuilder);
        return this;
    }

    public NotificationBuilder append(MessageListAppender messageListAppender) {
        messageBuilder.append(messageListAppender);
        return this;
    }

    public void send() {
        playersToNotify.forEach(offlinePlayer -> {
            if (!isOnlineOnly || offlinePlayer.isOnline()) {
                NotificationManager.sendNotification(offlinePlayer, messageBuilder);
            }
        });
    }
}
