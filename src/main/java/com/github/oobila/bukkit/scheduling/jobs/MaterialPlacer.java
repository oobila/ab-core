package com.github.oobila.bukkit.scheduling.jobs;

import com.github.oobila.bukkit.scheduling.Job;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class MaterialPlacer extends Job {

    private World world;
    private List<Material> materialList = new ArrayList<>();
    private List<Location> locations = new ArrayList<>();

    public MaterialPlacer(World world) {
        this.world = world;
    }

    public void addBlock(Material material, Location location){
        materialList.add(material);
        locations.add(location);
    }

    @Override
    public void run() {
        for(int i = 0; i < locations.size(); i++){
            world.getBlockAt(locations.get(i)).setType(materialList.get(i));
        }
    }

    @Override
    public String toString() {
        return "MaterialPlacer{}";
    }
}
