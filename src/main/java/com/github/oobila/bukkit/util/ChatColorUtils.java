package com.github.oobila.bukkit.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.ChatColor;

/**
 * Utility class to help with Minecraft chat colours
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatColorUtils {

    private static final String AMPERSAND = "&";
    private static final String EMPTY_STRING = "";

    /**
     * Function that replaces ChatColor codes with the ChatColor in a String. i.e. "&a" becomes ChatColor.GREEN
     * @param s
     * @return
     */
    public static String formatColors(String s){
        for(ChatColor color : ChatColor.values()){
            s = s.replaceAll(AMPERSAND + color.getChar(), color + EMPTY_STRING);
        }
        return s;
    }

    /**
     * Ensures a ChatColor is using it's light variant. i.e. DARK_GREEN becomes GREEN
     * @param chatColor
     * @return
     */
    public static ChatColor toLight(ChatColor chatColor){
        switch (chatColor) {
            case BLACK, DARK_GRAY:
                return ChatColor.DARK_GRAY;
            case DARK_BLUE, BLUE:
                return ChatColor.BLUE;
            case DARK_GREEN, GREEN:
                return ChatColor.GREEN;
            case DARK_AQUA, AQUA:
                return ChatColor.AQUA;
            case DARK_RED, RED:
                return ChatColor.RED;
            case DARK_PURPLE, LIGHT_PURPLE:
                return ChatColor.LIGHT_PURPLE;
            case GOLD, YELLOW:
                return ChatColor.YELLOW;
            case GRAY, WHITE:
                return ChatColor.WHITE;
            default:
                return chatColor;
        }
    }

    /**
     * Ensures a ChatColor is using it's dark variant. i.e. GREEN becomes DARK_GREEN
     * @param chatColor
     * @return
     */
    public static ChatColor toDark(ChatColor chatColor){
        switch (chatColor) {
            case BLACK, DARK_GRAY:
                return ChatColor.BLACK;
            case DARK_BLUE, BLUE:
                return ChatColor.DARK_BLUE;
            case DARK_GREEN, GREEN:
                return ChatColor.DARK_GREEN;
            case DARK_AQUA, AQUA:
                return ChatColor.DARK_AQUA;
            case DARK_RED, RED:
                return ChatColor.DARK_RED;
            case DARK_PURPLE, LIGHT_PURPLE:
                return ChatColor.DARK_PURPLE;
            case GOLD, YELLOW:
                return ChatColor.GOLD;
            case GRAY, WHITE:
                return ChatColor.GRAY;
            default:
                return chatColor;
        }
    }
    
}
