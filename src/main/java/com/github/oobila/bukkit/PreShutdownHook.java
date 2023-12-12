package com.github.oobila.bukkit;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.Plugin;

/**
 * Listener for when the "stop" command is used
 */
public class PreShutdownHook implements Listener {

    private static final String STOP_STRING = "stop";
    private static final String STOP_STRING2 = "/stop";

    private Plugin plugin;
    private Runnable runnable;

    public PreShutdownHook(Plugin plugin, Runnable runnable) {
        this.plugin = plugin;
        this.runnable = runnable;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onConsoleStop(ServerCommandEvent e) {
        if(e.getCommand().trim().equalsIgnoreCase(STOP_STRING) ||
                e.getCommand().trim().equalsIgnoreCase(STOP_STRING2)){
            runnable.run();
        }
    }

    @EventHandler
    public void onCommandStop(PlayerCommandPreprocessEvent e) {
        if(e.getMessage().trim().equalsIgnoreCase(STOP_STRING) ||
                e.getMessage().trim().equalsIgnoreCase(STOP_STRING2)){
            runnable.run();
        }
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent e) {
        if(e.getPlugin().getDescription().getDepend().contains("ABCore") ||
                e.getPlugin().getDescription().getSoftDepend().contains("ABCore")) {
            runnable.run();
        }
    }

}
