package com.github.oobila.bukkit.scheduling;

import org.bukkit.Bukkit;

public abstract class AsyncJob extends Job {

    @Override
    void runJob() {
        hasStarted = true;
        Bukkit.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            run();
            notifyObservers();
            isComplete = true;
        });
    }
}
