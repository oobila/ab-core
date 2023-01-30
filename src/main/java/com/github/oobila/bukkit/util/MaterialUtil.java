package com.github.oobila.bukkit.util;

import lombok.Getter;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Utility class to help with common functions for Minecraft Materials
 */
public class MaterialUtil {

    @Getter
    private static final List<Material> COLORED_GLASS_PANES = Arrays.asList(
            Material.RED_STAINED_GLASS_PANE,
            Material.LIGHT_BLUE_STAINED_GLASS_PANE,
            Material.YELLOW_STAINED_GLASS_PANE,
            Material.LIME_STAINED_GLASS_PANE,
            Material.ORANGE_STAINED_GLASS_PANE,
            Material.BLUE_STAINED_GLASS_PANE,
            Material.MAGENTA_STAINED_GLASS_PANE,
            Material.GREEN_STAINED_GLASS_PANE,
            Material.BROWN_STAINED_GLASS_PANE,
            Material.PINK_STAINED_GLASS_PANE,
            Material.PURPLE_STAINED_GLASS_PANE,
            Material.CYAN_STAINED_GLASS_PANE
    );

    @Getter
    private static final List<Material> COLORED_TERRACOTTA = Arrays.asList(
            Material.RED_TERRACOTTA,
            Material.LIGHT_BLUE_TERRACOTTA,
            Material.YELLOW_TERRACOTTA,
            Material.LIME_TERRACOTTA,
            Material.ORANGE_TERRACOTTA,
            Material.BLUE_TERRACOTTA,
            Material.MAGENTA_TERRACOTTA,
            Material.GREEN_TERRACOTTA,
            Material.BROWN_TERRACOTTA,
            Material.PINK_TERRACOTTA,
            Material.PURPLE_TERRACOTTA,
            Material.CYAN_TERRACOTTA
    );

    @Getter
    private static final List<Material> COLORED_CONCRETE = Arrays.asList(
            Material.RED_CONCRETE,
            Material.LIGHT_BLUE_CONCRETE,
            Material.YELLOW_CONCRETE,
            Material.LIME_CONCRETE,
            Material.ORANGE_CONCRETE,
            Material.BLUE_CONCRETE,
            Material.MAGENTA_CONCRETE,
            Material.GREEN_CONCRETE,
            Material.BROWN_CONCRETE,
            Material.PINK_CONCRETE,
            Material.PURPLE_CONCRETE,
            Material.CYAN_CONCRETE
    );

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

    private MaterialUtil() {
    }

    /**
     * Returns a random coloured glass pane
     * @return
     */
    public static Material randomColoredGlassPane(){
        return COLORED_GLASS_PANES.get(ThreadLocalRandom.current().nextInt(COLORED_GLASS_PANES.size()));
    }

    /**
     * Returns a set of random glass panes. No duplicates are used until the size is larger than the number of colours
     * where the pool is refreshed.
     * @param size
     * @return
     */
    @SuppressWarnings("java:S5413")
    public static Material[] randomColoredGlassPanes(int size){
        Material[] materials = new Material[size];
        List<Material> fetchList = new ArrayList<>(COLORED_GLASS_PANES);
        for(int i = 0; i < size; i++){
            if(fetchList.isEmpty()){
                fetchList = new ArrayList<>(COLORED_GLASS_PANES);
            }
            int randomIndex = ThreadLocalRandom.current().nextInt(fetchList.size());
            materials[i] = fetchList.remove(randomIndex);
        }
        return materials;
    }

    public static boolean isSign(Material type) {
        return SIGN_MATERIALS.contains(type);
    }

    public static List<Material> getMaterialList(Collection<String> strings) {
        return strings.stream().map(Material::getMaterial).toList();
    }
}
