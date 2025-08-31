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

@AbilityDescription(name = "Whirlpool", elems = {AbilityDescriptionElement.WATER})
public class Whirlpool extends WaterBase {

    private long duration;
    private double radius;
    private double pull;

    public Whirlpool(Player player) {
        super(player);
        this.duration = cfg.getLong("abilities.water.Defaults.duration", 4000);
        this.radius = cfg.getDouble("abilities.water.Defaults.range", 4.0);
        this.pull = cfg.getDouble("abilities.water.Defaults.strength", 0.7);

        BendingPlayer bp = BendingPlayer.getBendingPlayer(bender);
        if (bp == null || !bp.canBend(this)) return;
        start();
    }

    @Override
    public void progress() {
        if (!bender.isOnline() || bender.isDead()) { remove(); return; }
        Location center = bender.getLocation().add(0,1,0);
        for (Entity e : GeneralMethods.getEntitiesAroundPoint(center, radius)) {
            if (e instanceof LivingEntity && e != bender) {
                Vector toCenter = center.toVector().subtract(e.getLocation().toVector()).normalize().multiply(pull);
                toCenter.setY(0.1);
                e.setVelocity(e.getVelocity().add(toCenter));
                e.getWorld().spawnParticle(Particle.WATER_WAKE, e.getLocation(), 6, 0.2, 0.2, 0.2, 0.01);
            }
        }

        center.getWorld().spawnParticle(Particle.WATER_BUBBLE, center, 20, radius, 1.0, radius, 0.02);

        if (timeUp(duration)) { applyCooldown(); remove(); return; }
    }

    @Override public String getName() { return "Whirlpool"; }
    @Override public String getDescription() { return "Create a whirlpool pulling enemies inward."; }
    @Override public String getInstructions() { return "Activate to create an area-of-control around you."; }
    @Override public long getCooldown() { return cooldown; }
}
