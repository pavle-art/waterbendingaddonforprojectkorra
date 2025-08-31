package com.avatarbending.wateraddon.abilities.water.core;

import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import com.projectkorra.projectkorra.ability.*;
import com.projectkorra.projectkorra.GeneralMethods;
import com.avatarbending.wateraddon.WaterAddon;

public abstract class WaterBase extends WaterAbility implements AddonAbility {

    protected final Player bender;
    protected final FileConfiguration cfg;
    protected long startTime;
    protected long cooldown;

    public WaterBase(Player player) {
        super(player);
        this.bender = player;
        this.cfg = WaterAddon.getInstance().getConfig();
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public String getAuthor() { return "Pavle, GPT-5 Thinking"; }

    @Override
    public String getVersion() { return "1.0.0"; }

    @Override
    public void load() { }

    @Override
    public void stop() { }

    @Override
    public boolean isHarmlessAbility() { return false; } // override per ability

    @Override
    public boolean isExplosiveAbility() { return false; }

    @Override
    public Location getLocation() { return bender.getLocation(); }

    protected boolean timeUp(long maxDurationMs) {
        return System.currentTimeMillis() - startTime > maxDurationMs;
    }

    protected void applyCooldown() {
        if (cooldown > 0) {
            bender.setCooldown(bender.getInventory().getItemInMainHand().getType(), 0); // visual only (optional)
            this.bplayer.addCooldown(this);
        }
    }

    @Override
    public void remove() {
        super.remove();
    }
}
