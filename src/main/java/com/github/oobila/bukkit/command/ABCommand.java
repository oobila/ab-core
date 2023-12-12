package com.github.oobila.bukkit.command;

import com.github.oobila.bukkit.util.text.MessageBuilder;
import com.github.oobila.bukkit.util.text.NotificationBuilder;
import lombok.Getter;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class ABCommand implements ABCommandInterface, CommandExecutor, TabCompleter {

    private static final String ARG_IS_NON_NUMERIC = "arg: {0} is not numeric";
    private static final String ARG_IS_NON_BOOLEAN = "arg: {0} is not a boolean";
    private static final String ARG_IS_NON_ENUM_OPTION = "arg: {0} is not an option\noptions: \n";

    @Getter
    private String name;

    @Getter
    private String description;

    @Getter
    List<String> aliases;

    @Getter
    List<Argument<?>> arguments = new ArrayList<>();

    @Getter
    String permission;

    @Getter
    ABCommand parent = null;

    @Getter
    PlayerCommandExecutor command;

    @Getter
    Map<String, ABCommand> subCommands = new HashMap<>();
    Map<String, ABCommand> subCommandsWithAliases = new HashMap<>();

    ABCommand(String name, String description) {
        this.name = name.toLowerCase();
        this.description = description;
    }

    private boolean hasPermission(Player player) {
        if (permission != null) {
            if (!player.hasPermission(permission)) {
                new NotificationBuilder(player,"You do not have permission to run this command").send();
                return false;
            }
            return true;
        } else if (parent != null) {
            return parent.hasPermission(player);
        } else {
            new NotificationBuilder(player,"You do not have permission to run this command").send();
            return false;
        }
    }

    @SuppressWarnings("java:S3740")
    private boolean checkType(String input, Class type, Player player) {
        if (type.equals(String.class)) {
            return true;
        } else if(type.equals(int.class) || type.equals(Integer.class) ||
                type.equals(float.class) || type.equals(Float.class) ||
                type.equals(long.class) || type.equals(Long.class) ||
                type.equals(double.class) || type.equals(Double.class)) {
            if (!NumberUtils.isNumber(input)) {
                new NotificationBuilder(player, ARG_IS_NON_NUMERIC)
                        .variable(input).send();
                return false;
            } else {
                return true;
            }
        } else if(type.equals(boolean.class) || type.equals(Boolean.class)) {
            if (BooleanUtils.toBooleanObject(input) == null) {
                new NotificationBuilder(player, ARG_IS_NON_BOOLEAN)
                        .variable(input).send();
                return false;
            } else {
                return true;
            }
        } else if(type.isEnum()) {
            try{
                Enum.valueOf(type, input);
                return true;
            } catch (IllegalArgumentException exception) {
                new NotificationBuilder(player, ARG_IS_NON_ENUM_OPTION +
                        EnumSet.allOf(type).stream().map(
                                e -> " - " + e.toString()).collect(Collectors.joining("\n")
                        )
                ).variable(input).send();
                return false;
            }
        }
        return false;
    }

    @SuppressWarnings("java:S3776")
    private <T> boolean checkLimits(String input, Argument<T> argument) {
        try {
            if (argument.getMin() != null && argument.getMax() != null) {
                Class<T> type = argument.getType();
                if (type.equals(int.class) || type.equals(Integer.class)) {
                    int value = Integer.parseInt(input);
                    return value >= (int) argument.getMin() && value <= (int) argument.getMax();
                } else if (type.equals(float.class) || type.equals(Float.class)) {
                    float value = Float.parseFloat(input);
                    return value >= (float) argument.getMin() && value <= (float) argument.getMax();
                } else if (type.equals(long.class) || type.equals(Long.class)) {
                    long value = Long.parseLong(input);
                    return value >= (long) argument.getMin() && value <= (long) argument.getMax();
                } else if (type.equals(double.class) || type.equals(Double.class)) {
                    double value = Double.parseDouble(input);
                    return value >= (double) argument.getMin() && value <= (double) argument.getMax();
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private boolean argumentsFormedCorrectly(String[] args, Player player) {
        if (args.length == 0 && arguments.isEmpty()) { //no arguments required
            return true;
        } else {
            if (args.length > arguments.size()) {
                new NotificationBuilder(player, "too many arguments").send();
                sendHelpMessage(player);
                return false;
            }
            for(int i=0; i < arguments.size(); i++) {
                Argument<?> argument = arguments.get(i);
                if (!argument.isMandatory()) {
                    //reached non-mandatory arguments
                    return true;
                }
                if (args.length <= i) {
                    new NotificationBuilder(player, "not enough arguments").send();
                    sendHelpMessage(player);
                    return false;
                }
                String input = args[i];
                if (!checkType(input, argument.getType(), player) || !checkLimits(input, argument)) {
                    sendHelpMessage(player);
                    return false;
                }
            }
        }
        return true;
    }

    private String getParentPath(String path) {
        if (parent != null) {
            return parent.getParentPath(parent.name + " " + path);
        } else {
            return path;
        }
    }

    public void sendHelpMessage(Player player) {
        new NotificationBuilder(
                player,
                new MessageBuilder("---- {0} help ----\n")
                        .defaultColor(ChatColor.YELLOW)
                        .color(getParentPath(name), ChatColor.GOLD)
                        .append(getHelpMessageSegment())
                        .append(() ->
                                subCommands.values().stream().map(ABCommand::getHelpMessageSegment).toList()
                        )
                        .build()
        ).send();
    }

    private MessageBuilder getHelpMessageSegment() {
        return new MessageBuilder("{0} {1} - {2}")
                .color("/" + getParentPath(name), ChatColor.WHITE)
                .color(arguments.stream().map(argument -> "[" + argument.getName() + "]")
                        .collect(Collectors.joining(" ")), ChatColor.GRAY)
                .color(description, ChatColor.GOLD);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 0 && subCommandsWithAliases.containsKey(args[0])) {
            return subCommandsWithAliases.get(args[0]).onCommand(
                    sender,
                    command,
                    args[0],
                    Arrays.copyOfRange(args, 1, args.length)
            );
        } else if (sender instanceof Player player && hasPermission(player) && argumentsFormedCorrectly(args, player)) {
            onCommand(player, this, label, args);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 0 && subCommandsWithAliases.containsKey(args[0])) {
            return subCommandsWithAliases.get(args[0]).onTabComplete(
                    sender,
                    command,
                    args[0],
                    Arrays.copyOfRange(args, 1, args.length)
            );
        } else if (sender instanceof Player player &&
                hasPermission(player)) {
            return onTabComplete(player, this, label, args);
        }
        return Collections.EMPTY_LIST;
    }
}
