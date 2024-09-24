package com.soyaldo.cosmo.hubpvp.commands;

import com.soyaldo.cosmo.hubpvp.CosmoHubCombat;
import com.soyaldo.cosmo.hubpvp.util.Command;
import com.soyaldo.cosmo.hubpvp.util.Messenger;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CosmoHubCombatAdminCommand extends Command {

    private final CosmoHubCombat PLUGIN;

    public CosmoHubCombatAdminCommand(CosmoHubCombat PLUGIN) {
        super("cosmohubcombatadmin");
        this.PLUGIN = PLUGIN;
    }

    public void onExecute(CommandSender sender, String[] args) {
        Messenger messenger = PLUGIN.getMessenger();
        if (args.length == 0) {
            messenger.send(sender, "emptyArguments");
            return;
        }
        String subCommand = args[0];
        if (subCommand.equalsIgnoreCase("reload")) {
            PLUGIN.onReload();
            messenger.send(sender, "reloaded");
        } else {
            messenger.send(sender, "invalidSubcommand", new String[][]{
                    {"%subCommand%", subCommand}
            });
        }
    }

    public String onTabComplete(CommandSender sender, int position, String[] previousArguments) {
        if (position == 1) {
            return "reload,version";
        }
        return null;
    }

    @Override
    public void onPlayerExecute(Player sender, String[] args) {
        onExecute(sender, args);
    }

    @Override
    public void onConsoleExecute(ConsoleCommandSender sender, String[] args) {
        onExecute(sender, args);
    }

    @Override
    public String onPlayerTabComplete(Player requester, int position, String[] previousArguments) {
        return onTabComplete(requester, position, previousArguments);
    }

    @Override
    public String onConsoleTabComplete(ConsoleCommandSender requester, int position, String[] previousArguments) {
        return onTabComplete(requester, position, previousArguments);
    }

}