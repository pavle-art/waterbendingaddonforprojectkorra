package com.avatarbending.wateraddon.abilities.water.core;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AbilityDescription;
import com.projectkorra.projectkorra.ability.AbilityDescription.AbilityDescriptionElement;
import com.projectkorra.projectkorra.BendingPlayer;

@AbilityDescription(name = "WaterShield", elems = {AbilityDescriptionElement.WATER})
public class WaterShield extends WaterBase {

    private long duration;
    private double radius;

    public WaterShield(Player player) {
        super(player);
        this.duration = cfg.getLong("abilities.water.WaterShield.duration", 5000);
        this.radius = cfg.getDouble("abilities.water.WaterShield.radius", 3.0);
        this.cooldown = cfg.getLong("abilities.water.WaterShield.cooldown", 7000);

        BendingPlayer bp = BendingPlayer.getBendingPlayer(bender);
        if (bp == null || !bp.canBend(this)) return;
        start();
    }

    @Override
    public void progress() {
        if (!bender.isOnline() || bender.isDead()) { remove(); return; }
        if (timeUp(duration)) { applyCooldown(); remove(); return; }

        Location center = bender.getLocation().add(0, 1.2, 0);
        center.getWorld().spawnParticle(Particle.WATER_BUBBLE, center, 25, radius, 1.0, radius, 0.02);
        center.getWorld().playSound(center, Sound.BLOCK_BUBBLE_COLUMN_UPWARDS_INSIDE, 0.3f, 1.2f);

        for (Entity e : GeneralMethods.getEntitiesAroundPoint(center, radius)) {
            if (e instanceof Projectile) {
                Projectile p = (Projectile) e;
                // Reverse or dampen projectile
                p.setVelocity(p.getVelocity().multiply(-0.5));
            }
        }
    }

    @Override
    public String getName() { return "WaterShield"; }

    @Override
    public String getDescription() {
        return "Raise a swirling shield of water that deflects incoming projectiles around you.";
    }

    @Override
    public String getInstructions() {
        return "Hold sneak to maintain the shield; release to end early.";
    }

    @Override
    public boolean isHarmlessAbility() { return true; }

    @Override
    public long getCooldown() { return cooldown; }
}
