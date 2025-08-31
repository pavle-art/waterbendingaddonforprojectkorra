package com.avatarbending.wateraddon.abilities.water.core;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.projectkorra.projectkorra.ability.AbilityDescription;
import com.projectkorra.projectkorra.ability.AbilityDescription.AbilityDescriptionElement;

@AbilityDescription(name = "TidalRush", elems = {AbilityDescriptionElement.WATER})
public class TidalRush extends WaterBase {

    private double speed;
    private long maxDuration;

    public TidalRush(Player player) {
        super(player);
        this.speed = cfg.getDouble("abilities.water.TidalRush.speed", 0.7);
        this.maxDuration = cfg.getLong("abilities.water.TidalRush.max-duration", 4500);
        this.cooldown = cfg.getLong("abilities.water.TidalRush.cooldown", 8000);
        start();
    }

    @Override
    public void progress() {
        if (!bender.isOnline() || bender.isDead()) { remove(); return; }
        if (timeUp(maxDuration)) { applyCooldown(); remove(); return; }

        Vector dir = bender.getLocation().getDirection().normalize().multiply(speed);
        bender.setVelocity(bender.getVelocity().add(dir));

        Location l = bender.getLocation();
        l.getWorld().spawnParticle(Particle.WATER_SPLASH, l, 15, 0.4, 0.1, 0.4, 0.02);
        l.getWorld().playSound(l, Sound.BLOCK_WATER_AMBIENT, 0.6f, 1.4f);
    }

    @Override
    public String getName() { return "TidalRush"; }

    @Override
    public String getDescription() {
        return "Surf forward on a rushing tide, gaining quick mobility.";
    }

    @Override
    public String getInstructions() {
        return "Tap sneak to surge forward; movement keys steer while active.";
    }

    @Override
    public boolean isHarmlessAbility() { return true; }

    @Override
    public long getCooldown() { return cooldown; }
}
