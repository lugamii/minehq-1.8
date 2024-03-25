package dev.lugami.bridge.bukkit.parameters;

import dev.lugami.bridge.BridgeGlobal;
import dev.lugami.bridge.global.disguise.DisguiseProfile;
import dev.lugami.qlib.command.ParameterType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class DisguiseParameter implements ParameterType<DisguiseProfile> {

    public static boolean isUUID(String str) {
        try {
            UUID uuid = UUID.fromString(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    @Override
    public DisguiseProfile transform(CommandSender sender, String source) {
        DisguiseProfile disguiseProfile;
        if (isUUID(source)) {
            disguiseProfile = BridgeGlobal.getDisguiseManager().getDisguiseProfiles().get(UUID.fromString(source));
        } else {
            disguiseProfile = BridgeGlobal.getDisguiseManager().getProfile(source);
        }
        if (disguiseProfile == null) {
            sender.sendMessage("§cThere is no such disguise profile with the " + (isUUID(source) ? "uuid" : "name") + " \"" + source + "\".");
            return null;
        }
        return disguiseProfile;
    }

    @Override
    public List<String> tabComplete(Player player, Set<String> set, String s) {
        return null;
    }
}
