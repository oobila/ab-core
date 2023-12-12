package com.github.oobila.bukkit.plugin;

import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleManager {

    private static Map<Plugin, List<PluginModule>> modules = new HashMap<>();

    public static void register(PluginModule pluginModule) {
        modules.putIfAbsent(pluginModule.plugin, new ArrayList<>());
        modules.get(pluginModule.plugin).add(pluginModule);
    }

    public static boolean isEnabled(Class<? extends PluginModule> moduleClass) {
        for(Map.Entry<Plugin, List<PluginModule>> entry : modules.entrySet()) {
            for (PluginModule module : entry.getValue()) {
                if (moduleClass.isAssignableFrom(module.getClass())) {
                    return true;
                }
            }
        }
        return false;
    }
}
