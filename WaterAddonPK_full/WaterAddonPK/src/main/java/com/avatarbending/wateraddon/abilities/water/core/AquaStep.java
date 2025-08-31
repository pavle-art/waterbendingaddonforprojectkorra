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

@AbilityDescription(name = "AquaStep", elems = {AbilityDescriptionElement.WATER})
public class AquaStep extends WaterBase {

    private long maxDuration;
    private double speedBoost;

    public AquaStep(Player player) {
        super(player);
        this.maxDuration = cfg.getLong("abilities.water.Defaults.duration", 3000);
        this.speedBoost = cfg.getDouble("abilities.water.Defaults.speed", 0.4);

        BendingPlayer bp = BendingPlayer.getBendingPlayer(bender);
        if (bp == null || !bp.canBend(this)) return;
        start();
    }

    private boolean onWater() {
        Block b = bender.getLocation().subtract(0,1,0).getBlock();
        return b.getType() == Material.WATER;
    }

    @Override
    public void progress() {
        if (!bender.isOnline() || bender.isDead()) { remove(); return; }

        if (!onWater()) { applyCooldown(); remove(); return; }

        Vector vel = bender.getVelocity();
        vel.setY(0.0);
        vel.add(bender.getLocation().getDirection().normalize().multiply(speedBoost));
        bender.setVelocity(vel);
        bender.getWorld().spawnParticle(Particle.DRIP_WATER, bender.getLocation(), 8, 0.4, 0.1, 0.4, 0.01);

        if (timeUp(maxDuration)) { applyCooldown(); remove(); return; }
    }

    @Override public String getName() { return "AquaStep"; }
    @Override public String getDescription() { return "Briefly run across water surfaces with increased speed."; }
    @Override public String getInstructions() { return "Use while on a water surface to surf."; }
    @Override public long getCooldown() { return cooldown; }
}
