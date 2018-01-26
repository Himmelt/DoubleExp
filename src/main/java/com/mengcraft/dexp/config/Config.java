package com.mengcraft.dexp.config;

import com.mengcraft.dexp.util.ServerUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Set;

public class Config {

    private String lang = "en_us";
    private int broadcastTime = 3;
    private boolean broadcastEnable = false;
    private double serverRatio = 1.0;
    private final HashMap<String, Double> vips = new HashMap<>();

    private final File file;
    private final LangKeys langKeys;
    private final YamlConfiguration config = new YamlConfiguration();
    private boolean serverExpUse;
    private long serverExpTime;


    public Config(File file) {
        this.file = new File(file, "config.yml");
        this.langKeys = new LangKeys(new File(file, "lang"));
    }

    public void load() {
        if (!file.exists()) {
            save();
            return;
        }
        try {
            config.load(file);
            lang = config.getString("lang");
            if (lang == null || lang.isEmpty()) {
                lang = "en_us";
            }
            broadcastEnable = config.getBoolean("broadcast.enable");
            broadcastTime = config.getInt("broadcast.time");
            serverRatio = config.getDouble("serverRatio");
            ConfigurationSection vipRatios = config.getConfigurationSection("vipRatios");
            if (vipRatios != null) {
                Set<String> keys = vipRatios.getKeys(false);
                if (keys != null && !keys.isEmpty()) {
                    for (String vip : keys) {
                        if (vip != null && !vip.isEmpty()) {
                            vips.put(vip, vipRatios.getDouble(vip, 1.0D));
                        }
                    }
                }
            }
        } catch (Throwable e) {
            ServerUtils.broadcast("config file load exception !!!");
        }
        langKeys.setLang(lang);
        langKeys.load();
    }

    public void save() {
        try {
            config.set("lang", lang);
            config.set("broadcast.enable", broadcastEnable);
            config.set("broadcast.time", broadcastTime);
            config.set("serverRatio", serverRatio);
            ConfigurationSection vipRatios = config.getConfigurationSection("vipRatios");
            for (String vip : vips.keySet()) {
                if (vip != null && !vip.isEmpty()) {
                    vipRatios.set(vip, vips.get(vip));
                }
            }
            config.save(file);
        } catch (Throwable e) {
            ServerUtils.broadcast("config file save exception !!!");
        }
    }

    public boolean broadcastEnable() {
        return this.broadcastEnable;
    }

    public HashMap<String, Double> getVips() {
        return vips;
    }

    public double getServerRatio() {
        return serverRatio;
    }

    public void stopDxp() {

    }

    public boolean getServerExpUse() {
        return serverExpUse;
    }

    public long getServerExpTime() {
        return serverExpTime;
    }

    public long broadcastTime() {
        return broadcastTime;
    }

    public void setServerExpUse(boolean serverExpUse) {
        this.serverExpUse = serverExpUse;
    }

    public void setServerExpTime(int serverExpTime) {
        this.serverExpTime = serverExpTime;
    }

    public void lang(String lang) {
        this.lang = lang;
    }

    public String lang() {
        return this.lang;
    }

}
