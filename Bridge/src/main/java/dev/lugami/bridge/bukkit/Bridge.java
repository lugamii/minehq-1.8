package dev.lugami.bridge.bukkit;

import lombok.Setter;
import net.minecraft.server.v1_8_R3.Packet;
import dev.lugami.bridge.BridgeGlobal;
import dev.lugami.bridge.bukkit.listener.BridgeListener;
import dev.lugami.bridge.bukkit.listener.FreezeListener;
import dev.lugami.bridge.bukkit.parameters.*;
import dev.lugami.bridge.bukkit.parameters.param.filter.FilterActionParameter;
import dev.lugami.bridge.bukkit.parameters.param.filter.FilterParameter;
import dev.lugami.bridge.bukkit.parameters.param.filter.FilterTypeParameter;
import dev.lugami.bridge.bukkit.util.BukkitUtils;
import dev.lugami.bridge.global.disguise.DisguiseProfile;
import dev.lugami.bridge.global.filter.Filter;
import dev.lugami.bridge.global.filter.FilterAction;
import dev.lugami.bridge.global.filter.FilterType;
import dev.lugami.bridge.global.profile.Profile;
import dev.lugami.bridge.global.ranks.Rank;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import dev.lugami.qlib.command.FrozenCommandHandler;
import dev.lugami.qlib.nametag.FrozenNametagHandler;

import java.util.ArrayList;

public class Bridge extends JavaPlugin {

    @Getter private static Bridge instance;
    @Getter private boolean isBooted = false;
    @Getter private ArrayList<Packet> blockedPackets = new ArrayList<>();
    @Getter @Setter public boolean togglePacketLogger = false;

    @Override
    public void onLoad() {
        (instance = this).saveDefaultConfig();
        new BridgeGlobal();
        BridgeGlobal.getServerHandler().registerProvider(new BukkitStatusImplementer());
    }

    @Override
    public void onEnable() {
        FrozenCommandHandler.registerAll(this);
        FrozenCommandHandler.registerParameterType(Rank.class, new RankParamater());
        FrozenCommandHandler.registerParameterType(DisguiseProfile.class, new DisguiseParameter());
        FrozenCommandHandler.registerParameterType(Profile.class, new ProfileParamater());
        FrozenCommandHandler.registerParameterType(Plugin.class, new PluginParameter());
        FrozenCommandHandler.registerParameterType(FilterAction.class, new FilterActionParameter());
        FrozenCommandHandler.registerParameterType(FilterType.class, new FilterTypeParameter());
        FrozenCommandHandler.registerParameterType(Filter.class, new FilterParameter());
        FrozenNametagHandler.registerProvider(new BridgeNameTagProvider());

        BukkitUtils.registerListeners(BridgeListener.class);
        BukkitUtils.registerListeners(FreezeListener.class);
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> isBooted = true);
        BridgeGlobal.loadDisguise(false);
    }

    @Override
    public void onDisable() {
        BridgeGlobal.shutdown();
    }

}
