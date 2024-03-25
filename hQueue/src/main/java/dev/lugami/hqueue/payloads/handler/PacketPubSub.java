package dev.lugami.hqueue.payloads.handler;

import dev.lugami.hqueue.hQueue;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import redis.clients.jedis.JedisPubSub;

public class PacketPubSub extends JedisPubSub {

    public void onMessage(String channel, String message) {
        Class<?> packetClass;
        int packetMessageSplit = message.indexOf("||");
        String packetClassStr = message.substring(0, packetMessageSplit);
        String messageJson = message.substring(packetMessageSplit + "||".length());
        try {
            packetClass = Class.forName(packetClassStr);
        }
        catch (ClassNotFoundException ignored) {
            return;
        }
        Packet packet = (Packet)new Gson().fromJson(messageJson, packetClass);
        if (hQueue.getInstance().isEnabled()) {
            Bukkit.getScheduler().runTask(hQueue.getInstance(), packet::onReceive);
        }
    }
}

