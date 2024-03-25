package dev.lugami.hqueue.payloads;

import dev.lugami.hqueue.data.Queue;
import dev.lugami.hqueue.hQueue;
import dev.lugami.hqueue.payloads.handler.Packet;

import java.beans.ConstructorProperties;
import java.util.UUID;

import org.bukkit.Bukkit;

public class LeaveQueuePacket implements Packet {

    private UUID uuid;
    private String queueName;
    private String reason;
    private String sender;

    public LeaveQueuePacket(UUID uuid, Queue queue, String reason, String sender) {
        this.uuid = uuid;
        this.queueName = queue.getQueueName();
        this.reason = reason;
        this.sender = sender;
    }

    @Override
    public void onReceive() {
        Queue queue = hQueue.getQueueManager().getQueue(this.queueName);
        if (queue == null) {
            return;
        }
        queue.getPlayersInQueue().remove(queue.getPlayersInQueue().stream().filter(queuePlayer -> queuePlayer.getUuid().equals(this.uuid)).findAny().orElse(null));
        if (Bukkit.getOfflinePlayer((UUID)this.uuid).isOnline() && this.reason != null) {
            Bukkit.getPlayer((UUID)this.uuid).sendMessage(this.reason);
        }
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getQueueName() {
        return this.queueName;
    }

    public String getReason() {
        return this.reason;
    }

    public String getSender() {
        return this.sender;
    }

    @ConstructorProperties(value={"uuid", "queueName", "reason", "sender"})
    public LeaveQueuePacket(UUID uuid, String queueName, String reason, String sender) {
        this.uuid = uuid;
        this.queueName = queueName;
        this.reason = reason;
        this.sender = sender;
    }
}

