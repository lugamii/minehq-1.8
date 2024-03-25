package dev.lugami.hqueue.listeners;

import dev.lugami.hqueue.data.Queue;
import dev.lugami.hqueue.payloads.JoinPacket;
import dev.lugami.hqueue.payloads.LeavePacket;
import dev.lugami.hqueue.payloads.LeaveQueuePacket;
import dev.lugami.hqueue.payloads.handler.PacketHandler;
import dev.lugami.hqueue.hQueue;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class QueueListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        Queue q = hQueue.getQueueManager().getPlayersQueue(p.getUniqueId());
        if (q != null) {
            PacketHandler.send(new JoinPacket(p.getUniqueId(), q));
        }
        if (q != null && q.getQueueName().equals(Bukkit.getServerName())) {
            PacketHandler.send(new LeaveQueuePacket(p.getUniqueId(), q, null, Bukkit.getServerName()));
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        Queue q = hQueue.getQueueManager().getPlayersQueue(p.getUniqueId());
        if (q != null) {
            PacketHandler.send(new LeavePacket(p.getUniqueId(), System.currentTimeMillis(), q, Bukkit.getServerName()));
        }
    }
}

