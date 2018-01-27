package com.mengcraft.dexp.listener;

import com.mengcraft.dexp.config.Config;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;

import java.util.Map;

public class EventListener implements Listener {

    private final Config config;

    public EventListener(Config config) {
        this.config = config;
    }

    @EventHandler
    public void onDeathDropExp(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Map<String, Double> vips = config.getVips();
        for (String vip : vips.keySet()) {
            if (player.hasPermission(vip)) {
                double ratio = vips.get(vip);
                if (ratio > 1) {
                    int droppedExp = event.getDroppedExp();
                    event.setDroppedExp((int) (droppedExp / ratio));
                }
                return;
            }
        }
    }

    @EventHandler
    public void onPlayerExpChange(PlayerExpChangeEvent event) {
        Player player = event.getPlayer();
        Map<String, Double> vips = config.getVips();
        for (String vip : vips.keySet()) {
            if (player.hasPermission(vip)) {
                event.setAmount((int) (event.getAmount() * vips.get(vip)));
                return;
            }
        }
    }

}
