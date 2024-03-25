package dev.lugami.hqueue.payloads;

import dev.lugami.hqueue.data.Queue;
import dev.lugami.hqueue.hQueue;
import dev.lugami.hqueue.payloads.handler.Packet;

import java.beans.ConstructorProperties;

public class QueuePausePacket implements Packet {

    private String queueName;
    private boolean pause;
    private String sender;

    public QueuePausePacket(Queue queue, boolean pause, String sender) {
        this.queueName = queue.getQueueName();
        this.pause = pause;
        this.sender = sender;
    }

    @Override
    public void onReceive() {
        Queue queue = hQueue.getQueueManager().getQueue(this.queueName);
        if (queue == null) {
            return;
        }
        queue.setPaused(true);
    }

    @ConstructorProperties(value={"queueName", "pause", "sender"})
    public QueuePausePacket(String queueName, boolean pause, String sender) {
        this.queueName = queueName;
        this.pause = pause;
        this.sender = sender;
    }
}

