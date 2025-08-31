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

@AbilityDescription(name = "WaterBlast", elems = {AbilityDescriptionElement.WATER})
public class WaterBlast extends WaterBase {

    private double speed;
    private double range;
    private double damage;
    private Vector dir;
    private Location origin;

    public WaterBlast(Player player) {
        super(player);
        this.speed = cfg.getDouble("abilities.water.Defaults.speed", 1.6);
        this.range = cfg.getDouble("abilities.water.Defaults.range", 15.0);
        this.damage = cfg.getDouble("abilities.water.Defaults.damage", 2.0);
        this.origin = bender.getEyeLocation().clone();
        this.dir = bender.getEyeLocation().getDirection().normalize();

        BendingPlayer bp = BendingPlayer.getBendingPlayer(bender);
        if (bp == null || !bp.canBend(this)) return;
        start();
    }

    @Override
    public void progress() {
        if (!bender.isOnline() || bender.isDead()) { remove(); return; }

        origin.add(dir.clone().multiply(speed));
        origin.getWorld().spawnParticle(Particle.SPLASH, origin, 6, 0.1, 0.1, 0.1, 0.01);
        origin.getWorld().playSound(origin, Sound.ENTITY_DOLPHIN_SPLASH, 0.2f, 1.4f);

        for (Entity e : GeneralMethods.getEntitiesAroundPoint(origin, 1.0)) {
            if (e instanceof LivingEntity && e != bender) {
                LivingEntity le = (LivingEntity) e;
                DamageHandler.damageEntity(le, damage, this);
                Vector kb = dir.clone().multiply(0.6);
                kb.setY(0.2);
                le.setVelocity(le.getVelocity().add(kb));
                applyCooldown();
                remove();
                return;
            }
        }

        if (origin.getBlock().getType().isSolid() || bender.getEyeLocation().distance(origin) > range) {
            applyCooldown();
            remove();
            return;
        }
    }

    @Override public String getName() { return "WaterBlast"; }
    @Override public String getDescription() { return "Fire rapid water projectiles that scale with level."; }
    @Override public String getInstructions() { return "Left-click to fire a short-range water blast."; }
    @Override public long getCooldown() { return cooldown; }
}
