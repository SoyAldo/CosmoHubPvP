package com.soyaldo.cosmo.hubpvp.listeners;

import com.soyaldo.cosmo.hubpvp.CosmoHubCombat;
import com.soyaldo.cosmo.hubpvp.managers.combat.CombatManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class InventoryClickListener implements Listener {

    private final CosmoHubCombat PLUGIN;

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        CombatManager combatManager = PLUGIN.getCombatManager();

        if (!combatManager.isInPvP(player)) {
            return;
        }

        if (item == null) {
            return;
        }

        // TODO Make nbt checking
    }

}