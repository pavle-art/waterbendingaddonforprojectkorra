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

@AbilityDescription(name = "WaterWall", elems = {AbilityDescriptionElement.WATER})
public class WaterWall extends WaterBase {

    private long duration;
    private double height;
    private double width;

    public WaterWall(Player player) {
        super(player);
        this.duration = cfg.getLong("abilities.water.Defaults.duration", 4000);
        this.height = cfg.getDouble("abilities.water.Defaults.height", 3.0);
        this.width = cfg.getDouble("abilities.water.Defaults.width", 6.0);

        BendingPlayer bp = BendingPlayer.getBendingPlayer(bender);
        if (bp == null || !bp.canBend(this)) return;
        start();
    }

    @Override
    public void progress() {
        if (!bender.isOnline() || bender.isDead()) { remove(); return; }
        Location base = bender.getLocation().add(0,1,0);
        Vector dir = bender.getLocation().getDirection().normalize();
        for (double i = -width/2.0; i <= width/2.0; i += 0.7) {
            Location loc = base.clone().add(dir.clone().rotateAroundY(0).multiply(0)).add(dir.clone().crossProduct(new Vector(0,1,0)).normalize().multiply(i));
            for (double y = 0; y < height; y += 0.8) {
                Location p = loc.clone().add(0, y, 0);
                p.getWorld().spawnParticle(Particle.SPLASH, p, 6, 0.2, 0.2, 0.2, 0.01);
            }
        }

        if (timeUp(duration)) { applyCooldown(); remove(); return; }
    }

    @Override public String getName() { return "WaterWall"; }
    @Override public String getDescription() { return "Raise a wall of water to block attacks and sightlines."; }
    @Override public String getInstructions() { return "Create a temporary wall of water to control space."; }
    @Override public long getCooldown() { return cooldown; }
}
