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

@AbilityDescription(name = "HydroAvatar", elems = {AbilityDescriptionElement.WATER})
public class HydroAvatar extends WaterBase {

    private long duration;
    private double damageBoost;

    public HydroAvatar(Player player) {
        super(player);
        this.duration = cfg.getLong("abilities.water.Defaults.duration", 10000);
        this.damageBoost = cfg.getDouble("abilities.water.Defaults.force", 1.6);

        BendingPlayer bp = BendingPlayer.getBendingPlayer(bender);
        if (bp == null || !bp.canBend(this)) return;
        start();
    }

    @Override public void progress() {
        if (!bender.isOnline() || bender.isDead()) { remove(); return; }

        bender.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 40, 1, true, false));
        bender.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, 1, true, false));
        bender.getWorld().spawnParticle(Particle.WATER_WAKE, bender.getLocation(), 8, 0.6, 0.6, 0.6, 0.02);

        if (timeUp(duration)) { applyCooldown(); remove(); return; }
    }

    @Override public String getName() { return "HydroAvatar"; }
    @Override public String getDescription() { return "Enter an empowered state with heightened water control and boosted damage."; }
    @Override public String getInstructions() { return "Activate to gain a temporary, powerful buff."; }
    @Override public long getCooldown() { return cooldown; }
}
