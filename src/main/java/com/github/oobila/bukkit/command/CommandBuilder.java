package com.github.oobila.bukkit.command;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class CommandBuilder {

    private String name;
    private String description;
    private List<String> aliases;
    private List<Argument<?>> arguments = new ArrayList<>();
    private String permission;
    private ABCommand parent = null;
    private Map<String, ABCommand> subCommands = new HashMap<>();
    private Map<String, ABCommand> subCommandsWithAliases = new HashMap<>();
    private PlayerCommandExecutor command;
    
    public CommandBuilder(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public CommandBuilder aliases(String... aliases) {
        this.aliases = Arrays.asList(aliases);
        return this;
    }

    public CommandBuilder command(PlayerCommandExecutor command) {
        this.command = command;
        return this;
    }

    public <T> CommandBuilder argument(String argumentName, Class<T> type, boolean mandatory){
        Argument<T> argument = new Argument<>(argumentName, type, mandatory);
        if (type.isEnum()) {
            argument.setFixedSuggestions(Arrays.stream(type.getEnumConstants()).toList());
        }
        compareArgs(argument);
        arguments.add(argument);
        return this;
    }

    public <T> CommandBuilder argument(String argumentName, Class<T> type, T min, T max, boolean mandatory){
        Argument<T> argument = new Argument<>(argumentName, type, mandatory);
        argument.setMin(min);
        argument.setMax(max);
        compareArgs(argument);
        arguments.add(argument);
        return this;
    }

    public <T> CommandBuilder argument(String argumentName, Class<T> type, T min, T max, boolean mandatory, List<T> fixedSuggestions){
        Argument<T> argument = new Argument<>(argumentName, type, mandatory);
        argument.setMin(min);
        argument.setMax(max);
        argument.setFixedSuggestions(fixedSuggestions);
        compareArgs(argument);
        arguments.add(argument);
        return this;
    }

    public <T> CommandBuilder argument(String argumentName, Class<T> type, boolean mandatory, List<T> fixedSuggestions){
        Argument<T> argument = new Argument<>(argumentName, type, mandatory);
        argument.setFixedSuggestions(fixedSuggestions);
        compareArgs(argument);
        arguments.add(argument);
        return this;
    }

    public <T> CommandBuilder argument(String argumentName, Class<T> type, boolean mandatory, SuggestionCallable<T> suggestionCallable){
        Argument<T> argument = new Argument<>(argumentName, type, mandatory);
        argument.setSuggestionCallable(suggestionCallable);
        compareArgs(argument);
        arguments.add(argument);
        return this;
    }

    private void compareArgs(Argument a) {
        if (arguments.size() > 0 && !arguments.get(arguments.size() - 1).isMandatory() && a.isMandatory()) {
            Bukkit.getLogger().log(Level.SEVERE, name + ": Mandatory arguments must exist at the end of the command");
        }
    }

    public CommandBuilder permission(String permissionNode) {
        this.permission = permissionNode;
        return this;
    }

    public CommandBuilder subCommand(ABCommand subCommand) {
        this.subCommands.put(subCommand.getName(), subCommand);
        this.subCommandsWithAliases.put(subCommand.getName(), subCommand);
        if (subCommand.getAliases() != null) {
            subCommand.getAliases().forEach(aka ->
                    this.subCommandsWithAliases.put(aka, subCommand)
            );
        }
        return this;
    }

    private void setParents(ABCommand abCommand) {
        abCommand.subCommands.values().forEach(subCommand -> {
            subCommand.parent = abCommand;
            setParents(subCommand);
        });
    }

    public ABCommand build() {
        ABCommand abCommand = new ABCommand(name, description) {
            @Override
            public void onCommand(Player player, ABCommand abCommand, String label, String[] args) {
                if (command != null) {
                    command.onCommand(player, abCommand, label, args);
                } else {
                    abCommand.sendHelpMessage(player);
                }
            }

            @Override
            public List<String> onTabComplete(Player player, ABCommand abCommand, String label, String[] args) {
                if (args.length == 0) {
                    return Collections.emptyList();
                }
                List<String> suggestions = null;
                if (!arguments.isEmpty() && arguments.size() >= args.length) {
                    Argument<?> argument = arguments.get(args.length - 1);
                    if (argument.getFixedSuggestions() != null && !argument.getFixedSuggestions().isEmpty()) {
                        suggestions = argument.getFixedSuggestions()
                                .stream().map(Object::toString).toList();
                    } else if (argument.getSuggestionCallable() != null) {
                        suggestions = argument.getSuggestionCallable().getSuggestions(player)
                                .stream().map(Object::toString).toList();
                    }
                }
                if (args.length <= 1 && suggestions == null && !subCommands.isEmpty()) {
                    return subCommandsWithAliases.keySet().stream().toList();
                }
                if (suggestions == null) {
                    return Collections.EMPTY_LIST;
                } else {
                    return suggestions;
                }
            }
        };
        abCommand.aliases = this.aliases;
        abCommand.arguments = this.arguments;
        abCommand.permission = this.permission;
        abCommand.command = this.command;
        abCommand.subCommands = this.subCommands;
        abCommand.subCommandsWithAliases = this.subCommandsWithAliases;
        setParents(abCommand);
        return abCommand;
    }

    public void buildAndRegister(JavaPlugin plugin) {
        ABCommand abCommand = build();
        PluginCommand pluginCommand = plugin.getCommand(name);
        if (pluginCommand == null) {
            throw new NullPointerException("command " + name + "does not exist");
        }
        pluginCommand.setAliases(aliases);
        pluginCommand.setExecutor(abCommand);
        pluginCommand.setTabCompleter(abCommand);
    }
    
}
