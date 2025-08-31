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

@AbilityDescription(name = "WaterSpear", elems = {AbilityDescriptionElement.WATER})
public class WaterSpear extends WaterBase {

    private double damage;
    private double range;
    private Vector dir;
    private Location origin;

    public WaterSpear(Player player) {
        super(player);
        this.damage = cfg.getDouble("abilities.water.Defaults.damage", 4.0);
        this.range = cfg.getDouble("abilities.water.Defaults.range", 16.0);
        this.origin = bender.getEyeLocation().clone();
        this.dir = bender.getEyeLocation().getDirection().normalize();

        BendingPlayer bp = BendingPlayer.getBendingPlayer(bender);
        if (bp == null || !bp.canBend(this)) return;
        start();
    }

    @Override public void progress() {
        if (!bender.isOnline() || bender.isDead()) { remove(); return; }

        origin.add(dir.clone().multiply(1.2));
        origin.getWorld().spawnParticle(Particle.DRIP_WATER, origin, 6, 0.05, 0.05, 0.05, 0.01);
        origin.getWorld().playSound(origin, Sound.ITEM_TRIDENT_THROW, 0.2f, 1.6f);

        for (Entity e : GeneralMethods.getEntitiesAroundPoint(origin, 1.0)) {
            if (e instanceof LivingEntity && e != bender) {
                DamageHandler.damageEntity((LivingEntity)e, damage, this);
                e.setVelocity(dir.clone().multiply(0.7).setY(0.2));
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

    @Override public String getName() { return "WaterSpear"; }
    @Override public String getDescription() { return "Form and hurl a hardened spear of water."; }
    @Override public String getInstructions() { return "Left-click to throw a directed water spear."; }
    @Override public long getCooldown() { return cooldown; }
}
