package com.github.oobila.bukkit.gui.objects;

import com.github.oobila.bukkit.gui.AbstractGui;
import com.github.oobila.bukkit.gui.Cell;
import org.bukkit.plugin.Plugin;

public abstract class SimpleGui extends AbstractGui {

    protected SimpleGui(Plugin plugin, String title, Cell[] cells) {
        super(plugin, title, cells);
    }

    protected SimpleGui(Plugin plugin, String title, BlockedCell blockedCell, Cell[] cells) {
        super(plugin, title, blockedCell, cells);
    }
}
