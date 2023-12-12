package com.github.oobila.bukkit.util;

import com.github.oobila.bukkit.util.text.MessageBuilder;
import com.github.oobila.bukkit.util.text.Notification;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.EnumSet;
import java.util.stream.Collectors;

/**
 * Utility class to help with Minecraft commands
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommandUtil {

    private static final String ARG_FORMAT = ChatColor.YELLOW + "<{0}" + ChatColor.YELLOW + ">";
    private static final String ARG_ENUM_FORMAT = ChatColor.YELLOW + "<{0}" + ChatColor.YELLOW + ">";
    private static final String ARG_ENUM_ENTRY_FORMAT = ChatColor.GOLD + "{0}";
    private static final String ENUM_SEPARATOR = ChatColor.YELLOW + "|";
    private static final String INCORRECT_COMMAND_ARGS = "incorrect-command-args";
    private static final String COMMAND_OPTIONS = "command-options";
    private static final String COMMAND_TYPE = "command-type";

    /**
     * Checks that the command usage is correct and prints the correct parameters to the player if incorrect.
     * @param player
     * @param strings
     * @param args
     * @param argTypes
     * @param mandatory
     * @return
     */
    public static boolean checkCommandUsage(Player player, String[] strings, String[] args, Class[] argTypes, int mandatory){
        return checkArgsSize(player, strings, args, mandatory) &&
                checkOptions(player, strings, args, argTypes);
    }

    private static boolean checkArgsSize(Player player, String[] strings, String[] args, int mandatory) {
        if(strings.length > args.length || strings.length < mandatory){
            Notification.sendNotification(player,
                    new MessageBuilder("Command requires {0} arguments")
                            .variable(mandatory));
            return false;
        }
        return true;
    }

    private static boolean checkOptions(Player player, String[] strings, String[] args, Class[] argTypes) {
        for(int i = 0; i < strings.length; i++){
            if (!checkCommandType(i, player, strings, argTypes) ||
                !checkCommandOptions(i, player, strings, args, argTypes)) {
                return false;
            }
        }
        return true;
    }

    private static boolean checkCommandType(int index, Player player, String[] strings, Class[] argTypes) {
        if((argTypes[index].equals(int.class) || argTypes[index].equals(Integer.class) ||
                argTypes[index].equals(float.class) || argTypes[index].equals(Float.class) ||
                argTypes[index].equals(long.class) || argTypes[index].equals(Long.class)) &&
                !StringUtils.isNumeric(strings[index])){

            Notification.sendNotification(player,
                    new MessageBuilder("Incorrect arg type used: {0}. Arg must be of type: {1}")
                            .variable(strings[index])
                            .variable(argTypes[index].getSimpleName()));
            return false;
        }
        return true;
    }

    private static boolean checkCommandOptions(int index, Player player, String[] strings, String[] args, Class[] argTypes) {
        boolean enumIsCorrect = true;
        try {
            if(argTypes[index].isEnum() && Enum.valueOf(argTypes[index], strings[index].toUpperCase()) == null){
                enumIsCorrect = false;
            }
        } catch (Exception e) {
            enumIsCorrect = false;
        }
        if(!enumIsCorrect){
            String options = printArgUsage(args[index], argTypes[index]);

            Notification.sendNotification(player,
                    new MessageBuilder("Incorrect arg used: {0}. Must use one of: {1}")
                            .variable(strings[index])
                            .variable(options));
            return false;
        }
        return true;
    }

    private static String printArgUsage(String argName, Class<? extends Enum> argType){
        if(argType.isEnum()){
            return MessageFormat.format(ARG_ENUM_FORMAT,
                    EnumSet.allOf(argType).stream().map(
                            e -> MessageFormat.format(ARG_ENUM_ENTRY_FORMAT, e.toString())
                    ).collect(Collectors.joining(ENUM_SEPARATOR))
            );
        } else {
            return MessageFormat.format(ARG_FORMAT, argName);
        }
    }

}
