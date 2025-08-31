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

@AbilityDescription(name = "SurgeWave", elems = {AbilityDescriptionElement.WATER})
public class SurgeWave extends WaterBase {

    private double range;
    private double width;
    private double knockback;
    private Location origin;
    private Vector dir;

    public SurgeWave(Player player) {
        super(player);
        this.range = cfg.getDouble("abilities.water.Defaults.range", 12.0);
        this.width = cfg.getDouble("abilities.water.Defaults.width", 2.5);
        this.knockback = cfg.getDouble("abilities.water.Defaults.knockback", 1.2);
        this.origin = bender.getLocation().clone().add(0,1,0);
        this.dir = bender.getLocation().getDirection().normalize();

        BendingPlayer bp = BendingPlayer.getBendingPlayer(bender);
        if (bp == null || !bp.canBend(this)) return;
        start();
    }

    @Override
    public void progress() {
        if (!bender.isOnline() || bender.isDead()) { remove(); return; }

        for (double d = 0.5; d <= range; d += 0.7) {
            Location point = origin.clone().add(dir.clone().multiply(d));
            point.getWorld().spawnParticle(Particle.SPLASH, point, 8, width, 0.6, width, 0.02);
            for (Entity e : GeneralMethods.getEntitiesAroundPoint(point, width)) {
                if (e instanceof LivingEntity && e != bender) {
                    Vector kb = dir.clone().multiply(knockback);
                    kb.setY(0.6);
                    e.setVelocity(kb);
                    ((LivingEntity)e).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 1));
                    DamageHandler.damageEntity((LivingEntity)e, 1.5, this);
                }
            }
        }

        applyCooldown();
        remove();
    }

    @Override public String getName() { return "SurgeWave"; }
    @Override public String getDescription() { return "Send a forward wave that knocks enemies down."; }
    @Override public String getInstructions() { return "Hold and release to fire a surge wave."; }
    @Override public long getCooldown() { return cooldown; }
}
