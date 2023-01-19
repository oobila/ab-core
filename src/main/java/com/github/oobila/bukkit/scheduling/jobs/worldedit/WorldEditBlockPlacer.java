package com.github.oobila.bukkit.scheduling.jobs.worldedit;

import com.github.oobila.bukkit.scheduling.Job;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BaseBlock;

import java.util.ArrayList;
import java.util.List;

public class WorldEditBlockPlacer extends Job {

    private World world;
    private List<BaseBlock> blockDataList = new ArrayList<>();
    private List<BlockVector3> locations = new ArrayList<>();

    WorldEditBlockPlacer(World world) {
        this.world = world;
    }

    void addBlock(BaseBlock baseBlock, BlockVector3 location){
        blockDataList.add(baseBlock);
        locations.add(location);
    }

    @Override
    public void run() {
        for(int i = 0; i < locations.size(); i++){
            try {
                world.setBlock(locations.get(i), blockDataList.get(i));
            } catch (WorldEditException e) {
                e.printStackTrace();
            }
        }
    }
}
