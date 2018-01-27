package com.mengcraft.dexp;

import com.mengcraft.dexp.command.CommandDexp;
import com.mengcraft.dexp.command.IICommand;
import com.mengcraft.dexp.config.Config;
import com.mengcraft.dexp.listener.EventListener;
import com.mengcraft.dexp.util.ListUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class DoubleExp extends JavaPlugin {

    private Config config;
    private IICommand command;

    @Override
    public void onEnable() {
        config = new Config(this.getDataFolder());
        config.load();
        config.save();
        this.getServer().getPluginManager().registerEvents(new EventListener(config), this);
        command = new CommandDexp("dexp", this, config);
    }

    @Override
    public void onDisable() {
        config.save();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        return command.execute(sender, ListUtils.arrayList(args));
    }

}
