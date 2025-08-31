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

@AbilityDescription(name = "Purify", elems = {AbilityDescriptionElement.WATER})
public class Purify extends WaterBase {

    private long radius;

    public Purify(Player player) {
        super(player);
        this.radius = (long)cfg.getDouble("abilities.water.Defaults.range", 6.0);

        BendingPlayer bp = BendingPlayer.getBendingPlayer(bender);
        if (bp == null || !bp.canBend(this)) return;
        start();
    }

    @Override public void progress() {
        if (!bender.isOnline() || bender.isDead()) { remove(); return; }

        Location center = bender.getLocation();
        for (Entity e : GeneralMethods.getEntitiesAroundPoint(center, radius)) {
            if (e instanceof LivingEntity) {
                LivingEntity le = (LivingEntity)e;
                // Remove negative potion effects
                le.removePotionEffect(PotionEffectType.POISON);
                le.removePotionEffect(PotionEffectType.WITHER);
                le.removePotionEffect(PotionEffectType.BLINDNESS);
                le.removePotionEffect(PotionEffectType.SLOW_DIGGING);
                // Extinguish fire
                le.setFireTicks(0);
                le.getWorld().spawnParticle(Particle.SPELL, le.getLocation(), 8, 0.4, 0.4, 0.4, 0.02);
            }
        }

        applyCooldown();
        remove();
    }

    @Override public String getName() { return "Purify"; }
    @Override public String getDescription() { return "Cleanse allies of debuffs, negative effects, and fire."; }
    @Override public String getInstructions() { return "Use to remove debuffs from nearby allies."; }
    @Override public long getCooldown() { return cooldown; }
}
