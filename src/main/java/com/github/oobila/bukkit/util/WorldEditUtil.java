package com.github.oobila.bukkit.util;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.regions.Region;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.World;
import org.bukkit.util.BoundingBox;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorldEditUtil {

    public static final Object SCHEMATIC_FILE_EXTENSION = ".schem";

    public static BlockVector3 toBV3(Vector3 vector3) {
        return BlockVector3.at(
                vector3.getX(),
                vector3.getY(),
                vector3.getZ());
    }

    public static BoundingBox toBoundingBox(Region region) {
        return new BoundingBox(
                region.getMinimumPoint().getX(),
                region.getMinimumPoint().getY(),
                region.getMinimumPoint().getZ(),
                region.getMaximumPoint().getX(),
                region.getMaximumPoint().getY(),
                region.getMaximumPoint().getZ()
        );
    }

    public static boolean worldEditIsAvailable(World world) {
        WorldEdit worldEdit = WorldEdit.getInstance();
        if (worldEdit == null) {
            return false;
        }

        EditSession editSession = worldEdit.newEditSession(BukkitAdapter.adapt(world));
        try {
            BlockVector3 bv3 = BlockVector3.at(0, 320, 0);
            editSession.setBlock(bv3, editSession.getBlock(bv3));
            editSession.commit();
        } catch (Exception e) {
            return false;
        } finally {
            editSession.close();
        }

        return true;
    }
}
