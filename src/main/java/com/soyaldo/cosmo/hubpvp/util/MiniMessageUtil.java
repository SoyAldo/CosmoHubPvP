package com.soyaldo.cosmo.hubpvp.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class MiniMessageUtil {

    public Component applyColor(String text) {
        return MiniMessage.miniMessage().deserialize(text);
    }

    public String applyColorLegacy(String text) {
        return MiniMessage.miniMessage().serialize(applyColor(text));
    }

}