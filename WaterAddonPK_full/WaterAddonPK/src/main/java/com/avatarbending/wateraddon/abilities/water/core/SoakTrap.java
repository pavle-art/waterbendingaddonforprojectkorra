package com.avatarbending.wateraddon.abilities.water.core;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.bukkit.block.Block;
import org.bukkit.Material;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.projectkorra.projectkorra.ability.AbilityDescription;
import com.projectkorra.projectkorra.ability.AbilityDescription.AbilityDescriptionElement;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.ability.AddonAbility;

@AbilityDescription(name = "SoakTrap", elems = {AbilityDescriptionElement.WATER})
public class SoakTrap extends WaterBase {

    private long duration;
    private double slowLevel;

    public SoakTrap(Player player) {
        super(player);
        this.duration = cfg.getLong("abilities.water.Defaults.duration", 6000);
        this.slowLevel = cfg.getDouble("abilities.water.Defaults.slow", 1.5);

        BendingPlayer bp = BendingPlayer.getBendingPlayer(bender);
        if (bp == null || !bp.canBend(this)) return;
        start();
    }

    @Override
    public void progress() {
        if (!bender.isOnline() || bender.isDead()) { remove(); return; }
        Location center = bender.getLocation();
        for (Entity e : GeneralMethods.getEntitiesAroundPoint(center, 4.0)) {
            if (e instanceof LivingEntity && e != bender) {
                LivingEntity le = (LivingEntity)e;
                le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, (int)slowLevel));
                le.getWorld().spawnParticle(Particle.WATER_DROP, le.getLocation(), 6, 0.2, 0.1, 0.2, 0.01);
            }
        }

        if (timeUp(duration)) { applyCooldown(); remove(); return; }
    }

    @Override public String getName() { return "SoakTrap"; }
    @Override public String getDescription() { return "Soak the ground, slowing and slipping foes."; }
    @Override public String getInstructions() { return "Place a soak trap to hinder enemy movement."; }
    @Override public long getCooldown() { return cooldown; }
}
