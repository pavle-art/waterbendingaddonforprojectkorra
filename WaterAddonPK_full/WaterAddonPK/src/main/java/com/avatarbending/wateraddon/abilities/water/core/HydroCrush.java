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

@AbilityDescription(name = "HydroCrush", elems = {AbilityDescriptionElement.WATER})
public class HydroCrush extends WaterBase {

    private long duration;

    public HydroCrush(Player player) {
        super(player);
        this.duration = cfg.getLong("abilities.water.Defaults.duration", 2000);

        BendingPlayer bp = BendingPlayer.getBendingPlayer(bender);
        if (bp == null || !bp.canBend(this)) return;
        start();
    }

    @Override public void progress() {
        if (!bender.isOnline() || bender.isDead()) { remove(); return; }

        for (Entity e : GeneralMethods.getEntitiesAroundPoint(bender.getLocation().add(bender.getLocation().getDirection().normalize().multiply(3)), 2.0)) {
            if (e instanceof LivingEntity && e != bender) {
                LivingEntity le = (LivingEntity)e;
                le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int)duration/50, 4));
                le.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, (int)duration/50, 1));
                le.getWorld().spawnParticle(Particle.CLOUD, le.getLocation(), 12, 0.4, 0.4, 0.4, 0.02);
                DamageHandler.damageEntity(le, 2.0, this);
                applyCooldown();
                remove();
                return;
            }
        }

        if (timeUp(500)) { applyCooldown(); remove(); return; }
    }

    @Override public String getName() { return "HydroCrush"; }
    @Override public String getDescription() { return "Encase a target in pressurized water to stun them briefly."; }
    @Override public String getInstructions() { return "Use to stun a single target in front of you."; }
    @Override public long getCooldown() { return cooldown; }
}
