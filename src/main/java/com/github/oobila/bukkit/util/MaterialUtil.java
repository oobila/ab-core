package com.github.oobila.bukkit.util;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility class to help with common functions for Minecraft Materials
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MaterialUtil {

    // #### GENERIC ####

    public static List<Material> getMaterialList(Collection<String> strings) {
        return strings.stream().map(Material::getMaterial).toList();
    }

    // #### COLORED MATERIALS ####

    private static final int NUM_OF_COLORS = 16;
    private static final Map<ColoredMaterialMeta, Material> coloredMaterials = new HashMap<>();
    private static final Map<ColoredMaterialType, List<Material>> coloredMaterialLists = new HashMap<>();
    static {
        for(ColoredMaterialType coloredMaterialType : ColoredMaterialType.values()) {
            for (BlockColor blockColor : BlockColor.values()) {
                Material material = Material.getMaterial(blockColor.name() + "_" + coloredMaterialType.name());
                coloredMaterials.put(new ColoredMaterialMeta(blockColor, coloredMaterialType), material);
                coloredMaterialLists.computeIfAbsent(coloredMaterialType, (k) -> new ArrayList<>());
                coloredMaterialLists.get(coloredMaterialType).add(material);
            }
        }
    }

    /**
     * Returns a random coloured block
     * @param type
     * @return
     */
    public static Material randomColoredBlock(ColoredMaterialType type){
        return coloredMaterialLists.get(type).get(ThreadLocalRandom.current().nextInt(NUM_OF_COLORS));
    }

    /**
     * Returns a set of random colored materials.
     * @param type
     * @param size
     * @return
     */
    @SuppressWarnings("java:S5413")
    public static Material[] randomColoredBlockArray(ColoredMaterialType type, int size){
        return (Material[]) Stream.generate(() -> {
            return randomColoredBlock(type);
        }).limit(size).toArray();
    }

    public static Material getColoredMaterial(BlockColor blockColor, ColoredMaterialType type){
        return coloredMaterials.get(new ColoredMaterialMeta(blockColor, type));
    }

    public static Material getColoredMaterial(ChatColor chatColor, ColoredMaterialType type){
        return coloredMaterials.get(new ColoredMaterialMeta(BlockColor.get(chatColor), type));
    }

    public enum ColoredMaterialType {
        WOOL,
        CARPET,
        TERRACOTTA,
        CONCRETE,
        CONCRETE_POWDER,
        GLAZED_TERRACOTTA,
        STAINED_GLASS,
        STAINED_GLASS_PANE,
        SHULKER_BOX,
        BED,
        CANDLE,
        BANNER
    }

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

    // #### SIGNS ####

    private static final List<Material> SIGN_MATERIALS = Arrays.asList(
            Material.ACACIA_SIGN,
            Material.ACACIA_WALL_SIGN,
            Material.BIRCH_SIGN,
            Material.BIRCH_WALL_SIGN,
            Material.CRIMSON_SIGN,
            Material.CRIMSON_WALL_SIGN,
            Material.DARK_OAK_SIGN,
            Material.DARK_OAK_WALL_SIGN,
            Material.JUNGLE_SIGN,
            Material.JUNGLE_WALL_SIGN,
            Material.OAK_SIGN,
            Material.OAK_WALL_SIGN,
            Material.SPRUCE_SIGN,
            Material.SPRUCE_WALL_SIGN,
            Material.WARPED_SIGN,
            Material.WARPED_WALL_SIGN
    );

    public static boolean isSign(Material type) {
        return SIGN_MATERIALS.contains(type);
    }
}
