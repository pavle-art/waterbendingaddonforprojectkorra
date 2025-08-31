package com.avatarbending.wateraddon.abilities.water.core;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.block.Block;

import com.projectkorra.projectkorra.ability.AbilityDescription;
import com.projectkorra.projectkorra.ability.AbilityDescription.AbilityDescriptionElement;

@AbilityDescription(name = "HealingWaters", elems = {AbilityDescriptionElement.WATER})
public class HealingWaters extends WaterBase {

    private double healPerTick;
    private long interval;
    private double rainBoost;
    private long nextTick;

    public HealingWaters(Player player) {
        super(player);
        this.healPerTick = cfg.getDouble("abilities.water.HealingWaters.heal-per-tick", 0.5);
        this.interval = cfg.getLong("abilities.water.HealingWaters.interval", 800);
        this.rainBoost = cfg.getDouble("abilities.water.HealingWaters.in-rain-boost", 1.5);
        this.cooldown = cfg.getLong("abilities.water.HealingWaters.cooldown", 4000);
        this.nextTick = System.currentTimeMillis();

        start();
    }

    private boolean isInWater(Player p) {
        Block b = p.getLocation().getBlock();
        Material t = b.getType();
        return t == Material.WATER || b.getRelative(0, -1, 0).getType() == Material.WATER;
    }

    @Override
    public void progress() {
        if (!bender.isOnline() || bender.isDead()) { remove(); return; }

        if (!isInWater(bender) && !bender.getWorld().hasStorm()) {
            // End if not in water nor raining
            applyCooldown();
            remove();
            return;
        }

        if (System.currentTimeMillis() >= nextTick) {
            double amount = healPerTick;
            if (bender.getWorld().hasStorm()) amount *= rainBoost;
            double newHealth = Math.min(bender.getHealth() + amount, bender.getMaxHealth());
            bender.setHealth(newHealth);

            Location l = bender.getLocation().add(0, 1, 0);
            l.getWorld().spawnParticle(Particle.HEART, l, 2, 0.4, 0.6, 0.4, 0.01);
            l.getWorld().playSound(l, Sound.BLOCK_WATER_AMBIENT, 0.6f, 1.2f);

            nextTick = System.currentTimeMillis() + interval;
        }
    }

    @Override
    public String getName() { return "HealingWaters"; }

    @Override
    public String getDescription() {
        return "Channel restorative water to slowly heal yourself, stronger during rain.";
    }

    @Override
    public String getInstructions() {
        return "Stand in water or in the rain with the ability active to heal over time.";
    }

    @Override
    public boolean isHarmlessAbility() { return true; }

    @Override
    public long getCooldown() { return cooldown; }
}
