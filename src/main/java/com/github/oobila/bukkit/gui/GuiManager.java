package com.github.oobila.bukkit.gui;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class GuiManager {

    private static Map<UUID, Map<String, Object>> playerSelectionData = new HashMap<>();
    static Plugin plugin;
    static Map<Player, Gui> lastOpenedGui = new HashMap<>();
    static Map<Player, Gui> openGuis = new HashMap<>();

    private GuiManager() {
    }

    public static void onEnable(Plugin plugin) {
        GuiManager.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(new InventoryInteractionListeners(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new InventoryCloseListener(), plugin);
    }

    public static void closeAll(){
        Set<Player> players = new HashSet<>(openGuis.keySet());
        players.forEach(HumanEntity::closeInventory);
    }

    public static void addPlayerSelectionData(Player player, String key, Object data) {
        playerSelectionData.computeIfAbsent(player.getUniqueId(), uuid -> new HashMap<>());
        playerSelectionData.get(player.getUniqueId()).put(key, data);
    }

    public static Object getPlayerSelectionData(Player player, String key) {
        return playerSelectionData.get(player.getUniqueId()).get(key);
    }

    public static void removePlayerSelectionData(Player player) {
        playerSelectionData.remove(player.getUniqueId());
    }
}
