package dev.lugami.hqueue.utils;

import dev.lugami.hqueue.hQueue;
import org.bukkit.configuration.file.FileConfiguration;

public class QueueConfig {
    public String redisHost;
    public String redisPassword;
    public String redisChannel;
    public int redisPort;
    public int redisDB;

    public QueueConfig() {
        FileConfiguration c = hQueue.getInstance().getConfig();
        this.redisHost = c.getString("redis.host", "0.0.0.0");
        this.redisPort = c.getInt("redis.port", 6379);
        this.redisPassword = c.getString("redis.password", "");
        this.redisChannel = c.getString("redis.channel", "hqueue-server");
        this.redisDB = c.getInt("redis.database", 1);
    }

    public String getRedisHost() {
        return this.redisHost;
    }

    public String getRedisPassword() {
        return this.redisPassword;
    }

    public String getRedisChannel() {
        return this.redisChannel;
    }

    public int getRedisPort() {
        return this.redisPort;
    }

    public int getRedisDB() {
        return this.redisDB;
    }
}

