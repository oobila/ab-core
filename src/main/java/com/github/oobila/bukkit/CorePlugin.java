package com.github.oobila.bukkit;

import com.github.oobila.bukkit.entity.BehaviourScheduler;
import com.github.oobila.bukkit.gui.GuiListener;
import com.github.oobila.bukkit.gui.GuiManager;
import com.github.oobila.bukkit.itemstack.CustomItemStackListener;
import com.github.oobila.bukkit.persistence.caches.ConfigCache;
import com.github.oobila.bukkit.scheduling.JobScheduler;
import com.github.oobila.bukkit.util.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;


public class CorePlugin extends JavaPlugin {

    //CORE PLUGIN ICON (blue):  eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzRmZWM1Y2RjMmUyMjQwMWE0YjViYTM3NmZmZTY0NzI3OWVkM2Y5NGZkNzVmODA1NmQ5ODZmMzNjOTMxYWM1NSJ9fX0=
    private static final int SPIGOT_ID = 97898;
    private static final int METRICS_ID = 0;

    @Getter @Setter(AccessLevel.PRIVATE)
    private static CorePlugin instance;

    private static final ConfigCache<String, Object> config = new ConfigCache<>(
            "config.yml",
            String.class,
            Object.class
    );
    private static final ConfigCache<String, String> language = new ConfigCache<>(
            "language.yml",
            String.class,
            String.class
    );

    @Getter
    private JobScheduler jobScheduler;

    @Getter
    private BehaviourScheduler behaviourScheduler;

    @Override
    public void onEnable() {
        super.onEnable();
        setInstance(this);
        new UpdateChecker(this, SPIGOT_ID);

        //#### new persistence ####
        config.open(this);
        language.open(this);

        //#### metrics ####
        Metrics metrics = new Metrics(this, METRICS_ID);

        //#### services ####
        jobScheduler = new JobScheduler(this, 1);

        //#### register ####
        Bukkit.getServer().getPluginManager().registerEvents(new GuiListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new CustomItemStackListener(), this);

        //#### gui ####
        GuiManager.onEnable(this);
        new PreShutdownHook(this, GuiManager::closeAll);

        //#### plugin ####
        //schedule tasks
        try {
            behaviourScheduler = new BehaviourScheduler();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        BehaviourScheduler.resetInstance();
        super.onDisable();
    }

    public Economy getEconomy() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return null;
        }
        return rsp.getProvider();
    }

    public boolean hasEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        return rsp != null;
    }

    public void checkRequiredVersion(String requiredCoreVersion) {
        if(requiredCoreVersion != null && !requiredCoreVersion.isEmpty()){
            Version pluginVersion = new Version(getDescription().getVersion());
            if(pluginVersion.compareTo(new Version(requiredCoreVersion)) < 0){
                getLogger().log(Level.SEVERE, "This version of {0} plugin requires ABCoreLib v{1}", new String[]{getName(), requiredCoreVersion});
                Bukkit.shutdown();
            }
        }
    }

    public void checkForUpdate(Plugin plugin, int spigotId) {
        if(spigotId != 0){
            new UpdateChecker(plugin, spigotId);
        }
    }

}
