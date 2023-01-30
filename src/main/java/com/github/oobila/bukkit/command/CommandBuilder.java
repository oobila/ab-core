package com.github.oobila.bukkit.command;

import com.github.oobila.bukkit.util.text.MessageBuilder;
import com.github.oobila.bukkit.util.text.NotificationBuilder;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class CommandBuilder implements CommandExecutor, TabCompleter {

    private static final String ARG_IS_NON_NUMERIC = "arg: {0} is not numeric";
    private static final String ARG_IS_NON_BOOLEAN = "arg: {0} is not a boolean";
    private static final String ARG_IS_NON_ENUM_OPTION = "arg: {0} is not an option\noptions: \n";

    private String name;
    private CommandBuilder parent = null;
    private List<String> aliases;
    private CommandExecutor commandExecutor;
    private PlayerCommandExecutor playerCommandExecutor;
    private Set<Class<? extends CommandSender>> allowedSenders = new HashSet<>();
    private List<Argument<?>> arguments = new ArrayList<>();
    private String permission;
    private String description;
    private Map<String, CommandBuilder> subCommands = new HashMap<>();
    private Map<String, CommandBuilder> subCommandsWithAliases = new HashMap<>();

    public CommandBuilder(String name) {
        this.name = name.toLowerCase();
    }

    public CommandBuilder aliases(String... aliases) {
        this.aliases = Arrays.asList(aliases);
        return this;
    }

    public CommandBuilder commandExecutor(CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
        return this;
    }

    public CommandBuilder playerCommand(PlayerCommandExecutor playerCommand) {
        this.playerCommandExecutor = playerCommand;
        return this;
    }

    public CommandBuilder allowedSenders(Set<Class<? extends CommandSender>> allowedSenders) {
        this.allowedSenders.addAll(allowedSenders);
        return this;
    }

    public CommandBuilder allowedSender(Class<? extends CommandSender> allowedSender) {
        this.allowedSenders.add(allowedSender);
        return this;
    }

    public <T> CommandBuilder argument(String argumentName, Class<T> type){
        arguments.add(new Argument<>(argumentName, type));
        return this;
    }

    public <T> CommandBuilder argument(String argumentName, Class<T> type, T min, T max){
        Argument<T> argument = new Argument<>(argumentName, type);
        argument.setMin(min);
        argument.setMax(max);
        arguments.add(argument);
        return this;
    }

    public <T> CommandBuilder argument(String argumentName, Class<T> type, T min, T max, List<T> fixedSuggestions){
        Argument<T> argument = new Argument<>(argumentName, type);
        argument.setMin(min);
        argument.setMax(max);
        argument.setFixedSuggestions(fixedSuggestions);
        arguments.add(argument);
        return this;
    }

    public <T> CommandBuilder argument(String argumentName, Class<T> type, List<T> fixedSuggestions){
        Argument<T> argument = new Argument<>(argumentName, type);
        argument.setFixedSuggestions(fixedSuggestions);
        arguments.add(argument);
        return this;
    }

    public <T> CommandBuilder argument(String argumentName, Class<T> type, SuggestionCallable<T> suggestionCallable){
        Argument<T> argument = new Argument<>(argumentName, type);
        argument.setSuggestionCallable(suggestionCallable);
        arguments.add(argument);
        return this;
    }

    public CommandBuilder permission(String permissionNode) {
        this.permission = permissionNode;
        return this;
    }

    public CommandBuilder description(String description) {
        this.description = description;
        return this;
    }

    public CommandBuilder subCommand(CommandBuilder commandBuilder) {
        commandBuilder.parent = this;
        this.subCommands.put(commandBuilder.name, commandBuilder);
        this.subCommandsWithAliases.put(commandBuilder.name, commandBuilder);
        if (commandBuilder.aliases != null) {
            commandBuilder.aliases.forEach(aka ->
                this.subCommandsWithAliases.put(aka, commandBuilder)
            );
        }
        return this;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (commandExecutor != null) {
            return commandExecutor.onCommand(sender, command, label, args);
        } else if (playerCommandExecutor != null){
            if (sender instanceof Player player) {
                if (hasPermission(player)) {
                    if (args.length > 0 && subCommandsWithAliases.containsKey(args[0])) {
                        return subCommandsWithAliases.get(args[0]).onCommand(
                                sender, command, label, Arrays.copyOfRange(args, 1, args.length));
                    } else if (argumentsFormedCorrectly(args, player)) {
                        playerCommandExecutor.onCommand(player, command, label, args);
                        return true;
                    }
                } else {
                    new NotificationBuilder(player, "You do not have permission for this command");
                }
            }
            return true;
        } else {
            Bukkit.getLogger().log(Level.SEVERE, "No command executor set");
            return true;
        }
    }

    private boolean hasPermission(Player player) {
        if (!player.hasPermission(permission)) {
            new NotificationBuilder(player,"You do not have permission to run this command").send();
            return false;
        }
        return true;
    }

    private boolean argumentsFormedCorrectly(String[] args, Player player) {
        if (args.length == 0 && arguments.isEmpty()) { //no arguments
            return true;
        } else if (args.length != 0 && args[0].equalsIgnoreCase("help")) {
            sendHelpMessage(player);
            return false;
        } else {
            for(int i=0; i < args.length; i++) {
                if (arguments.size() <= i) {
                    new NotificationBuilder(player, "too many arguments").send();
                    return false;
                }
                String input = args[i];
                Argument<?> argument = arguments.get(i);
                if (!checkType(input, argument.getType(), player) || !checkLimits(input, argument)) {
                    return false;
                }
            }
        }
        return true;
    }

    @SuppressWarnings("java:S3740")
    private boolean checkType(String input, Class type, Player player) {
        if (type.equals(String.class)) {
            return true;
        } else if(type.equals(int.class) || type.equals(Integer.class) ||
                type.equals(float.class) || type.equals(Float.class) ||
                type.equals(long.class) || type.equals(Long.class) ||
                type.equals(double.class) || type.equals(Double.class)) {
            if (!StringUtils.isNumeric(input)) {
                new NotificationBuilder(player, ARG_IS_NON_NUMERIC)
                        .variable(input).send();
                return false;
            } else {
                return true;
            }
        } else if(type.equals(boolean.class) || type.equals(Boolean.class)) {
            if (BooleanUtils.toBooleanObject(input) != null) {
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

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 0) {
            return Collections.emptyList();
        }
        List<String> suggestions = null;
        if (subCommandsWithAliases.containsKey(args[0].toLowerCase())) {
            return subCommandsWithAliases.get(args[0]).onTabComplete(sender, command, alias,
                    Arrays.copyOfRange(args, 1, args.length));
        } else if (!arguments.isEmpty() && arguments.size() >= args.length){
            Argument<?> argument = arguments.get(args.length - 1);
            if (argument.getFixedSuggestions() != null && !argument.getFixedSuggestions().isEmpty()) {
                suggestions = argument.getFixedSuggestions()
                        .stream().map(Object::toString).toList();
            } else {
                if (sender instanceof Player player && argument.getSuggestionCallable() != null) {
                    suggestions = argument.getSuggestionCallable().getSuggestions(player)
                            .stream().map(Object::toString).toList();
                }
            }
        }
        if (suggestions == null && !subCommands.isEmpty()) {
            return subCommands.keySet().stream().toList();
        }
        return suggestions;
    }

    protected void sendHelpMessage(Player player) {
        new NotificationBuilder(
                player,
                new MessageBuilder("---- {0} help ----\n")
                        .defaultColor(ChatColor.YELLOW)
                        .color(getParentPath(name), ChatColor.GOLD)
                        .append(getHelpMessageSegment())
                        .append(() ->
                            subCommands.values().stream().map(CommandBuilder::getHelpMessageSegment).toList()
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

    private String getParentPath(String path) {
        if (parent != null) {
            return parent.getParentPath(parent.name + " " + path);
        } else {
            return path;
        }
    }

    public PluginCommand register(JavaPlugin plugin) throws NullPointerException {
        PluginCommand pluginCommand = plugin.getCommand(name);
        if (pluginCommand == null) {
            throw new NullPointerException("command " + name + "does not exist");
        }
        pluginCommand.setAliases(aliases);
        pluginCommand.setExecutor(this);
        pluginCommand.setTabCompleter(this);
        return pluginCommand;
    }
}
