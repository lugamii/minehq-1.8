package dev.lugami.hqueue.payloads;

import dev.lugami.hqueue.data.Queue;
import dev.lugami.hqueue.hQueue;
import dev.lugami.hqueue.payloads.handler.Packet;

import java.beans.ConstructorProperties;

public class QueueDeletePacket implements Packet {

    private String queueName;
    private String sender;

    public QueueDeletePacket(Queue queue, String sender) {
        this.queueName = queue.getQueueName();
        this.sender = sender;
    }

    @Override
    public void onReceive() {
        Queue queue = hQueue.getQueueManager().getQueue(this.queueName);
        if (queue == null) {
            return;
        }
        queue.delete();
    }

    @ConstructorProperties(value={"queueName", "sender"})
    public QueueDeletePacket(String queueName, String sender) {
        this.queueName = queueName;
        this.sender = sender;
    }
}

