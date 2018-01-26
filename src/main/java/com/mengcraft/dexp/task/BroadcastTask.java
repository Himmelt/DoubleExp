package com.mengcraft.dexp.task;

import com.mengcraft.dexp.config.Config;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class BroadcastTask extends BukkitRunnable {

    private final Config config;

    private static BroadcastTask task;

    public BroadcastTask(Config config) {
        this.config = config;
    }

    public void run() {

    }

    public static void runNewTask(Plugin plugin, Config config) {
        if (task != null) {
            task.cancel();
        }
        task = new BroadcastTask(config);
        task.runTaskTimer(plugin, config.broadcastTime(), config.broadcastTime());
    }
}
