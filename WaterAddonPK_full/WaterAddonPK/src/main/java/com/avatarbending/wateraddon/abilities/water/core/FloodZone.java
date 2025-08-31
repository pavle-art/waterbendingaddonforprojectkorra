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

@AbilityDescription(name = "FloodZone", elems = {AbilityDescriptionElement.WATER})
public class FloodZone extends WaterBase {

    private long duration;
    private double radius;

    public FloodZone(Player player) {
        super(player);
        this.duration = cfg.getLong("abilities.water.Defaults.duration", 7000);
        this.radius = cfg.getDouble("abilities.water.Defaults.range", 6.0);

        BendingPlayer bp = BendingPlayer.getBendingPlayer(bender);
        if (bp == null || !bp.canBend(this)) return;
        start();
    }

    @Override public void progress() {
        if (!bender.isOnline() || bender.isDead()) { remove(); return; }
        Location center = bender.getLocation();
        for (double x = -radius; x <= radius; x += 1.0) {
            for (double z = -radius; z <= radius; z += 1.0) {
                Location l = center.clone().add(x, 0, z);
                l.getWorld().spawnParticle(Particle.SPLASH, l, 4, 0.2, 0.1, 0.2, 0.01);
            }
        }

        for (Entity e : GeneralMethods.getEntitiesAroundPoint(center, radius)) {
            if (e instanceof LivingEntity && e != bender) {
                ((LivingEntity)e).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 1));
            }
        }

        if (timeUp(duration)) { applyCooldown(); remove(); return; }
    }

    @Override public String getName() { return "FloodZone"; }
    @Override public String getDescription() { return "Waterlog the terrain, creating tactical water sources and slowing foes."; }
    @Override public String getInstructions() { return "Use to flood an area for battlefield control."; }
    @Override public long getCooldown() { return cooldown; }
}
