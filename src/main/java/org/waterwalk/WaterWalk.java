package org.waterwalk;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class WaterWalk extends JavaPlugin implements Listener {

    private final int MAX_SPEED_AMPLIFIER = 25;

    @Override
    public void onEnable() {
        // Register event listener
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Clean up resources
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();

        // Check if player is in survival or adventure mode
        if (player.getGameMode() != GameMode.SURVIVAL && player.getGameMode() != GameMode.ADVENTURE) {
            return;
        }

        // Check if player has the speedster permission
        if (!player.hasPermission("speedster")) {
            return;
        }

        // Check if player has maximum speed potion effect
        boolean hasMaxSpeed = false;
        for (PotionEffect effect : player.getActivePotionEffects()) {
            if (effect.getType() == PotionEffectType.SPEED && effect.getAmplifier() >= MAX_SPEED_AMPLIFIER) {
                hasMaxSpeed = true;
                break;
            }
        }

        if (hasMaxSpeed) {
            // Check if player is standing on water and not jumping
            if (player.getLocation().getBlock().getType() == Material.WATER && !player.isJumping()) {
                // Set player's velocity to zero
                player.setVelocity(new Vector(0, 0, 0));

                // Allow player to fly and set fly speed to 0.1
                player.setAllowFlight(true);
                player.setFlying(true);
                player.setFlySpeed(0.1f);
            }
        } else {
            // Revert player's movement if they lose maximum speed potion effect
            player.setAllowFlight(false);
            player.setFlying(false);
            player.setFlySpeed(0.1f);
        }
    }
}