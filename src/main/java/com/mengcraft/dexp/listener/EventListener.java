package com.mengcraft.dexp.listener;

import com.mengcraft.dexp.config.Config;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;

import java.util.Map;

public class EventListener implements Listener {

    private final Config config;
    private static final String PERM_HEAD = "dexp.";

    public EventListener(Config config) {
        this.config = config;
    }

    @EventHandler
    public void onDeathDropExp(PlayerDeathEvent event) {
        if (config.getEnable()) {
            Player player = event.getEntity();
            Map<String, Double> vips = config.getVips();
            for (String vip : vips.keySet()) {
                if (player.hasPermission(PERM_HEAD + vip)) {
                    double ratio = vips.get(vip);
                    if (ratio > 1) {
                        int droppedExp = event.getDroppedExp();
                        event.setDroppedExp((int) (droppedExp / ratio));
                    }
                    return;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerExpChange(PlayerExpChangeEvent event) {
        if (config.getEnable()) {
            Player player = event.getPlayer();
            Map<String, Double> vips = config.getVips();
            for (String vip : vips.keySet()) {
                if (player.hasPermission(PERM_HEAD + vip)) {
                    event.setAmount((int) (event.getAmount() * vips.get(vip)));
                    return;
                }
            }
        }
    }

}
