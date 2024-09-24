package com.soyaldo.cosmo.hubpvp.listeners;

import com.soyaldo.cosmo.hubpvp.CosmoHubCombat;
import com.soyaldo.cosmo.hubpvp.util.Yaml;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

@RequiredArgsConstructor
public class PlayerRespawnListener implements Listener {

    private final CosmoHubCombat PLUGIN;

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Yaml settings = PLUGIN.getSettings();
        Player player = event.getPlayer();
        if (!settings.getBoolean("respawn.enabled", false)) return;
        if (settings.getBoolean("respawn.useWorldSpawn", false)) {
            event.setRespawnLocation(player.getWorld().getSpawnLocation());
        } else {
            Location spawn = new Location(
                    player.getWorld(),
                    settings.getDouble("x"),
                    settings.getDouble("y"),
                    settings.getDouble("z"),
                    settings.getInt("yaw"),
                    settings.getInt("pitch")
            );
            event.setRespawnLocation(spawn);
        }
    }

}
