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

@AbilityDescription(name = "BubblePrison", elems = {AbilityDescriptionElement.WATER})
public class BubblePrison extends WaterBase {

    private long duration;

    public BubblePrison(Player player) {
        super(player);
        this.duration = cfg.getLong("abilities.water.Defaults.duration", 3000);
        BendingPlayer bp = BendingPlayer.getBendingPlayer(bender);
        if (bp == null || !bp.canBend(this)) return;
        start();
    }

    @Override
    public void progress() {
        if (!bender.isOnline() || bender.isDead()) { remove(); return; }

        for (Entity e : GeneralMethods.getEntitiesAroundPoint(bender.getLocation().add(bender.getLocation().getDirection().normalize().multiply(2)), 2.0)) {
            if (e instanceof LivingEntity && e != bender) {
                LivingEntity le = (LivingEntity)e;
                le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int)duration/50, 3));
                le.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, (int)duration/50, 2));
                le.setVelocity(new Vector(0,0,0));
                le.getWorld().spawnParticle(Particle.WATER_BUBBLE, le.getLocation(), 30, 1.0, 1.0, 1.0, 0.02);
            }
        }

        if (timeUp(duration)) { applyCooldown(); remove(); return; }
    }

    @Override public String getName() { return "BubblePrison"; }
    @Override public String getDescription() { return "Encapsulate a target in a water bubble, limiting movement and vision."; }
    @Override public String getInstructions() { return "Bind and cast to trap a nearby enemy in a bubble."; }
    @Override public long getCooldown() { return cooldown; }
}
