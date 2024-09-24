package com.soyaldo.cosmo.hubpvp.listeners;

import com.soyaldo.cosmo.hubpvp.CosmoHubCombat;
import com.soyaldo.cosmo.hubpvp.Permissions;
import com.soyaldo.cosmo.hubpvp.managers.combat.CombatManager;
import com.soyaldo.cosmo.hubpvp.managers.combat.CombatState;
import com.soyaldo.cosmo.hubpvp.managers.combat.OldPlayerData;
import lombok.RequiredArgsConstructor;
import me.quared.hubpvp.HubPvP;
import me.quared.hubpvp.core.PvPManager;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@RequiredArgsConstructor
public class PlayerJoinListener implements Listener {

    private final CosmoHubCombat PLUGIN;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        CombatManager combatManager = PLUGIN.getCombatManager();
        // Checking the usage permission
        if (!player.hasPermission(Permissions.usagePermission)) {
            return;
        }
        // Checking the disabled words
        World world = player.getWorld();
        String worldName = world.getName();
        if (!PLUGIN.getSettings().getStringList("disabled-worlds").contains(worldName)) {
            combatManager.giveWeapon(player);
        }

        combatManager.getOldPlayerDataList().add(new OldPlayerData(player, player.getInventory().getArmorContents(), player.getAllowFlight()));
        combatManager.setPlayerCombatState(player, CombatState.OFF);
    }

}