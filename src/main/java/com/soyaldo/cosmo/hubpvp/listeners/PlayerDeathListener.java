package com.soyaldo.cosmo.hubpvp.listeners;

import com.soyaldo.cosmo.hubpvp.CosmoHubCombat;
import com.soyaldo.cosmo.hubpvp.managers.combat.CombatManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

@RequiredArgsConstructor
public class PlayerDeathListener implements Listener {

    private final CosmoHubCombat PLUGIN;

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        CombatManager combatManager = PLUGIN.getCombatManager();
        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        if (killer == null) return;
        if (!combatManager.isInPvP(victim) || !combatManager.isInPvP(killer)) return;

        event.setKeepInventory(true);
        event.setKeepLevel(true);

        victim.getInventory().setHeldItemSlot(0);

        int healthOnKill = PLUGIN.getSettings().getInt("healthOnKill");
        if (healthOnKill != -1) {
            killer.setHealth(Math.min(killer.getHealth() + healthOnKill, killer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
            PLUGIN.getMessenger().send(killer, "healthGained", new String[][]{
                    {"%extra%", String.valueOf(healthOnKill)},
                    {"%killer%", victim.getName()}
            });
        }

        combatManager.disablePvP(victim);

        PLUGIN.getMessenger().send(victim, "killed", new String[][]{
                {"%killer%", killer.getName()}
        });
        PLUGIN.getMessenger().send(killer, "killedOther", new String[][]{
                {"%killed%", victim.getName()}
        });

        event.setDeathMessage("");
    }


}