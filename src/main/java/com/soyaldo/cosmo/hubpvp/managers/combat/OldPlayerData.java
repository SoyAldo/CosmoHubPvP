package com.soyaldo.cosmo.hubpvp.managers.combat;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public record OldPlayerData(Player player, ItemStack[] armor, boolean canFly) {

}