package dev.lugami.bridge.bukkit.parameters;

import dev.lugami.bridge.BridgeGlobal;
import dev.lugami.bridge.bukkit.BukkitAPI;
import dev.lugami.bridge.global.util.MojangUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import dev.lugami.bridge.global.profile.Profile;
import dev.lugami.qlib.command.ParameterType;
import dev.lugami.qlib.qLib;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ProfileParamater implements ParameterType<Profile> {

    public static boolean isUUID(String str) {
        try {
            UUID uuid = UUID.fromString(str);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    @Override
    public Profile transform(CommandSender sender, String source) {

        String fixedSource = source.replace("get/", "");

        Profile pf;

        if(fixedSource.equals("self")) {
            pf = BukkitAPI.getProfile(sender);
        }
        else if(isUUID(fixedSource)) {
            pf = BukkitAPI.getProfile(UUID.fromString(fixedSource));
        }else {

            pf = BukkitAPI.getProfile(fixedSource);

            if(!source.startsWith("get/")) {

                if(pf == null) {
                    UUID playerUUID = null;
                    try {
                        playerUUID = MojangUtils.fetchUUID(fixedSource);
                        if(playerUUID != null) {
                            pf = BridgeGlobal.getProfileHandler().getNewProfileOrCreate(fixedSource, playerUUID);
                        }else {
                            pf = null;
                        }
                    } catch (Exception e) {
                        pf = null;
                        e.printStackTrace();
                    }
                }
            }



        }
        if(pf == null) {
            sender.sendMessage("§cNo such player with the " + (isUUID(fixedSource) ? "uuid" : "name") + " \"" + fixedSource + "\".");
            return null;
        }
        return pf;
    }

    @Override
    public List<String> tabComplete(Player sender, Set<String> flags, String source) {
        List<String> completions = new ArrayList<>();

        for (Player player : qLib.getInstance().getServer().getOnlinePlayers()) {
            if (StringUtils.startsWithIgnoreCase(player.getName(), source) && sender.canSee(player) && BridgeGlobal.getProfileHandler().getProfileByUUID(player.getUniqueId()) != null) {
                completions.add(player.getName());
            }
        }
        return completions;
    }
}
