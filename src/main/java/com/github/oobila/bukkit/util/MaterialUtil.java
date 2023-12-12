package com.github.oobila.bukkit.util;

import com.github.oobila.bukkit.util.enums.BlockColor;
import com.github.oobila.bukkit.util.enums.ColoredMaterialType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
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
        return (Material[]) Stream.generate(() -> randomColoredBlock(type))
                .limit(size)
                .toArray();
    }

    public static Material getColoredMaterial(BlockColor blockColor, ColoredMaterialType type){
        return coloredMaterials.get(new ColoredMaterialMeta(blockColor, type));
    }

    public static Material getColoredMaterial(ChatColor chatColor, ColoredMaterialType type){
        return coloredMaterials.get(new ColoredMaterialMeta(BlockColor.get(chatColor), type));
    }

    // #### SIGNS ####
    public static final List<Material> SIGN_MATERIALS = Arrays.stream(Material.values())
            .filter(material -> material.name().toLowerCase().endsWith("sign"))
            .toList();

    public static boolean isSign(Material type) {
        return SIGN_MATERIALS.contains(type);
    }

    // #### TRANSPARENT ####
    public static final List<Material> TRANSPARENT_MATERIALS = Arrays.stream(Material.values())
            .filter(material -> !material.isSolid())
            .toList();
    public static final Set<Material> TRANSPARENT_MATERIAL_SET = TRANSPARENT_MATERIALS.stream().collect(Collectors.toSet());

}
