package dev.lugami.hqueue.payloads;

import dev.lugami.hqueue.hQueue;
import dev.lugami.hqueue.data.Queue;
import dev.lugami.hqueue.data.QueuePlayer;
import dev.lugami.hqueue.payloads.handler.Packet;
import java.beans.ConstructorProperties;
import java.util.UUID;

import org.bukkit.Bukkit;

public class JoinQueuePacket implements Packet {

    private UUID uuid;
    private String queueName;
    private String sender;

    public JoinQueuePacket(UUID uuid, Queue queue, String sender) {
        this.uuid = uuid;
        this.queueName = queue.getQueueName();
        this.sender = sender;
    }

    @Override
    public void onReceive() {
        if (this.sender.equals(Bukkit.getServerName())) {
            return;
        }
        Queue queue = hQueue.getQueueManager().getQueue(this.queueName);
        if (queue == null) {
            return;
        }
        queue.getPlayersInQueue().add(new QueuePlayer(this.uuid, System.currentTimeMillis()));
    }

    @ConstructorProperties(value={"uuid", "queueName", "sender"})
    public JoinQueuePacket(UUID uuid, String queueName, String sender) {
        this.uuid = uuid;
        this.queueName = queueName;
        this.sender = sender;
    }
}

