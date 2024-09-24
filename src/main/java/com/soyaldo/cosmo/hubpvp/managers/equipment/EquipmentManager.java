package com.soyaldo.cosmo.hubpvp.managers.equipment;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class EquipmentManager {

    private CustomItem weapon, helmet, chestplate, leggings, boots;

    public void loadItems() {
        // Weapon
        weapon = getItemFromConfig("weapon");

        // Armor
        helmet = getItemFromConfig("helmet");
        chestplate = getItemFromConfig("chestplate");
        leggings = getItemFromConfig("leggings");
        boots = getItemFromConfig("boots");
    }

    public CustomItem getItemFromConfig(String name) {
        HubPvP instance = HubPvP.instance();
        String material = instance.getConfig().getString("items." + name + ".material");
        if (material == null) {
            instance.getLogger().warning("Material for item " + name + " is null!");
            return null;
        }
        CustomItem item = CustomItemManager.get().createCustomItem(new ItemStack(Material.valueOf(material)));

        String itemName = instance.getConfig().getString("items." + name + ".name");
        if (itemName != null && !itemName.isEmpty()) item.setName(StringUtil.colorize(itemName));

        List<String> lore = instance.getConfig().getStringList("items." + name + ".lore");
        if (!(lore.size() == 1 && lore.get(0).isEmpty())) item.addLore(StringUtil.colorize(lore));

        List<String> enchants = instance.getConfig().getStringList("items." + name + ".enchantments");
        if (enchants != null && !enchants.isEmpty()) {
            for (String enchant : enchants) {
                String[] split = enchant.split(":");
                Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(split[0].toLowerCase()));
                if (enchantment == null) {
                    HubPvP.instance().getLogger().warning("Could not find enchantment " + split[0]);
                    continue;
                }
                item.getItemStack().addEnchantment(enchantment, Integer.parseInt(split[1]));
            }
        }

        item.addFlags(ItemFlag.HIDE_UNBREAKABLE);
        item.setUnbreakable(true);

        return item;
    }

    public void giveWeapon(Player p) {
        p.getInventory().setItem(HubPvP.instance().getConfig().getInt("items.weapon.slot") - 1, getWeapon().getItemStack());
    }

}
