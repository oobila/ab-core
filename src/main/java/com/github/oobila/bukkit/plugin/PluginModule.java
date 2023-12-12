package com.github.oobila.bukkit.plugin;

import org.bukkit.plugin.Plugin;

public abstract class PluginModule {

    Plugin plugin;

    public PluginModule(Plugin plugin) {
        this.plugin = plugin;
        ModuleManager.register(this);
    }

    public PluginModule enable() {
        onEnable(plugin);
        return this;
    }

    public PluginModule disable() {
        onDisable(plugin);
        return this;
    }

    protected abstract void onEnable(Plugin plugin);

    protected abstract void onDisable(Plugin plugin);

}
