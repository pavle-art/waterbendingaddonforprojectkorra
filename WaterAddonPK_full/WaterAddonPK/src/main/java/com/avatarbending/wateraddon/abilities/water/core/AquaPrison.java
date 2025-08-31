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

@AbilityDescription(name = "AquaPrison", elems = {AbilityDescriptionElement.WATER})
public class AquaPrison extends WaterBase {

    private long duration;

    public AquaPrison(Player player) {
        super(player);
        this.duration = cfg.getLong("abilities.water.Defaults.duration", 4000);

        BendingPlayer bp = BendingPlayer.getBendingPlayer(bender);
        if (bp == null || !bp.canBend(this)) return;
        start();
    }

    @Override public void progress() {
        if (!bender.isOnline() || bender.isDead()) { remove(); return; }

        for (Entity e : GeneralMethods.getEntitiesAroundPoint(bender.getLocation().add(bender.getLocation().getDirection().normalize().multiply(3)), 2.5)) {
            if (e instanceof LivingEntity && e != bender) {
                LivingEntity le = (LivingEntity)e;
                le.setVelocity(new Vector(0,0,0));
                le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int)duration/50, 10));
                le.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, (int)duration/50, 1));
                le.getWorld().spawnParticle(Particle.WATER_BUBBLE, le.getLocation(), 40, 1.0, 1.0, 1.0, 0.02);
            }
        }

        if (timeUp(duration)) { applyCooldown(); remove(); return; }
    }

    @Override public String getName() { return "AquaPrison"; }
    @Override public String getDescription() { return "Strong bubble trap that suspends an enemy in midair."; }
    @Override public String getInstructions() { return "Cast to imprison a single nearby enemy."; }
    @Override public long getCooldown() { return cooldown; }
}
