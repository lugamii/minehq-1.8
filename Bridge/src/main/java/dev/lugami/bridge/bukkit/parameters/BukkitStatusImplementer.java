package dev.lugami.bridge.bukkit.parameters;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dev.lugami.bridge.BridgeGlobal;
import dev.lugami.bridge.bukkit.listener.GeneralListener;
import dev.lugami.bridge.global.util.JsonChain;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Bukkit;
import dev.lugami.bridge.global.status.StatusProvider;
import dev.lugami.qlib.uuid.FrozenUUIDCache;

import java.util.List;
import java.util.stream.Collectors;

public class BukkitStatusImplementer extends StatusProvider {

    public BukkitStatusImplementer() {
        super("Bridge Bukkit Implementer", 1);
    }

    @Override
    public String serverName() {
        return BridgeGlobal.getServerName();
    }

    @Override
    public String serverStatus() {
        return GeneralListener.getServerStatus();
    }

    @Override
    public int online() {
        return Bukkit.getOnlinePlayers().size();
    }

    @Override
    public int maximum() {
        return Bukkit.getMaxPlayers();
    }

    @Override
    public String motd() {
        return Bukkit.getMotd();
    }

    @Override
    public double tps() {
        double[] tps = org.bukkit.Bukkit.spigot().getTPS();
        return tps[0];
    }

    @Override
    public List<String> players() {
        return Bukkit.getOnlinePlayers().stream().map(player -> player.getUniqueId().toString()).collect(Collectors.toList());
    }

    @Override
    public JsonObject dataPassthrough() {
        JsonChain jsonChain = new JsonChain();
        if(BridgeGlobal.getUpdaterManager().getRunningDirectory().contains("/temp/"))
            jsonChain.addProperty("tempServer", true).addProperty("runningDirectory", BridgeGlobal.getUpdaterManager().getRunningDirectory()).get();
        jsonChain.addProperty("port", Bukkit.getPort()).addProperty("vanished", new Gson().toJson(Bukkit.getOnlinePlayers().stream().filter(player -> player.hasMetadata("invisible")).map(player -> FrozenUUIDCache.name(player.getUniqueId())).collect(Collectors.toList())));
        return jsonChain.get();
    }
}