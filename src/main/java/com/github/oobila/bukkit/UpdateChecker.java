package com.github.oobila.bukkit;

import com.github.oobila.bukkit.util.Version;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.logging.Level;

class UpdateChecker {

    private static final String UPDATE_TEXT = ChatColor.WHITE + "An update is available! " +
    ChatColor.DARK_PURPLE + "v{0}" + ChatColor.WHITE + ". The server is running " + ChatColor.DARK_PURPLE + "v{1}";

    private final Plugin plugin;
    private final int resourceId;
    private final String pluginName;
    private boolean newVersionAvailable = false;
    private String newVersion;

    UpdateChecker(Plugin plugin, int resourceId) {
        this.plugin = plugin;
        this.resourceId = resourceId;
        this.pluginName = ChatColor.GOLD + plugin.getName();

        getVersion(version -> {
            if(new Version(plugin.getDescription().getVersion()).compareTo(new Version(version)) < 0){
                newVersionAvailable = true;
                newVersion = version;
                plugin.getLogger().log(
                        Level.WARNING,
                        MessageFormat.format(ChatColor.stripColor(UPDATE_TEXT), version, plugin.getDescription().getVersion()));
                plugin.getServer().getPluginManager().registerEvents(new JoinEvent(), plugin);
            }
        });
    }

    void getVersion(final Consumer<String> consumer, Plugin plugin, int resourceId) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (InputStream inputStream = new URL(
                    "https://api.spigotmc.org/legacy/update.php?resource=" + resourceId).openStream();
                 Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    consumer.accept(scanner.next());
                }
            } catch (IOException exception) {
                plugin.getLogger().info("Unable to check for updates: " + exception.getMessage());
            }
        });
    }

    void getVersion(final Consumer<String> consumer) {
        getVersion(consumer, this.plugin, this.resourceId);
    }

    class JoinEvent implements Listener {

        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent event){
            if(newVersionAvailable && event.getPlayer().isOp()){
                Player p = event.getPlayer();
                p.sendMessage(pluginName + " " + MessageFormat.format(UPDATE_TEXT, newVersion, plugin.getDescription().getVersion()));
            }
        }

    }

}
