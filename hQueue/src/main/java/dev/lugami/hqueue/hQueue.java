package dev.lugami.hqueue;

import dev.lugami.hqueue.data.Queue;
import dev.lugami.hqueue.data.QueueManager;
import dev.lugami.hqueue.listeners.QueueListener;
import dev.lugami.hqueue.payloads.handler.PacketHandler;
import dev.lugami.hqueue.command.param.QueueParamType;
import dev.lugami.hqueue.utils.QueueConfig;
import dev.lugami.hqueue.utils.RedisCommand;
import dev.lugami.qlib.command.FrozenCommandHandler;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class hQueue extends JavaPlugin {

    private static hQueue instance;
    private static QueueConfig queueConfig;
    private static QueueManager queueManager;
    private static JedisPool jedisPool;

    public void onEnable() {
        instance = this;
        instance.saveDefaultConfig();
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        queueConfig = new QueueConfig();
        jedisPool = queueConfig.getRedisPassword().isEmpty() ? new JedisPool(new JedisPoolConfig(), queueConfig.getRedisHost(), queueConfig.getRedisPort(), 0) : new JedisPool((GenericObjectPoolConfig)new JedisPoolConfig(), queueConfig.getRedisHost(), queueConfig.getRedisPort(), 0, queueConfig.getRedisPassword(), queueConfig.getRedisDB());
        PacketHandler.init();
        queueManager = new QueueManager();
        queueManager.startQueues();
        FrozenCommandHandler.registerParameterType(Queue.class, new QueueParamType());
        FrozenCommandHandler.registerAll(this);
        this.getServer().getPluginManager().registerEvents(new QueueListener(), this);
    }

    public void onDisable() {
        jedisPool.close();
        instance = null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public <T> T runRedisCommand(RedisCommand<T> redisCommand) {
        Jedis jedis = jedisPool.getResource();
        jedis.select(hQueue.getQueueConfig().getRedisDB());
        T result = null;
        try {
            result = redisCommand.execute(jedis);
        }
        catch (Exception e) {
            e.printStackTrace();
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
        }
        finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
        return result;
    }

    public static hQueue getInstance() {
        return instance;
    }

    public static QueueConfig getQueueConfig() {
        return queueConfig;
    }

    public static QueueManager getQueueManager() {
        return queueManager;
    }

    public static JedisPool getJedisPool() {
        return jedisPool;
    }
}

