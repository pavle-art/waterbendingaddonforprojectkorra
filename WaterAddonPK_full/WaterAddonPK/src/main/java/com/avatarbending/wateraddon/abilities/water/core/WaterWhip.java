package com.avatarbending.wateraddon.abilities.water.core;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.bukkit.util.RayTraceResult;
import org.bukkit.Location;
import org.bukkit.Material;

import com.projectkorra.projectkorra.ability.AbilityDescription;
import com.projectkorra.projectkorra.ability.AbilityDescription.AbilityDescriptionElement;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.ability.WaterAbility;
import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.GeneralMethods;

@AbilityDescription(name = "WaterWhip", elems = {AbilityDescriptionElement.WATER})
public class WaterWhip extends WaterBase {

    private double range;
    private double damage;
    private double knockback;
    private int ticks;
    private long maxDuration;
    private Vector dir;

    public WaterWhip(Player player) {
        super(player);
        this.range = cfg.getDouble("abilities.water.WaterWhip.range", 12.0);
        this.damage = cfg.getDouble("abilities.water.WaterWhip.damage", 2.5);
        this.knockback = cfg.getDouble("abilities.water.WaterWhip.knockback", 0.6);
        this.cooldown = cfg.getLong("abilities.water.WaterWhip.cooldown", 3000);
        this.ticks = cfg.getInt("abilities.water.WaterWhip.ticks", 20);
        this.maxDuration = ticks * 50L;
        this.dir = bender.getEyeLocation().getDirection().normalize();

        if (!bPlayerCanBend()) return;
        start();
    }

    private boolean bPlayerCanBend() {
        BendingPlayer bp = BendingPlayer.getBendingPlayer(bender);
        if (bp == null) return false;
        if (!bp.canBend(this)) return false;
        return true;
    }

    @Override
    public void progress() {
        if (!bender.isOnline() || bender.isDead()) { remove(); return; }
        if (timeUp(maxDuration)) { applyCooldown(); remove(); return; }

        Location eye = bender.getEyeLocation();
        Vector step = dir.clone().multiply(0.7);
        Location pos = eye.clone();

        for (int i = 0; i < 16; i++) {
            pos.add(step);
            pos.getWorld().spawnParticle(Particle.WATER_SPLASH, pos, 4, 0.05, 0.05, 0.05, 0.02);
            pos.getWorld().playSound(pos, Sound.ENTITY_FISH_SWIM, 0.3f, 1.6f);

            // Hit entities
            for (Entity e : GeneralMethods.getEntitiesAroundPoint(pos, 1.0)) {
                if (e instanceof LivingEntity && e != bender) {
                    LivingEntity le = (LivingEntity) e;
                    DamageHandler.damageEntity(le, damage, this);
                    Vector kb = dir.clone().multiply(knockback);
                    kb.setY(Math.max(0.1, kb.getY()));
                    le.setVelocity(le.getVelocity().add(kb));
                    applyCooldown();
                    remove();
                    return;
                }
            }

            // Block collision
            if (pos.getBlock().getType().isSolid()) {
                applyCooldown();
                remove();
                return;
            }

            if (eye.distanceSquared(pos) > range * range) {
                applyCooldown();
                remove();
                return;
            }
        }
    }

    @Override
    public String getName() { return "WaterWhip"; }

    @Override
    public String getDescription() {
        return "Form a swift whip of water that lashes forward, dealing light damage and knockback.";
    }

    @Override
    public String getInstructions() {
        return "Left-click with WaterWhip bound to lash in the direction you're facing.";
    }

    @Override
    public boolean isHarmlessAbility() { return false; }

    @Override
    public long getCooldown() { return cooldown; }
}
