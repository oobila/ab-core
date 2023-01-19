package com.github.oobila.bukkit.gui;

import lombok.Getter;
import org.bukkit.plugin.Plugin;

@Getter
public abstract class Cell implements GuiCell {

    private Plugin plugin;
    private GuiBase gui;
    private int position;

    void onCellAdd(Plugin plugin, GuiBase gui, int position) {
        this.plugin = plugin;
        this.gui = gui;
        this.position = position;
    }

}