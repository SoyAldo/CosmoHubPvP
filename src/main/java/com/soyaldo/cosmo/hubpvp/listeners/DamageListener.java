package com.soyaldo.cosmo.hubpvp.listeners;

import com.soyaldo.cosmo.hubpvp.CosmoHubCombat;
import com.soyaldo.cosmo.hubpvp.managers.combat.CombatManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.List;

@RequiredArgsConstructor
public class DamageListener implements Listener {

    private final CosmoHubCombat PLUGIN;

    @EventHandler(priority = EventPriority.HIGH)
    public void onDamage(EntityDamageByEntityEvent event) {
        // Getting managers
        CombatManager combatManager = PLUGIN.getCombatManager();
        // Getting entities
        Entity damagerEntity = event.getDamager();
        Entity damagedEntity = event.getEntity();
        // Checking if the entities are Players
        if (!damagerEntity.getType().equals(EntityType.PLAYER) || !damagedEntity.getType().equals(EntityType.PLAYER)) {
            return;
        }
        // Transform the entities to Player
        Player damagerPlayer = (Player) damagerEntity;
        Player damagedPlayer = (Player) damagedEntity;
        // Checking if any player are in combat
        if (!combatManager.isInPvP(damagerPlayer) || !combatManager.isInPvP(damagedPlayer)) {
            event.setCancelled(true);
        }
        // Checking the world
        List<String> disabledWorlds = PLUGIN.getSettings().getStringList("disabledWorlds");
        World world = damagerEntity.getLocation().getWorld();
        if (disabledWorlds.contains(world.getName())) {
            event.setCancelled(true);
        }
    }

}