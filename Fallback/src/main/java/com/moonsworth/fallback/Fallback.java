package com.moonsworth.fallback;

import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.moonsworth.fallback.alert.AlertsManager;
import com.moonsworth.fallback.banwave.BanWave;
import com.moonsworth.fallback.banwave.BanWaveManager;
import com.moonsworth.fallback.player.PlayerDataManager;
import com.moonsworth.fallback.log.LogExportRunnable;
import com.moonsworth.fallback.command.BanWaveCommand;
import com.moonsworth.fallback.listener.PlayerListener;
import com.moonsworth.fallback.handler.CustomMovementHandler;
import com.moonsworth.fallback.handler.CustomPacketHandler;
import dev.lugami.qlib.command.FrozenCommandHandler;
import dev.lugami.spigot.KatsuSpigot;
import net.minecraft.server.v1_8_R3.MinecraftServer;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClient;
import com.moonsworth.fallback.log.LogManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Fallback extends JavaPlugin
{
    public static Fallback instance;
    private PlayerDataManager playerDataManager;
    private AlertsManager alertsManager;
    private LogManager logManager;
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    public BanWaveManager banWaveManager;
    private double rangeVl = 100.0;
    private List<String> disabledChecks = new ArrayList<>();

    
    public void onLoad() {
    }
    
    public void onEnable() {
        Fallback.instance = this;
        this.banWaveManager = new BanWaveManager();
        new BanWave();
        this.saveDefaultConfig();
        this.registerHandlers();
        this.registerManagers();
        this.registerListeners();
        this.registerCommands();
        this.registerExportLogsTimer();
        this.registerDatabase();
    }
    
    public void onDisable() {
        this.getLogManager().exportAllLogs();
        this.mongoClient.close();
    }
    
    public boolean isAntiCheatEnabled() {
        return MinecraftServer.getServer().tps1.getAverage() > 19.0;
    }
    
    private void registerHandlers() {
        KatsuSpigot.INSTANCE.addPacketHandler(new CustomPacketHandler(this));
        KatsuSpigot.INSTANCE.addMovementHandler(new CustomMovementHandler(this));
    }
    
    private void registerManagers() {
        this.alertsManager = new AlertsManager();
        this.playerDataManager = new PlayerDataManager();
        this.logManager = new LogManager();
    }
    
    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        this.getServer().getPluginManager().registerEvents(new BanWaveCommand(), this);
    }
    
    private void registerCommands() {
        FrozenCommandHandler.registerAll(this);
    }
    
    private void registerExportLogsTimer() {
        this.getServer().getScheduler().runTaskTimerAsynchronously(this, new LogExportRunnable(null), 600L, 600L);
    }
    
    private void registerDatabase() {
        if (this.getConfig().getBoolean("Mongo.Authentication.Enabled")) {
            final ServerAddress serverAddress = new ServerAddress(this.getConfig().getString("Mongo.Host"), this.getConfig().getInt("Mongo.Port"));
            final MongoCredential credential = MongoCredential.createCredential(this.getConfig().getString("Mongo.Authentication.Username"), "admin", this.getConfig().getString("Mongo.Authentication.Password").toCharArray());
            this.mongoClient = new MongoClient(serverAddress, credential, MongoClientOptions.builder().build());
        }
        else {
            this.mongoClient = new MongoClient(this.getConfig().getString("Mongo.Host"), this.getConfig().getInt("Mongo.Port"));
        }
        this.mongoDatabase = this.mongoClient.getDatabase(this.getConfig().getString("Mongo.DbName"));
    }
    
    public PlayerDataManager getPlayerDataManager() {
        return this.playerDataManager;
    }
    
    public AlertsManager getAlertsManager() {
        return this.alertsManager;
    }
    
    public LogManager getLogManager() {
        return this.logManager;
    }
    
    public MongoClient getMongoClient() {
        return this.mongoClient;
    }
    
    public MongoDatabase getMongoDatabase() {
        return this.mongoDatabase;
    }
    
    public BanWaveManager getBanWaveManager() {
        return this.banWaveManager;
    }
    
    public double getRangeVl() {
        return this.rangeVl;
    }
    
    public List<String> getDisabledChecks() {
        return this.disabledChecks;
    }
}
