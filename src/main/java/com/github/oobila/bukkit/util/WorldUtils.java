package com.github.oobila.bukkit.util;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class WorldUtils {

    public static Block getBlockAtDistance(LivingEntity entity, int range, boolean stop_at_solid) {
        BlockIterator iterator = new BlockIterator(entity, range);
        while (iterator.hasNext()) {
            Block b = iterator.next();
            if (iterator.hasNext()) {
                if (stop_at_solid) {
                    if (MaterialUtil.TRANSPARENT_MATERIALS.contains(b.getType())) {
                        continue;
                    } else {
                        return b;
                    }
                } else {
                    continue;
                }
            } else {
                return b;
            }
        }
        return null;
    }

    public static List<Location> getLine(Location start, double range, int locations_between) {
        Vector dir = start.getDirection();
        double step = range/locations_between;
        List<Location> temp = new ArrayList<>();

        for (int i=0; i < locations_between; i++) {
            temp.add(dir.add(dir.clone().normalize().multiply(step)).toLocation(start.getWorld()));
        }

        return temp;
    }
}
