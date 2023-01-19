package com.github.oobila.bukkit;

import com.github.oobila.bukkit.entity.BehaviourScheduler;
import com.github.oobila.bukkit.events.PreShutdownHook;
import com.github.oobila.bukkit.gui.GuiManager;
import com.github.oobila.bukkit.scheduling.JobScheduler;
import com.github.oobila.bukkit.sidecar.config.ConfigLoader;
import com.github.oobila.bukkit.sidecar.config.PluginConfig;
import com.github.oobila.bukkit.sidecar.persistence.DataLoader;
import com.github.oobila.bukkit.util.Version;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.util.Map;
import java.util.logging.Level;


public class CorePlugin extends JavaPlugin {

    //CORE PLUGIN ICON (blue):  eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzRmZWM1Y2RjMmUyMjQwMWE0YjViYTM3NmZmZTY0NzI3OWVkM2Y5NGZkNzVmODA1NmQ5ODZmMzNjOTMxYWM1NSJ9fX0=
    private static final int SPIGOT_ID = 97898;

    @PluginConfig(path = "language.yml")
    @Getter
    private static Map<String, String> language;

    @Getter
    private static CorePlugin instance;

    @Getter
    private JobScheduler jobScheduler;

    private Economy economy = null;
    private BehaviourScheduler behaviourScheduler = null;

    // needed for MockBukkit
    public CorePlugin() {
        super();
    }

    // needed for MockBukkit
    protected CorePlugin(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }

    @Override
    @SuppressWarnings("java:S2696")
    public void onEnable() {
        super.onEnable();
        CorePlugin.instance = this;
        new UpdateChecker(this, SPIGOT_ID);

        //#### config ####
        ConfigLoader.load(this);

        //#### persistence ####
        DataLoader.load(this);

        //#### scheduling ####
        jobScheduler = new JobScheduler(this, 1);

        //#### gui ####
        GuiManager.onEnable(this);
        new PreShutdownHook(this, GuiManager::closeAll);

        //#### plugin ####
        //economy
        setupEconomy();

        //schedule tasks
        try {
            behaviourScheduler = new BehaviourScheduler();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        //#### persistence ####
        DataLoader.save(this);

        behaviourScheduler.resetInstance();
        super.onDisable();
    }

    public Economy getEconomy() {
        return economy;
    }

    public boolean hasEconomy() {
        return economy != null;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
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
