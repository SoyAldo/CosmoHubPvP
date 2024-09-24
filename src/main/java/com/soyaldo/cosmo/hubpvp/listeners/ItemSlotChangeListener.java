package com.soyaldo.cosmo.hubpvp.listeners;

import com.soyaldo.cosmo.hubpvp.CosmoHubCombat;
import com.soyaldo.cosmo.hubpvp.managers.combat.CombatManager;
import lombok.RequiredArgsConstructor;
import me.quared.hubpvp.HubPvP;
import me.quared.hubpvp.core.PvPState;
import me.quared.hubpvp.util.StringUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

@RequiredArgsConstructor
public class ItemSlotChangeListener implements Listener {

    private final CosmoHubCombat PLUGIN;

    @EventHandler
    public void onItemSlotChange(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack held = event.getPlayer().getInventory().getItem(event.getNewSlot());
        CombatManager combatManager = PLUGIN.getCombatManager();

        if (!player.hasPermission("hubpvp.use")) return;

        if (Objects.equals(held, combatManager.getWeapon().getItemStack())) {
            if (combatManager.getPlayerState(player) == PvPState.DISABLING)
                combatManager.setPlayerState(player, PvPState.ON);
            if (combatManager.getPlayerState(player) == PvPState.ENABLING) return;

            if (HubPvP.instance().getConfig().getStringList("disabled-worlds").contains(player.getWorld().getName())) {
                player.sendMessage(StringUtil.colorize(instance.getConfig().getString("lang.disabled-in-world")));
                return;
            }

            // Equipping
            if (!combatManager.isInPvP(player)) {
                combatManager.setPlayerState(player, PvPState.ENABLING);
                BukkitRunnable enableTask = new BukkitRunnable() {
                    int time = instance.getConfig().getInt("enable-cooldown") + 1;

                    public void run() {
                        time--;
                        if (combatManager.getPlayerState(player) != PvPState.ENABLING || !held.isSimilar(combatManager.getWeapon().getItemStack())) {
                            combatManager.removeTimer(player);
                            cancel();
                        } else if (time == 0) {
                            combatManager.enablePvP(player);
                            combatManager.removeTimer(player);
                            cancel();
                        } else {
                            player.sendMessage(StringUtil.colorize(instance.getConfig().getString("lang.pvp-enabling").replaceAll("%time%", Integer.toString(time))));
                        }
                    }
                };
                combatManager.putTimer(player, enableTask);
                enableTask.runTaskTimer(instance, 0L, 20L);
            }
        } else if (combatManager.isInPvP(player)) {
            if (combatManager.getPlayerState(player) == PvPState.ENABLING)
                combatManager.setPlayerState(player, PvPState.OFF);
            if (combatManager.getPlayerState(player) == PvPState.DISABLING) return;
            // Dequipping
            combatManager.setPlayerState(player, PvPState.DISABLING);
            BukkitRunnable disableTask = new BukkitRunnable() {
                int time = instance.getConfig().getInt("disable-cooldown") + 1;

                public void run() {
                    time--;
                    if (combatManager.getPlayerState(player) != PvPState.DISABLING || held != null && held.isSimilar(combatManager.getWeapon().getItemStack())) {
                        combatManager.removeTimer(player);
                        cancel();
                    } else if (time == 0) {
                        combatManager.disablePvP(player);
                        combatManager.removeTimer(player);
                        cancel();
                    } else {
                        player.sendMessage(StringUtil.colorize(instance.getConfig().getString("lang.pvp-disabling").replaceAll("%time%", Integer.toString(time))));
                    }
                }
            };
            combatManager.putTimer(player, disableTask);
            disableTask.runTaskTimer(instance, 0L, 20L);
        } else {
            // Not in PvP and not equipping
            combatManager.setPlayerState(player, PvPState.OFF); // Ensure there isn't any lingering state
            combatManager.removeTimer(player);
        }
    }

}