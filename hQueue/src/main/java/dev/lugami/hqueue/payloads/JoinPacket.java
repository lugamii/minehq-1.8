package dev.lugami.hqueue.payloads;

import dev.lugami.hqueue.data.Queue;
import dev.lugami.hqueue.data.QueuePlayer;
import dev.lugami.hqueue.hQueue;
import dev.lugami.hqueue.payloads.handler.Packet;

import java.beans.ConstructorProperties;
import java.util.UUID;

public class JoinPacket implements Packet {

    private UUID player;
    private String queueName;

    public JoinPacket(UUID player, Queue queue) {
        this.player = player;
        this.queueName = queue.getQueueName();
    }

    @Override
    public void onReceive() {
        Queue queue = hQueue.getQueueManager().getQueue(this.queueName);
        if (queue == null) {
            return;
        }
        queue.getOfflinePlayersInQueue().remove(this.player);
        queue.getPlayersInQueue().add(new QueuePlayer(this.player, System.currentTimeMillis()));
    }

    @ConstructorProperties(value={"player", "queueName"})
    public JoinPacket(UUID player, String queueName) {
        this.player = player;
        this.queueName = queueName;
    }
}

