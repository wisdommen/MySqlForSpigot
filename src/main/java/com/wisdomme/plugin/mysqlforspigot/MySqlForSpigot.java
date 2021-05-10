package com.wisdomme.plugin.mysqlforspigot;

import org.bukkit.plugin.java.JavaPlugin;

public final class MySqlForSpigot extends JavaPlugin {
    private static MySqlForSpigot plugin;

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
        Metrics metrics = new Metrics(this, 11311);
    }

    @Override
    public void onDisable() {
    }

    public static MySqlForSpigot getInstance() {
        return plugin;
    }
}
