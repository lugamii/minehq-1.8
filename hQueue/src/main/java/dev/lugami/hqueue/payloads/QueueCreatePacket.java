package dev.lugami.hqueue.payloads;

import dev.lugami.hqueue.data.Queue;
import dev.lugami.hqueue.hQueue;
import dev.lugami.hqueue.payloads.handler.Packet;

import java.beans.ConstructorProperties;
import java.util.UUID;

import org.bukkit.Bukkit;

public class QueueCreatePacket implements Packet {

    private UUID uuid;
    private String name;
    private String sender;

    @Override
    public void onReceive() {
        if (this.sender.equals(Bukkit.getServerName())) {
            return;
        }
        Queue queue = hQueue.getQueueManager().getQueue(this.uuid);
        if (queue != null) {
            return;
        }
        new Queue(this.uuid, this.name);
    }

    @ConstructorProperties(value={"uuid", "name", "sender"})
    public QueueCreatePacket(UUID uuid, String name, String sender) {
        this.uuid = uuid;
        this.name = name;
        this.sender = sender;
    }
}

