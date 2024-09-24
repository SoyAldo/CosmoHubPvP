package com.soyaldo.cosmo.hubpvp.util;

import lombok.Getter;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class Messenger {

    private final JavaPlugin javaPlugin;
    @Getter
    @Setter
    private String langType = "en_us";
    @Getter
    private Yaml lang;

    public Messenger(JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;
    }

    public Messenger(JavaPlugin javaPlugin, String langType) {
        this.javaPlugin = javaPlugin;
        this.langType = langType;
    }

    public void register() {
        reload();
    }

    public void reload() {
        InputStream inputStream = javaPlugin.getResource("lang/" + langType + ".yml");
        if (inputStream == null) {
            lang = new Yaml(javaPlugin, "lang", langType);
            if (!lang.exist()) {
                lang = new Yaml(javaPlugin, "lang", "en_us", javaPlugin.getResource("lang/en_us.yml"));
            }
        } else {
            lang = new Yaml(javaPlugin, "lang", "en_us", inputStream);
        }
        lang.register();
    }

    public void send(String name, String path) {
        send(name, path, new String[][]{});
    }

    public void send(String name, String path, String[][] replacements) {
        if (name.equals("console")) {
            CommandSender console = javaPlugin.getServer().getConsoleSender();
            send(console, path, replacements);
        } else {
            Player player = javaPlugin.getServer().getPlayerExact(name);
            if (player != null) send(player, path, replacements);
        }
    }

    public void send(CommandSender commandSender, String path) {
        send(commandSender, path, new String[][]{});
    }

    public void send(CommandSender commandSender, String path, String[][] replacements) {
        Object message = lang.get(path, "&8[&4&l!&8] &cEl mensaje &f&n" + path + "&r &cno existe.");
        String prefix = lang.getString("prefix", "");

        replacements = Arrays.copyOf(replacements, replacements.length + 1);
        replacements[replacements.length - 1] = new String[]{"%prefix%", prefix};

        sendRaw(commandSender, message, replacements);
    }

    public void sendRaw(CommandSender commandSender, Object message) {
        sendRaw(commandSender, message, new String[][]{});
    }

    public void sendRaw(CommandSender commandSender, Object message, String[][] replacements) {
        // If the message is null.
        if (message == null) {
            // Return the method.
            return;
        }
        // If the message is a String o List
        if (message.getClass().getSimpleName().equals("String")) {
            sendRaw(javaPlugin, commandSender, (String) message, replacements);
        } else {
            sendRaw(javaPlugin, commandSender, (List<String>) message, replacements);
        }
    }

    public static void sendRaw(JavaPlugin javaPlugin, CommandSender commandSender, List<String> messages) {
        // Use the sendRaw method with the empty replacements.
        sendRaw(javaPlugin, commandSender, messages, new String[][]{});
    }

    public static void sendRaw(JavaPlugin javaPlugin, CommandSender commandSender, List<String> messages, String[][] replacements) {
        // If the message is null.
        if (messages == null) {
            // Return the method.
            return;
        }
        // Use the sendRaw in all list.
        for (String message : messages) {
            sendRaw(javaPlugin, commandSender, message, replacements);
        }
    }

    public static void sendRaw(JavaPlugin javaPlugin, CommandSender commandSender, String message) {
        // Use the sendRaw method with the empty replacements.
        sendRaw(javaPlugin, commandSender, message, new String[][]{});
    }

    public static void sendRaw(JavaPlugin javaPlugin, CommandSender commandSender, String message, String[][] replacements) {
        // If the message is null.
        if (message == null) {
            // Return the method.
            return;
        }

        // Replace all replacements.
        for (String[] replacement : replacements) {
            message = message.replace(replacement[0], replacement[1]);
        }

        // PlaceholderAPI
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                message = PlaceholderAPI.setPlaceholders(player, message);
            }
        }

        // Generating the audience.
        Audience audience = BukkitAudiences.create(javaPlugin).sender(commandSender);

        // Generating the component.
        Component component = MiniMessage.miniMessage().deserialize(message);

        // Send the message.
        audience.sendMessage(component);
    }

}