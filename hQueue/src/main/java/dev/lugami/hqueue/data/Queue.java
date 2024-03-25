package dev.lugami.hqueue.data;

import dev.lugami.hqueue.payloads.LeaveQueuePacket;
import dev.lugami.hqueue.payloads.handler.PacketHandler;
import dev.lugami.hqueue.hQueue;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.*;

import dev.lugami.bridge.BridgeGlobal;
import dev.lugami.bridge.global.profile.Profile;
import dev.lugami.bridge.global.ranks.Rank;
import dev.lugami.qlib.util.TimeUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Queue {
    private UUID uuid;
    private String queueName;
    private boolean paused = false;
    private transient PriorityQueue<QueuePlayer> playersInQueue;
    private transient PriorityQueue<Profile> players;
    private transient HashMap<UUID, Long> offlinePlayersInQueue;

    public Queue(String queueName) {
        this.uuid = UUID.randomUUID();
        this.queueName = queueName;
        this.playersInQueue = new PriorityQueue<QueuePlayer>(new QueueComparator());
        this.offlinePlayersInQueue = new HashMap();
        this.queueTimer();
    }

    public Queue(UUID uuid, String queueName) {
        this.uuid = uuid;
        this.queueName = queueName;
        this.playersInQueue = new PriorityQueue<QueuePlayer>(new QueueComparator());
        this.offlinePlayersInQueue = new HashMap();
        this.queueTimer();
    }

    public Queue(UUID uuid, String queueName, boolean paused) {
        this.uuid = uuid;
        this.queueName = queueName;
        this.paused = paused;
        this.playersInQueue = new PriorityQueue<QueuePlayer>(new QueueComparator());
        this.offlinePlayersInQueue = new HashMap();
        this.queueTimer();
    }

    public void queueTimer() {
        new BukkitRunnable(){

            public void run() {
                for (QueuePlayer queuePlayer : Queue.this.getPlayersInQueue()) {
                    UUID pid = queuePlayer.getUuid();
                    OfflinePlayer op = Bukkit.getOfflinePlayer(pid);
                    if (op == null || !op.isOnline()) continue;
                    Queue.this.sendInfo(pid);
                }
            }
        }.runTaskTimer(hQueue.getInstance(), 300L, 300L);
        Bukkit.getScheduler().runTaskTimer(hQueue.getInstance(), () -> {
            UUID uuid;
            Long time;
            Iterator<UUID> kekistani = this.offlinePlayersInQueue.keySet().iterator();
            if (kekistani.hasNext() && (time = this.offlinePlayersInQueue.get(kekistani.next())) + TimeUtil.parseTime("15s") <= System.currentTimeMillis()) {
                kekistani.remove();
            }
        }, 0L, 40L);
    }

    public void sendInfo(UUID pid) {
        Player p = Bukkit.getPlayer(pid);
        if (p == null) {
            return;
        }
        String rank = "N/A";
        Profile profile = BridgeGlobal.getProfileHandler().getProfileByUUID(p.getUniqueId());
        if (profile != null) {
            rank = profile.getCurrentGrant().getRank().getColor() + profile.getCurrentGrant().getRank().getDisplayName();
        }
            Profile pf = BridgeGlobal.getProfileHandler().getProfileByUUID(p.getUniqueId());
            Rank r = pf.getCurrentGrant().getRank();
            p.sendMessage("");
            p.sendMessage(net.md_5.bungee.api.ChatColor.LIGHT_PURPLE + "You are position " + net.md_5.bungee.api.ChatColor.YELLOW + "#" + getPosition(p.getUniqueId()) + net.md_5.bungee.api.ChatColor.LIGHT_PURPLE + " in the "+ net.md_5.bungee.api.ChatColor.YELLOW + getQueueName() + net.md_5.bungee.api.ChatColor.LIGHT_PURPLE + " queue.");
            if(!r.isDefaultRank() && r.getPriority() > 19) {
                p.sendMessage(net.md_5.bungee.api.ChatColor.AQUA + "Your " + rank + net.md_5.bungee.api.ChatColor.AQUA + " rank has placed you in front of " +  Math.subtractExact(getPlayersInQueue().size(), getPosition(p.getUniqueId())) + " players!");
            } else if (!r.isDefaultRank() && r.getPriority() < 20) {
                p.sendMessage(net.md_5.bungee.api.ChatColor.AQUA + "Your " + rank + net.md_5.bungee.api.ChatColor.AQUA + " rank has placed you in front of " + Math.subtractExact(getPlayersInQueue().size(), getPosition(p.getUniqueId())) + " players. " + ChatColor.RED + "You can upgrade your rank at " + ChatColor.LIGHT_PURPLE + "store.minehq.org/upgrades!");
            } else if (r.isDefaultRank()) {
                p.sendMessage( net.md_5.bungee.api.ChatColor.AQUA + "You can upgrade to " + net.md_5.bungee.api.ChatColor.GOLD + "PRO " + net.md_5.bungee.api.ChatColor.AQUA + "to skip to the front of the queue." + net.md_5.bungee.api.ChatColor.RED + " You can purchase a rank at " + ChatColor.LIGHT_PURPLE + "store.minehq.org");
            }
            String thanksFor = "???";
            switch(r.getName()) {
                case "YouTuber":
                    thanksFor = "YouTuber";
                    break;

                case "Famous":
                    thanksFor = "Famous YouTuber";
                    break;
                case "Partner":
                    thanksFor = "Partner";
                    break;
                default:
                    break;
            }


            if(!thanksFor.equals("???")) {
                p.sendMessage(net.md_5.bungee.api.ChatColor.AQUA + "Thanks for being a " + thanksFor + " ;)");
            }
            p.sendMessage(ChatColor.GOLD + "To leave the queue, use /leavequeue");
            p.sendMessage("");

        }

    public boolean isAvailable(UUID player) {
        if (BridgeGlobal.getServerHandler().getServer().getName() == null) {
            return false;
        }
        if (!BridgeGlobal.getServerHandler().getServer().isOnline()) {
            return false;
        }
        Profile profile = BridgeGlobal.getProfileHandler().getProfileByUUID(player);
        boolean isStaff = false;
        if (profile != null) {
            boolean bl = isStaff = profile.getCurrentGrant().getRank().isStaff();
        }
        if (BridgeGlobal.getServerHandler().getServer().isWhitelisted()) {
            return isStaff;
        }
        if (this.isPaused()) {
            return isStaff;
        }
        return true;
    }

    public Long getEstimatedTime(UUID uuid) {
        return (this.getPosition(uuid) * 2L) * 1000L;
    }

    public void delete() {
        hQueue.getQueueManager().getQueues().remove(this);
    }

    public QueuePlayer getPlayerAt(int i) {
        return this.getPlayersInQueue().stream().filter(queuePlayer -> this.getPosition(queuePlayer.getUuid()) == i).findFirst().orElse(null);
    }

    public boolean containsPlayer(UUID uuid) {
        for (QueuePlayer player : this.playersInQueue) {
            if (!player.getUuid().equals(uuid)) continue;
            return true;
        }
        return false;
    }

    public int getPosition(UUID uuid) {
        QueuePlayer player;
        if (!this.containsPlayer(uuid)) {
            return 0;
        }
        PriorityQueue<QueuePlayer> queue = new PriorityQueue<>(this.playersInQueue);
        int position = 0;
        while (!queue.isEmpty() && !queue.poll().getUuid().equals(uuid)) {
            ++position;
        }
        return position + 1;
    }

    public void sendFirst() {
        UUID qp = this.getPlayerAt(1).getUuid();
        Player p = Bukkit.getPlayer(qp);
        if (p == null) {
            PacketHandler.send(new LeaveQueuePacket(qp, this, null, Bukkit.getServerName()));
            return;
        }
        this.sendPlayerToServer(p, this.getQueueName());
        PacketHandler.send(new LeaveQueuePacket(p.getUniqueId(), this, null, Bukkit.getServerName()));
    }

    public String queueStatus(UUID uuid) {
        Profile profile = BridgeGlobal.getProfileHandler().getProfileByUUID(uuid);
        boolean isStaff = false;
        if (profile != null) {
            boolean bl = isStaff = profile.getCurrentGrant().getRank().isStaff();
        }
        if (BridgeGlobal.getServerHandler().getServer().getName() == null || !BridgeGlobal.getServerHandler().getServer().isOnline()) {
            return "Offline";
        }
        if (BridgeGlobal.getServerHandler().getServer().isWhitelisted()) {
            return "Whitelisted";
        }
        if (this.isPaused() && !isStaff) {
            return "Paused";
        }
        return TimeUtil.millisToRoundedTime(this.getEstimatedTime(uuid));
    }

    public void sendDirect(Player p) {
        this.sendPlayerToServer(p, this.getQueueName());
    }

    public void sendPlayerToServer(Player player, String server) {
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            out.writeUTF("Connect");
            out.writeUTF(server);
            player.sendPluginMessage(hQueue.getInstance(), "BungeeCord", b.toByteArray());
            b.close();
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getQueueName() {
        return this.queueName;
    }

    public boolean isPaused() {
        return this.paused;
    }

    public PriorityQueue<QueuePlayer> getPlayersInQueue() {
        return this.playersInQueue;
    }

    public PriorityQueue<Profile> getPlayers() {
        return this.players;
    }

    public HashMap<UUID, Long> getOfflinePlayersInQueue() {
        return this.offlinePlayersInQueue;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public void setPlayersInQueue(PriorityQueue<QueuePlayer> playersInQueue) {
        this.playersInQueue = playersInQueue;
    }

    public void setOfflinePlayersInQueue(HashMap<UUID, Long> offlinePlayersInQueue) {
        this.offlinePlayersInQueue = offlinePlayersInQueue;
    }
}

