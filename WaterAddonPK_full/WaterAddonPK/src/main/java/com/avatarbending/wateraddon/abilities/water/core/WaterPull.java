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

@AbilityDescription(name = "WaterPull", elems = {AbilityDescriptionElement.WATER})
public class WaterPull extends WaterBase {

    private double range;
    private double strength;

    public WaterPull(Player player) {
        super(player);
        this.range = cfg.getDouble("abilities.water.Defaults.range", 10.0);
        this.strength = cfg.getDouble("abilities.water.Defaults.strength", 1.0);

        BendingPlayer bp = BendingPlayer.getBendingPlayer(bender);
        if (bp == null || !bp.canBend(this)) return;
        start();
    }

    @Override
    public void progress() {
        if (!bender.isOnline() || bender.isDead()) { remove(); return; }

        Location loc = bender.getLocation().add(0, 1.0, 0);
        for (Entity e : GeneralMethods.getEntitiesAroundPoint(loc, range)) {
            if (e == bender) continue;
            Vector to = loc.toVector().subtract(e.getLocation().toVector()).normalize();
            e.setVelocity(e.getVelocity().add(to.multiply(strength)).setY(Math.max(0.1, to.getY() * 0.6)));
            e.getWorld().spawnParticle(Particle.SPLASH, e.getLocation(), 4, 0.2, 0.2, 0.2, 0.01);
        }

        if (timeUp(1200)) { applyCooldown(); remove(); return; }
    }

    @Override public String getName() { return "WaterPull"; }
    @Override public String getDescription() { return "Pull entities or items toward you with a water stream."; }
    @Override public String getInstructions() { return "Left-click while bound to pull in nearby entities."; }
    @Override public long getCooldown() { return cooldown; }
}
