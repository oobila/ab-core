package com.github.oobila.bukkit.util.text;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class MessageBuilder {

    private static final ChatColor MESSAGE = ChatColor.GOLD;
    private static final ChatColor VARIABLE = ChatColor.RED;
    private static final ChatColor TRUE_VARIABLE = ChatColor.GREEN;
    private static final ChatColor FALSE_VARIABLE = ChatColor.RED;
    private static final ChatColor MONEY = ChatColor.GREEN;
    private static final ChatColor NEGATIVE_MONEY = ChatColor.RED;
    private static final ChatColor INSTRUCTION = ChatColor.LIGHT_PURPLE;

    private static final String EMPTY_STRING = "";

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter DATE_AND_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd @ HH:mm:ss");
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm:ss");

    private String pattern;
    private List<String> args = new LinkedList<>();
    private List<MessageBuilder> appendix = new LinkedList<>();
    private ChatColor defaultColor = MESSAGE;

    public MessageBuilder(String pattern) {
        this.pattern = defaultColor + pattern;
    }

    public MessageBuilder variable(String variable) {
        args.add(VARIABLE + variable + defaultColor);
        return this;
    }

    public MessageBuilder variables(String[] variables) {
        for(String variable : variables) {
            args.add(VARIABLE + variable + defaultColor);
        }
        return this;
    }

    public MessageBuilder trueVariable(String variable) {
        args.add(TRUE_VARIABLE + variable + defaultColor);
        return this;
    }

    public MessageBuilder falseVariable(String variable) {
        args.add(FALSE_VARIABLE + variable + defaultColor);
        return this;
    }

    public MessageBuilder variable(int variable) {
        args.add(VARIABLE + String.valueOf(variable) + defaultColor);
        return this;
    }

    public MessageBuilder variable(double variable) {
        args.add(VARIABLE + String.valueOf(variable) + defaultColor);
        return this;
    }

    public MessageBuilder variable(long variable) {
        args.add(VARIABLE + String.valueOf(variable) + defaultColor);
        return this;
    }

    public MessageBuilder variable(boolean variable) {
        args.add(VARIABLE + String.valueOf(variable) + defaultColor);
        return this;
    }

    public MessageBuilder money(String money) {
        args.add(MONEY + money + defaultColor);
        return this;
    }

    public MessageBuilder money(double money) {
        args.add((money < 0 ? NEGATIVE_MONEY : MONEY) + String.valueOf(money) + defaultColor);
        return this;
    }

    public MessageBuilder money(int money) {
        args.add((money < 0 ? NEGATIVE_MONEY : MONEY) + String.valueOf(money) + defaultColor);
        return this;
    }

    public MessageBuilder color(String string, ChatColor chatColor) {
        args.add(chatColor + string + defaultColor);
        return this;
    }

    public MessageBuilder location(Location location) {
        args.add(MessageFormat.format("{0}[x={1},y={2},z={3}]",
                defaultColor,
                VARIABLE + String.valueOf(location.getBlockX()) + defaultColor,
                VARIABLE + String.valueOf(location.getBlockY()) + defaultColor,
                VARIABLE + String.valueOf(location.getBlockZ()) + defaultColor));
        return this;
    }

    public MessageBuilder player(OfflinePlayer player) {
        if (player == null) {
            args.add(EMPTY_STRING);
        } else {
            args.add(VARIABLE + player.getName() + defaultColor);
        }
        return this;
    }

    public MessageBuilder player(UUID playerId) {
        if (playerId == null || Bukkit.getOfflinePlayer(playerId) == null) {
            args.add(EMPTY_STRING);
        } else {
            args.add(VARIABLE + Bukkit.getOfflinePlayer(playerId).getName() + defaultColor);
        }
        return this;
    }

    public MessageBuilder date(LocalDate expiryDate) {
        args.add(VARIABLE + DATE_FORMATTER.format(expiryDate) + defaultColor);
        return this;
    }

    public MessageBuilder time(ZonedDateTime expiryDate) {
        args.add(VARIABLE + TIME_FORMATTER.format(expiryDate) + defaultColor);
        return this;
    }

    public MessageBuilder dateAndTime(ZonedDateTime expiryDate) {
        if (expiryDate == null) {
            args.add("");
        } else {
            args.add(VARIABLE + DATE_AND_TIME_FORMATTER.format(expiryDate) + defaultColor);
        }
        return this;
    }

    public MessageBuilder dateAndTime(ZonedDateTime expiryDate, String string) {
        if (expiryDate != null) {
            return dateAndTime(expiryDate);
        } else {
            return variable(string);
        }
    }

    public MessageBuilder instruction(String instruction) {
        args.add(INSTRUCTION + instruction + defaultColor);
        return this;
    }

    public MessageBuilder booleanVariable(boolean b, String variable) {
        if (b) {
            return trueVariable(variable);
        } else {
            return falseVariable(variable);
        }
    }

    public MessageBuilder defaultColor(ChatColor defaultColor) {
        this.defaultColor = defaultColor;
        return this;
    }

    public MessageBuilder append(MessageBuilder messageBuilder) {
        appendix.add(messageBuilder);
        return this;
    }

    public MessageBuilder append(MessageListAppender messageListAppender) {
        appendix.addAll(messageListAppender.getAppendix());
        return this;
    }

    public String build() {
        StringBuilder sb = new StringBuilder();
        sb.append(defaultColor);
        if (!args.isEmpty()) {
            sb.append(MessageFormat.format(pattern, args.toArray(new String[args.size()])));
        } else {
            sb.append(pattern);
        }
        appendix.forEach(messageBuilder ->
            sb.append("\n  " + messageBuilder.build())
        );
        return sb.toString();
    }
}
