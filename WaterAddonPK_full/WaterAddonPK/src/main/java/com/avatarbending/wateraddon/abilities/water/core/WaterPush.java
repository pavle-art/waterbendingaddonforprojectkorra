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

@AbilityDescription(name = "WaterPush", elems = {AbilityDescriptionElement.WATER})
public class WaterPush extends WaterBase {

    private double range;
    private double strength;

    public WaterPush(Player player) {
        super(player);
        this.range = cfg.getDouble("abilities.water.Defaults.range", 8.0);
        this.strength = cfg.getDouble("abilities.water.Defaults.strength", 1.4);

        BendingPlayer bp = BendingPlayer.getBendingPlayer(bender);
        if (bp == null || !bp.canBend(this)) return;
        start();
    }

    @Override
    public void progress() {
        if (!bender.isOnline() || bender.isDead()) { remove(); return; }

        Location front = bender.getLocation().add(bender.getLocation().getDirection().normalize().multiply(1.2));
        for (Entity e : GeneralMethods.getEntitiesAroundPoint(front, range / 4.0)) {
            if (e instanceof LivingEntity && e != bender) {
                Vector kb = e.getLocation().toVector().subtract(bender.getLocation().toVector()).normalize().multiply(strength);
                kb.setY(0.4);
                e.setVelocity(kb);
                e.getWorld().spawnParticle(Particle.WATER_SPLASH, e.getLocation(), 8, 0.2, 0.2, 0.2, 0.02);
                e.getWorld().playSound(e.getLocation(), Sound.ENTITY_FISH_SWIM, 0.4f, 1.6f);
                applyCooldown();
                remove();
                return;
            }
        }

        if (timeUp(300)) { applyCooldown(); remove(); return; }
    }

    @Override public String getName() { return "WaterPush"; }
    @Override public String getDescription() { return "Push enemies away with a burst of water."; }
    @Override public String getInstructions() { return "Left-click to push nearby enemies away."; }
    @Override public long getCooldown() { return cooldown; }
}
