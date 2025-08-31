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

@AbilityDescription(name = "RainDance", elems = {AbilityDescriptionElement.WATER})
public class RainDance extends WaterBase {

    private long duration;

    public RainDance(Player player) {
        super(player);
        this.duration = cfg.getLong("abilities.water.Defaults.duration", 15000);

        BendingPlayer bp = BendingPlayer.getBendingPlayer(bender);
        if (bp == null || !bp.canBend(this)) return;
        start();
    }

    @Override public void progress() {
        if (!bender.isOnline() || bender.isDead()) { remove(); return; }
        bender.getWorld().setStorm(true);
        Location center = bender.getLocation();
        for (Entity e : GeneralMethods.getEntitiesAroundPoint(center, 8.0)) {
            if (e instanceof LivingEntity) {
                LivingEntity le = (LivingEntity)e;
                if (le.getLocation().distance(center) < 6.0 && !le.equals(bender)) {
                    double newHealth = Math.min(le.getHealth() + 0.3, le.getMaxHealth());
                    le.setHealth(newHealth);
                    le.getWorld().spawnParticle(Particle.DRIP_WATER, le.getLocation(), 4, 0.2, 0.2, 0.2, 0.01);
                }
            }
        }

        if (timeUp(duration)) {
            bender.getWorld().setStorm(false);
            applyCooldown();
            remove();
            return;
        }
    }

    @Override public String getName() { return "RainDance"; }
    @Override public String getDescription() { return "Call heavy rain: buffs water abilities and heals allies slowly."; }
    @Override public String getInstructions() { return "Channel to bring rain for a period of time."; }
    @Override public long getCooldown() { return cooldown; }
}
