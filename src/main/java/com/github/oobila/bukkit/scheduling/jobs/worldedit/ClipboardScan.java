package com.github.oobila.bukkit.scheduling.jobs.worldedit;

import com.github.oobila.bukkit.scheduling.AsyncJob;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BaseBlock;

public class ClipboardScan extends AsyncJob {

    private Clipboard clipboard;
    private ClipboardScanVisitor visitor;

    public ClipboardScan(Clipboard clipboard, ClipboardScanVisitor visitor) {
        this.clipboard = clipboard;
        this.visitor = visitor;
    }

    @Override
    public void run() {
        BlockVector3 min = clipboard.getMinimumPoint();
        BlockVector3 max = clipboard.getMaximumPoint();

        for(int y = min.getY(); y <= max.getY(); y++) {
            for (int x = min.getX(); x <= max.getX(); x++) {
                for (int z = min.getZ(); z <= max.getZ(); z++) {
                    BlockVector3 bv3 = BlockVector3.at(x, y, z);
                    BaseBlock baseBlock = clipboard.getFullBlock(bv3);
                    visitor.visit(baseBlock, bv3, clipboard);
                }
            }
        }
    }

    public interface ClipboardScanVisitor {
        void visit(BaseBlock baseBlock, BlockVector3 blockVector3, Clipboard clipboard);
    }
}
