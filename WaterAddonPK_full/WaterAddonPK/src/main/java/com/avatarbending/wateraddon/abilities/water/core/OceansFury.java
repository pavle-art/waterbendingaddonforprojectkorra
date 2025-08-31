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

@AbilityDescription(name = "OceansFury", elems = {AbilityDescriptionElement.WATER})
public class OceansFury extends WaterBase {

    private double range;
    private double width;
    private double force;

    public OceansFury(Player player) {
        super(player);
        this.range = cfg.getDouble("abilities.water.Defaults.range", 20.0);
        this.width = cfg.getDouble("abilities.water.Defaults.width", 6.0);
        this.force = cfg.getDouble("abilities.water.Defaults.force", 1.6);

        BendingPlayer bp = BendingPlayer.getBendingPlayer(bender);
        if (bp == null || !bp.canBend(this)) return;
        start();
    }

    @Override public void progress() {
        if (!bender.isOnline() || bender.isDead()) { remove(); return; }
        Location origin = bender.getLocation().add(0,1,0);
        Vector dir = bender.getLocation().getDirection().normalize();
        for (double d = 1.0; d <= range; d += 1.0) {
            Location p = origin.clone().add(dir.clone().multiply(d));
            for (double w = -width/2.0; w <= width/2.0; w += 1.0) {
                Location q = p.clone().add(dir.clone().crossProduct(new Vector(0,1,0)).normalize().multiply(w));
                q.getWorld().spawnParticle(Particle.WATER_SPLASH, q, 8, 0.4, 1.0, 0.4, 0.02);
            }

            for (Entity e : GeneralMethods.getEntitiesAroundPoint(p, width/2.0)) {
                if (e instanceof LivingEntity && e != bender) {
                    e.setVelocity(dir.clone().multiply(force).setY(0.8));
                    DamageHandler.damageEntity((LivingEntity)e, 3.0, this);
                }
            }
        }

        applyCooldown();
        remove();
    }

    @Override public String getName() { return "OceansFury"; }
    @Override public String getDescription() { return "Unleash a massive tidal wave that sweeps across the battlefield."; }
    @Override public String getInstructions() { return "Channel and release to send a sweeping tidal wave."; }
    @Override public long getCooldown() { return cooldown; }
}
