package dev.lugami.hqueue.data;

import dev.lugami.hqueue.payloads.JoinQueuePacket;
import dev.lugami.hqueue.payloads.QueueCreatePacket;
import dev.lugami.hqueue.hQueue;
import dev.lugami.hqueue.payloads.handler.PacketHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dev.lugami.bridge.BridgeGlobal;
import dev.lugami.bridge.global.profile.Profile;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class QueueManager {

    public List<dev.lugami.hqueue.data.Queue> queues = new ArrayList<dev.lugami.hqueue.data.Queue>();

    public QueueManager() {
        hQueue.getInstance().runRedisCommand(redis -> {
            if (redis.exists("hqueue:queues")) {
                List<dev.lugami.hqueue.data.Queue> serializedQueues = new Gson().fromJson(redis.get("hqueue:queues"), new TypeToken<List<dev.lugami.hqueue.data.Queue>>() {
                }.getType());
                for (dev.lugami.hqueue.data.Queue sQueue : serializedQueues) {
                    if (sQueue == null) continue;
                    sQueue.setPlayersInQueue(new PriorityQueue<>(new QueueComparator()));
                    sQueue.setOfflinePlayersInQueue(new HashMap<>());
                    sQueue.queueTimer();
                    this.queues.add(sQueue);
                    Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[hQueue] Loaded the queue: " + sQueue.getQueueName());
                }
            }
            return null;
        });
        Bukkit.getScheduler().runTaskTimerAsynchronously(hQueue.getInstance(), this::saveQueues, 0L, 120L);
    }

    public void saveQueues() {
        hQueue.getInstance().runRedisCommand(redis -> {
            String serializedQueues = new Gson().toJson(this.queues);
            redis.set("hqueue:queues", serializedQueues);
            return null;
        });
    }

    public void startQueues() {
        new BukkitRunnable() {

            public void run() {
                for (dev.lugami.hqueue.data.Queue q : QueueManager.this.queues) {
                    if (q.getPlayersInQueue().isEmpty() || q.getPlayerAt(1) == null || Bukkit.getPlayer(q.getPlayerAt(1).getUuid()) == null)
                        continue;
                    UUID uuid = q.getPlayerAt(1).getUuid();
                    Profile profile = BridgeGlobal.getProfileHandler().getProfileByUUID(uuid);
                    boolean isStaff = false;
                    if (profile != null) {
                        boolean bl = isStaff = profile.getCurrentGrant().getRank().isStaff();
                    }
                    if ((BridgeGlobal.getServerHandler().getServer().getName() == null || !BridgeGlobal.getServerHandler().getServer().isOnline() || (q.isPaused() || BridgeGlobal.getServerHandler().getServer().isWhitelisted() && !isStaff || !Bukkit.getPlayer(q.getPlayerAt(1).getUuid()).isOnline() || !q.isAvailable(q.getPlayerAt(1).getUuid()))))
                        continue;
                    q.sendFirst();
                }
            }
        }.runTaskTimer(hQueue.getInstance(), 0L, 20L);
    }

    public dev.lugami.hqueue.data.Queue getPlayersQueue(UUID uuid) {
        for (dev.lugami.hqueue.data.Queue queue : this.queues) {
            for (QueuePlayer queuePlayer : queue.getPlayersInQueue()) {
                if (!queuePlayer.getUuid().equals(uuid)) continue;
                return queue;
            }
        }
        return null;
    }

    public boolean isQueue(String s) {
        return this.queues.contains(new dev.lugami.hqueue.data.Queue(s));
    }

    public dev.lugami.hqueue.data.Queue getQueue(String s) {
        for (dev.lugami.hqueue.data.Queue q : this.queues) {
            if (!q.getQueueName().equalsIgnoreCase(s)) continue;
            return q;
        }
        return null;
    }

    public dev.lugami.hqueue.data.Queue getQueue(UUID uuid) {
        for (dev.lugami.hqueue.data.Queue q : this.queues) {
            if (!q.getUuid().toString().equals(uuid.toString())) continue;
            return q;
        }
        return null;
    }

    public void createQueue(String name) {
        UUID uuid = UUID.randomUUID();
        this.queues.add(new dev.lugami.hqueue.data.Queue(uuid, name));
        PacketHandler.send(new QueueCreatePacket(uuid, name, Bukkit.getServerName()));
    }

    public void addPlayer(Player p, String queue) {
        dev.lugami.hqueue.data.Queue q = hQueue.getQueueManager().getQueue(queue);
        if (q == null) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', hQueue.getInstance().getConfig().getString("lang.queue.doesnt_exist").replace("%queue%", queue)));
            return;
        }
        if (hQueue.getQueueManager().getPlayersQueue(p.getUniqueId()) != null) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', hQueue.getInstance().getConfig().getString("lang.queue.alreadyqueued").replace("%queue%", hQueue.getQueueManager().getPlayersQueue(p.getUniqueId()).getQueueName())));
            return;
        }

        q.getPlayersInQueue().add(new QueuePlayer(p.getUniqueId(), System.currentTimeMillis()));
        p.sendMessage((hQueue.getQueueManager().getPlayersQueue(p.getUniqueId()).getQueueName().endsWith("-Lobby") ? ChatColor.translateAlternateColorCodes('&', hQueue.getInstance().getConfig().getString("lang.queue.joinedplayerqueue").replace("%queue%", hQueue.getQueueManager().getPlayersQueue(p.getUniqueId()).getQueueName())) : ChatColor.translateAlternateColorCodes('&', hQueue.getInstance().getConfig().getString("lang.queue.joinedqueue").replace("%queue%", q.getQueueName()))));
        PacketHandler.send(new JoinQueuePacket(p.getUniqueId(), q, Bukkit.getServerName()));
        if (q.getPlayerAt(2) == null && q.isAvailable(p.getUniqueId()) && !q.isPaused()) {
            q.sendFirst();
        }
    }

    public List<Queue> getQueues() {
        return this.queues;
    }
}

