package com.github.oobila.bukkit.util.enums;

import lombok.Getter;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum BlockColor {
    WHITE(ChatColor.WHITE),
    LIGHT_GRAY(ChatColor.GRAY),
    GRAY(ChatColor.DARK_GRAY),
    BLACK(ChatColor.BLACK),
    BROWN(null),
    RED(ChatColor.RED),
    ORANGE(ChatColor.GOLD),
    YELLOW(ChatColor.YELLOW),
    LIME(ChatColor.GREEN),
    GREEN(ChatColor.DARK_GREEN),
    CYAN(ChatColor.DARK_AQUA),
    LIGHT_BLUE(ChatColor.AQUA),
    BLUE(ChatColor.BLUE),
    PURPLE(ChatColor.DARK_PURPLE),
    MAGENTA(null),
    PINK(ChatColor.LIGHT_PURPLE);

    private static final Map<ChatColor, BlockColor> chatColorBlockColorMap;

    static {
        chatColorBlockColorMap = Arrays.stream(BlockColor.values())
                .filter(blockColor -> blockColor.chatColor != null)
                .collect(Collectors.toMap(
                                (blockColor) -> blockColor.getChatColor(),
                                (blockColor) -> blockColor
                        )
                );
    }

    @Getter
    private ChatColor chatColor;

    BlockColor(ChatColor chatColor) {
        this.chatColor = chatColor;
    }

    public static BlockColor get(ChatColor chatColor) {
        return chatColorBlockColorMap.get(chatColor);
    }
}
