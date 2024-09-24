package com.soyaldo.cosmo.hubpvp;

import com.soyaldo.cosmo.hubpvp.bstats.Metrics;
import com.soyaldo.cosmo.hubpvp.commands.CosmoHubCombatAdminCommand;
import com.soyaldo.cosmo.hubpvp.listeners.*;
import com.soyaldo.cosmo.hubpvp.managers.combat.CombatManager;
import com.soyaldo.cosmo.hubpvp.util.Copyright;
import com.soyaldo.cosmo.hubpvp.util.Messenger;
import com.soyaldo.cosmo.hubpvp.util.Yaml;
import lombok.Getter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class CosmoHubCombat extends JavaPlugin {

    // Files
    private final Yaml settings = new Yaml(this, "settings", getResource("settings.yml"));
    // Utilities
    private Messenger messenger;

    private CombatManager combatManager;

    @Override
    public void onEnable() {
        // Files
        settings.register();
        // Utilities
        messenger = new Messenger(this, settings.getString("lang", "en_us"));

        ItemGuiLib.setPluginInstance(this);
        combatManager = new CombatManager(this);
        // BStats
        new Metrics(this, 0);

        registerListeners();
        registerCommands();

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        Copyright.sendVersionStatusFromConsole(this, "&aEnabled");
    }

    @Override
    public void onDisable() {
        combatManager.disable();
        Copyright.sendVersionStatusFromConsole(this, "&cDisabled");
    }

    public void onReload() {
        // Files
        settings.reload();
        // Utilities
        messenger = new Messenger(this, settings.getString("lang", "en_us"));
        combatManager.reload();
    }

    private void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new DamageListener(this), this);
        pluginManager.registerEvents(new PlayerDeathListener(this), this);
        pluginManager.registerEvents(new PlayerRespawnListener(this), this);
        pluginManager.registerEvents(new ItemSlotChangeListener(this), this);
        pluginManager.registerEvents(new PlayerJoinListener(this), this);
        pluginManager.registerEvents(new PlayerQuitListener(this), this);
        pluginManager.registerEvents(new PlayerDropItemListener(this), this);
        pluginManager.registerEvents(new InventoryClickListener(this), this);
    }

    private void registerCommands() {
        new CosmoHubCombatAdminCommand(this).registerCommand(this);
    }

}