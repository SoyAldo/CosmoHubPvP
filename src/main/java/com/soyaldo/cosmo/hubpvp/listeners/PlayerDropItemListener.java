package com.soyaldo.cosmo.hubpvp.listeners;

import com.soyaldo.cosmo.hubpvp.CosmoHubCombat;
import com.soyaldo.cosmo.hubpvp.managers.combat.CombatManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class PlayerDropItemListener implements Listener {

    private final CosmoHubCombat PLUGIN;

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItemDrop().getItemStack();
        CombatManager combatManager = PLUGIN.getCombatManager();

        if (!combatManager.isInPvP(player)) {
            return;
        }

        // TODO Make nbt verification
    }

}