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

@AbilityDescription(name = "WaterGeyser", elems = {AbilityDescriptionElement.WATER})
public class WaterGeyser extends WaterBase {

    private double force;

    public WaterGeyser(Player player) {
        super(player);
        this.force = cfg.getDouble("abilities.water.Defaults.force", 1.2);

        BendingPlayer bp = BendingPlayer.getBendingPlayer(bender);
        if (bp == null || !bp.canBend(this)) return;
        start();
    }

    @Override
    public void progress() {
        if (!bender.isOnline() || bender.isDead()) { remove(); return; }

        for (Entity e : GeneralMethods.getEntitiesAroundPoint(bender.getLocation().add(bender.getLocation().getDirection().normalize().multiply(3)), 2.0)) {
            if (e instanceof LivingEntity && e != bender) {
                e.setVelocity(new Vector(0, force, 0));
                e.getWorld().spawnParticle(Particle.WATER_SPLASH, e.getLocation(), 12, 0.5, 0.5, 0.5, 0.02);
                e.getWorld().playSound(e.getLocation(), Sound.ENTITY_DOLPHIN_JUMP, 0.5f, 1.3f);
                DamageHandler.damageEntity((LivingEntity)e, 1.0, this);
                applyCooldown();
                remove();
                return;
            }
        }

        if (timeUp(300)) { applyCooldown(); remove(); return; }
    }

    @Override public String getName() { return "WaterGeyser"; }
    @Override public String getDescription() { return "Erupt water under enemies, launching them into the air."; }
    @Override public String getInstructions() { return "Use near enemies to send them skyward."; }
    @Override public long getCooldown() { return cooldown; }
}
