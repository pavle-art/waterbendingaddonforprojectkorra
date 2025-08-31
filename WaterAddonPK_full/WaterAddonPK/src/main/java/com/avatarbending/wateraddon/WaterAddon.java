package com.avatarbending.wateraddon;

import org.bukkit.plugin.java.JavaPlugin;
import com.projectkorra.projectkorra.ability.CoreAbility;

public class WaterAddon extends JavaPlugin {

    private static WaterAddon instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        // Register our abilities in this package
        CoreAbility.registerPluginAbilities(this, "com.avatarbending.wateraddon.abilities.water.core");
        getLogger().info("[WaterAddonPK] Enabled and registered Water abilities.");
    }

    @Override
    public void onDisable() {
        getLogger().info("[WaterAddonPK] Disabled.");
    }

    public static WaterAddon getInstance() {
        return instance;
    }
}
