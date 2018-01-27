package com.mengcraft.dexp.listener;

import com.mengcraft.dexp.config.Config;
import com.mengcraft.dexp.util.ServerUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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
        if (config.getServerRatio() > 1) {
            event.setDroppedExp((int) (event.getDroppedExp() / config.getServerRatio()));
        }
    }

    @EventHandler
    public void onServerExp(PlayerExpChangeEvent event) {
        if (config.getServerExpUse()) {//getConfig.getBoolean("server.exp.use")
            long currentTime = System.currentTimeMillis();
            long setTime = config.getDeadline();//.getLong("server.exp.time");
            if (currentTime <= setTime) {
                int amount = Math.round(event.getAmount() * (float) config.getServerRatio());
                event.setAmount(amount);
                event.getPlayer().sendMessage(ChatColor.GREEN + "多倍经验活动中! 你获得 " + amount + " 点经验");
            } else {
                config.stopDxp();
                ServerUtils.broadcast(ChatColor.RED + "多倍经验活动已结束!");
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerExpChange(PlayerExpChangeEvent event) {
        Player player = event.getPlayer();
        Map<String, Double> vips = config.getVips();
        for (String vip : vips.keySet()) {
            if (player.hasPermission(vip)) {
                event.setAmount((int) (event.getAmount() * vips.get(vip)));
                return;
            }
        }
        event.setAmount((int) (event.getAmount() * config.getServerRatio()));
    }

}
