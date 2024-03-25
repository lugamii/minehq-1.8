package dev.lugami.ubungee;

import com.google.common.io.ByteStreams;
import dev.lugami.ubungee.commands.*;
import dev.lugami.ubungee.listener.BungeeListener;
import dev.lugami.ubungee.listener.CBListener;
import dev.lugami.ubungee.whitelist.WhitelistUUID;
import lombok.Getter;
import lombok.Setter;
import dev.lugami.bridge.global.GlobalAPI;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import dev.lugami.bridge.global.profile.Profile;
import dev.lugami.bridge.global.ranks.Rank;
import dev.lugami.ubungee.whitelist.WhitelistRank;
import dev.lugami.ubungee.whitelist.WhitelistType;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class uBungee extends Plugin {

    @Getter public static uBungee instance;
    private File configFile;
    @Getter private WhitelistType whitelist;
    @Getter private Configuration config;
    @Getter @Setter private String motdLineOne, motdLineTwo;
    @Getter @Setter private String whitelistMotdLineOne, whitelistMotdLineTwo;
    @Getter private List<List<String>> broadcasts = new ArrayList<>();
    @Getter @Setter private String bypassDomain = "";
    @Getter private Map<String, List<UUID>> domainConnection = new HashMap<>();
    private int broadcastPosition = 0;

    @Override
    public void onLoad() {
        setupConfig();
    }

    @Override
    public void onEnable() {
        instance = this;
        whitelist = (getConfig().getString("whitelist.type").equalsIgnoreCase("none") ? null : (getConfig().getString("whitelist.type").equalsIgnoreCase("rank") ? new WhitelistRank() : new WhitelistUUID()));
        motdLineOne = ChatColor.translateAlternateColorCodes('&', getConfig().getString("motd.1"));
        motdLineTwo = ChatColor.translateAlternateColorCodes('&', getConfig().getString("motd.2"));
        whitelistMotdLineOne = ChatColor.translateAlternateColorCodes('&', getConfig().getString("whitelist-motd.1"));
        whitelistMotdLineTwo = ChatColor.translateAlternateColorCodes('&', getConfig().getString("whitelist-motd.2"));

        getProxy().getPluginManager().registerListener(this, new BungeeListener());
        getProxy().getPluginManager().registerListener(this, new CBListener());
        getProxy().getPluginManager().registerCommand(this, new HubCommand());
        getProxy().getPluginManager().registerCommand(this, new StaffCommand());
        getProxy().getPluginManager().registerCommand(this, new ProfilesCommand());
        getProxy().getPluginManager().registerCommand(this, new GlistCommand());
        getProxy().getPluginManager().registerCommand(this, new ServerCommand());
        getProxy().getPluginManager().registerCommand(this, new SendCommand());
        getProxy().getPluginManager().registerCommand(this, new PullCommand());
        getProxy().getPluginManager().registerCommand(this, new WhitelistCommand());
        getProxy().getPluginManager().registerCommand(this, new CBICommand());
        getProxy().getPluginManager().registerCommand(this, new MOTDCommand());
        getProxy().getPluginManager().registerCommand(this, new WhitelistMOTDCommand());
        getProxy().getPluginManager().registerCommand(this, new BypassDomainCommand());
        getProxy().getPluginManager().registerCommand(this, new RegisterServerCommand());
        getProxy().getPluginManager().registerCommand(this, new UnRegsiterServerCommand());
        getProxy().getPluginManager().registerCommand(this, new DomainStatsCommand());

        if(getConfig().getSection("broadcasts").getKeys() != null) {
            getConfig().getSection("broadcasts").getKeys().forEach(s -> {

                broadcasts.add(getConfig().getStringList("broadcasts." + s));
            });

            startTipTask();
        }

    }

    @Override
    public void onDisable() {
        uBungee.getInstance().saveConfig();
    }

    private void setupConfig() {
        configFile = new File(getDataFolder(), "config.yml");
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                try (InputStream is = getResourceAsStream("config.yml");
                     OutputStream os = new FileOutputStream(configFile)) {
                    ByteStreams.copy(is, os);
                }
            } catch (IOException e) {
                throw new RuntimeException("Unable to create configuration file", e);
            }
        }

    }

    public boolean hasPermission(Profile profile, String permission) {
        if(profile.hasPermission(permission)) return true;
        for (Rank inherit : GlobalAPI.getPlayerRank(profile).getInherits()) {
            if(inherit.hasPermission(permission)) return true;
        }
        return false;
    }

    public boolean saveConfig() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, configFile);
            motdLineOne = ChatColor.translateAlternateColorCodes('&', getConfig().getString("motd.1"));
            motdLineTwo = ChatColor.translateAlternateColorCodes('&', getConfig().getString("motd.2"));
            whitelistMotdLineOne = ChatColor.translateAlternateColorCodes('&', getConfig().getString("whitelist-motd.1"));
            whitelistMotdLineTwo = ChatColor.translateAlternateColorCodes('&', getConfig().getString("whitelist-motd.2"));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setWhitelist(WhitelistType w) {
        if(w == null){
            whitelist = null;
            getConfig().set("whitelist.type", "none");
            saveConfig();
            return;
        }
        whitelist = w;
        getConfig().set("whitelist.type", w.name());
        if(w instanceof WhitelistRank) getConfig().set("whitelist.rank", ((WhitelistRank) w).getMinRank().getName());
        saveConfig();
    }

    private void startTipTask() {

        BungeeCord.getInstance().getScheduler().schedule(this, () -> {
            if(broadcasts == null || broadcasts.isEmpty()) return;
            broadcasts.get(broadcastPosition).forEach(str -> BungeeCord.getInstance().broadcast(ChatColor.translateAlternateColorCodes('&', str)));
            if(broadcastPosition >= (broadcasts.size() - 1)) broadcastPosition = 0;
            else broadcastPosition = broadcastPosition + 1;
        }, 0, 3, TimeUnit.MINUTES);

    }

    public void addDomainID(UUID uuid, String domain) {
        if (!domainConnection.containsKey(domain)) {
            domainConnection.put(domain, new ArrayList<>());
        }

        List<UUID> uuidList = domainConnection.get(domain);
        uuidList.add(uuid);
    }

}