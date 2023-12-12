package com.github.oobila.bukkit.util;

import com.github.oobila.bukkit.util.enums.BlockColor;
import com.github.oobila.bukkit.util.enums.ColoredMaterialType;

import java.util.Objects;

class ColoredMaterialMeta {
    BlockColor blockColor;
    ColoredMaterialType type;

    public ColoredMaterialMeta(BlockColor blockColor, ColoredMaterialType type) {
        this.blockColor = blockColor;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ColoredMaterialMeta that = (ColoredMaterialMeta) o;
        return blockColor == that.blockColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(blockColor, type);
    }
}
