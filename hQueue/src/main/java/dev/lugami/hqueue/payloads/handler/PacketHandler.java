package dev.lugami.hqueue.payloads.handler;

import dev.lugami.hqueue.hQueue;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public final class PacketHandler {

    private static String GLOBAL_MESSAGE_CHANNEL;
    static final String PACKET_MESSAGE_DIVIDER = "||";

    public static void init() {
        GLOBAL_MESSAGE_CHANNEL = hQueue.getQueueConfig().getRedisChannel();
        PacketHandler.connectToServer(hQueue.getJedisPool());
    }

    public static void connectToServer(JedisPool connectTo) {
        Thread subscribeThread = new Thread(() -> {
            while (hQueue.getInstance().isEnabled()) {
                try {
                    Jedis jedis = connectTo.getResource();
                    Throwable throwable = null;
                    try {
                        PacketPubSub pubSub = new PacketPubSub();
                        String channel = GLOBAL_MESSAGE_CHANNEL;
                        jedis.subscribe(pubSub, channel);
                    }
                    catch (Throwable pubSub) {
                        throwable = pubSub;
                        throw pubSub;
                    }
                    finally {
                        if (jedis == null) continue;
                        if (throwable != null) {
                            try {
                                jedis.close();
                            }
                            catch (Throwable pubSub) {
                                throwable.addSuppressed(pubSub);
                            }
                            continue;
                        }
                        jedis.close();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "hQueue - Packet Subscribe Thread");
        subscribeThread.setDaemon(true);
        subscribeThread.start();
    }

    public static void send(Packet packet) {
        if (!hQueue.getInstance().isEnabled()) {
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(hQueue.getInstance(), () -> {
            try (Jedis jedis = hQueue.getJedisPool().getResource();){
                String encodedPacket = packet.getClass().getName() + PACKET_MESSAGE_DIVIDER + new Gson().toJson(packet);
                jedis.publish(GLOBAL_MESSAGE_CHANNEL, encodedPacket);
            }
        });
    }

    private PacketHandler() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

