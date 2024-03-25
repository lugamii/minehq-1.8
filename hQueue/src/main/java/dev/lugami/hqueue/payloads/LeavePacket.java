package dev.lugami.hqueue.payloads;

import dev.lugami.hqueue.data.Queue;
import dev.lugami.hqueue.hQueue;
import dev.lugami.hqueue.payloads.handler.Packet;

import java.beans.ConstructorProperties;
import java.util.UUID;

public class LeavePacket implements Packet {

    private UUID player;
    private Long leftTime;
    private String queueName;
    private String sender;

    public LeavePacket(UUID player, Long leftTime, Queue queue, String sender) {
        this.player = player;
        this.leftTime = leftTime;
        this.queueName = queue.getQueueName();
        this.sender = sender;
    }

    @Override
    public void onReceive() {
        Queue queue = hQueue.getQueueManager().getQueue(this.queueName);
        if (queue == null) {
            return;
        }
        queue.getPlayersInQueue().remove(queue.getPlayersInQueue().stream().filter(queuePlayer -> queuePlayer.getUuid().equals(this.player)).findFirst().orElse(null));
        queue.getOfflinePlayersInQueue().put(this.player, this.leftTime);
    }

    @ConstructorProperties(value={"player", "leftTime", "queueName", "sender"})
    public LeavePacket(UUID player, Long leftTime, String queueName, String sender) {
        this.player = player;
        this.leftTime = leftTime;
        this.queueName = queueName;
        this.sender = sender;
    }
}

