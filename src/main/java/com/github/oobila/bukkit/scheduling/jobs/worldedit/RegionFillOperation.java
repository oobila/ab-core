package com.github.oobila.bukkit.scheduling.jobs.worldedit;

import com.github.oobila.bukkit.scheduling.AsyncJob;
import com.github.oobila.bukkit.scheduling.jobs.MaterialPlacer;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.Material;
import org.bukkit.World;

public class RegionFillOperation extends AsyncJob {

    private World world;
    private Region region;
    private Material material;
    private int maxBlockBatchCount = 50;

    public RegionFillOperation(Region region, Material material, int maxBlockBatchCount) {
        this.world = BukkitAdapter.adapt(region.getWorld());
        this.region = region;
        this.material = material;
        this.maxBlockBatchCount = maxBlockBatchCount;
    }

    @Override
    public void run() {
        int i = 0;
        MaterialPlacer materialPlacer = new MaterialPlacer(world);
        for (BlockVector3 blockVector3 : region) {
            if(i >= maxBlockBatchCount){
                addChildJob(materialPlacer);
                materialPlacer = new MaterialPlacer(world);
                i = 0;
            }
            materialPlacer.addBlock(material, BukkitAdapter.adapt(world, blockVector3));
            i++;
        }
        addChildJob(materialPlacer);
    }

    @Override
    public String toString() {
        return "RegionFillOperation{" +
                "material=" + material.toString() +
                '}';
    }
}