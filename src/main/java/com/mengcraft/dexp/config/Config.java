package com.mengcraft.dexp.config;

import com.mengcraft.dexp.util.ServerUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Config {

    private String lang = "en_us";
    private final Map<String, Double> vips = new LinkedHashMap<>();

    private final File file;
    private final LangKeys langKeys;
    private final YamlConfiguration config = new YamlConfiguration();

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

    public Map<String, Double> getVips() {
        return vips;
    }

    public void lang(String lang) {
        this.lang = lang;
    }

    public String lang() {
        return this.lang;
    }

}
