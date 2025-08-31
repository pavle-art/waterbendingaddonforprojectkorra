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

@AbilityDescription(name = "AbyssGrip", elems = {AbilityDescriptionElement.WATER})
public class AbyssGrip extends WaterBase {

    private long duration;
    private double pull;

    public AbyssGrip(Player player) {
        super(player);
        this.duration = cfg.getLong("abilities.water.Defaults.duration", 3500);
        this.pull = cfg.getDouble("abilities.water.Defaults.strength", 1.2);

        BendingPlayer bp = BendingPlayer.getBendingPlayer(bender);
        if (bp == null || !bp.canBend(this)) return;
        start();
    }

    @Override public void progress() {
        if (!bender.isOnline() || bender.isDead()) { remove(); return; }
        Location center = bender.getLocation().add(bender.getLocation().getDirection().normalize().multiply(4)).add(0,0.5,0);
        for (Entity e : GeneralMethods.getEntitiesAroundPoint(center, 3.0)) {
            if (e instanceof LivingEntity && e != bender) {
                e.setVelocity(new Vector(0, -0.7, 0));
                DamageHandler.damageEntity((LivingEntity)e, 2.5, this);
                e.getWorld().spawnParticle(Particle.SPELL_WITCH, e.getLocation(), 10, 0.3, 0.3, 0.3, 0.02);
            }
        }

        if (timeUp(duration)) { applyCooldown(); remove(); return; }
    }

    @Override public String getName() { return "AbyssGrip"; }
    @Override public String getDescription() { return "Drag enemies into a crushing watery void beneath them."; }
    @Override public String getInstructions() { return "Use to yank foes downward and damage them."; }
    @Override public long getCooldown() { return cooldown; }
}
