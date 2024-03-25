package dev.lugami.hqueue.command;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.lugami.hqueue.data.Queue;
import dev.lugami.hqueue.data.QueuePlayer;
import dev.lugami.hqueue.hQueue;
import dev.lugami.hqueue.payloads.LeaveQueuePacket;
import dev.lugami.hqueue.payloads.QueuePausePacket;
import dev.lugami.hqueue.payloads.QueueUnPausePacket;
import dev.lugami.hqueue.payloads.handler.PacketHandler;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import dev.lugami.bridge.bukkit.util.Chat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dev.lugami.bridge.BridgeGlobal;
import dev.lugami.bridge.global.profile.Profile;
import dev.lugami.qlib.command.Command;
import dev.lugami.qlib.command.Param;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import dev.lugami.qlib.menu.Button;
import dev.lugami.qlib.menu.pagination.PaginatedMenu;
import dev.lugami.qlib.uuid.FrozenUUIDCache;

public class QueueCommands {

    @Command(names = {"leavequeue", "lq"}, permission = "")
    public static void leavequeue(Player p) {
        Queue q = hQueue.getQueueManager().getPlayersQueue(p.getUniqueId());
        if (q == null) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', hQueue.getInstance().getConfig().getString("lang.queue.not_queued")));
            return;
        }
        PacketHandler.send(new LeaveQueuePacket(p.getUniqueId(), q, ChatColor.translateAlternateColorCodes('&', hQueue.getInstance().getConfig().getString("lang.queue.left").replace("%queue%", q.getQueueName())), Bukkit.getServerName()));
    }

    @Command(names = {"queue create"}, permission = "hqueue.create", hidden = true)
    public static void create(CommandSender s, @Param(name = "name") String name) {
        Queue queue = hQueue.getQueueManager().getQueue(name);
        if (queue != null) {
            s.sendMessage(ChatColor.translateAlternateColorCodes('&', hQueue.getInstance().getConfig().getString("lang.queue.already_exists").replace("%queue%", queue.getQueueName())));
            return;
        }
        hQueue.getQueueManager().createQueue(name);
        s.sendMessage(ChatColor.translateAlternateColorCodes('&', hQueue.getInstance().getConfig().getString("lang.queue.created").replace("%queue%", name)));
    }

    @Command(names = {"queue delete"}, permission = "hqueue.delete", hidden = true)
    public static void delete(CommandSender s, @Param(name = "queue") Queue queue) {
        queue.delete();
        s.sendMessage(ChatColor.translateAlternateColorCodes('&', hQueue.getInstance().getConfig().getString("lang.queue.deleted").replace("%queue%", queue.getQueueName())));
    }

    @Command(names = {"queue pause"}, permission = "hqueue.pause", hidden = true)
    public static void pause(CommandSender s, @Param(name = "queue") Queue queue) {
        boolean pause = !queue.isPaused();
        PacketHandler.send(new QueuePausePacket(queue, true, Bukkit.getServerName()));
        if (pause) {
            s.sendMessage(ChatColor.translateAlternateColorCodes('&', hQueue.getInstance().getConfig().getString("lang.queue.paused").replace("%queue%", queue.getQueueName())));
        } else {
            PacketHandler.send(new QueueUnPausePacket(queue, false, Bukkit.getServerName()));
            s.sendMessage(ChatColor.translateAlternateColorCodes('&', hQueue.getInstance().getConfig().getString("lang.queue.unpaused").replace("%queue%", queue.getQueueName())));
        }
    }

    @Command(names = {"joinqueue"}, permission = "")
    public static void join(Player player, @Param(name = "queue") Queue queue) {
        hQueue.getQueueManager().addPlayer(player, queue.getQueueName());
    }

    @Command(names = {"queue list"}, permission = "hqueue.list", hidden = true, async = true)
    public static void list(CommandSender s) {
        s.sendMessage(org.bukkit.ChatColor.DARK_PURPLE.toString() + org.bukkit.ChatColor.STRIKETHROUGH + "---------------------------------------------");
        for (String s1 : hQueue.getInstance().getConfig().getStringList("lang.queue.list_title")) {
            s.sendMessage(ChatColor.translateAlternateColorCodes('&', s1));
        }
        if (hQueue.getQueueManager().getQueues().isEmpty()) {
            s.sendMessage(ChatColor.translateAlternateColorCodes('&', hQueue.getInstance().getConfig().getString("lang.queue.no_queues")));
            s.sendMessage(org.bukkit.ChatColor.DARK_PURPLE.toString() + org.bukkit.ChatColor.STRIKETHROUGH + "---------------------------------------------");
        } else {
            hQueue.getQueueManager().getQueues().forEach(queue -> {
                ArrayList inQueue = new ArrayList();
                ArrayList offlineQueue = new ArrayList();
                queue.getPlayersInQueue().forEach(uuid -> {
                    Profile profile = BridgeGlobal.getProfileHandler().getProfileByUUID(uuid.getUuid());
                    if (profile != null) {
                        inQueue.add(profile.getCurrentGrant().getRank().getColor() + profile.getUsername());
                    } else {
                        inQueue.add(uuid.getUuid().toString());
                    }
                });
                queue.getOfflinePlayersInQueue().keySet().forEach(uuid -> {
                    Profile profile = BridgeGlobal.getProfileHandler().getProfileByUUID(uuid);
                    if (profile != null) {
                        offlineQueue.add(profile.getCurrentGrant().getRank().getColor() + profile.getUsername());
                    } else {
                        offlineQueue.add(uuid.toString());
                    }
                });
                for (String s1 : hQueue.getInstance().getConfig().getStringList("lang.queue.list_queue")) {
                    if (!queue.isPaused()) {
                        s.sendMessage(ChatColor.translateAlternateColorCodes('&', s1.replace("%queue%", queue.getQueueName()).replace("%paused%", ("")).replace("%inqueue%", StringUtils.join(inQueue, (ChatColor.WHITE + ", "))).replace("%offlinequeue%", StringUtils.join(offlineQueue, (ChatColor.WHITE + ", ")))));
                    } else {
                        s.sendMessage(ChatColor.translateAlternateColorCodes('&', s1.replace("%queue%", queue.getQueueName()).replace("%paused%", ("&7[Paused]")).replace("%inqueue%", StringUtils.join(inQueue, (ChatColor.WHITE + ", "))).replace("%offlinequeue%", StringUtils.join(offlineQueue, (ChatColor.WHITE + ", ")))));
                    }
                }
            });
            s.sendMessage(org.bukkit.ChatColor.DARK_PURPLE.toString() + org.bukkit.ChatColor.STRIKETHROUGH + "---------------------------------------------");
        }
    }

    @Command(names = {"queue managequeues"}, permission = "op", hidden = true, async = true)
    public static void queues(Player player) {
        new PaginatedMenu() {

            @Override
            public String getPrePaginatedTitle(Player player) {
                return "Queues";
            }

            @Override
            public Map<Integer, Button> getAllPagesButtons(Player player) {
                Map<Integer, Button> buttons = Maps.newHashMap();
                int index = 0;
                for (Queue queueTarget : hQueue.getQueueManager().getQueues()) {

                    queueTarget.getPlayersInQueue().stream().sorted((o1, o2) -> BridgeGlobal.getProfileHandler().getProfileByUUID(o2.getUuid()).getCurrentGrant().getRank().getPriority() - BridgeGlobal.getProfileHandler().getProfileByUUID(o1.getUuid()).getCurrentGrant().getRank().getPriority());

                    buttons.put(index, new Button() {
                        @Override
                        public String getName(Player player) {
                            return Chat.format((queueTarget.isPaused() ? org.bukkit.ChatColor.RED + queueTarget.getQueueName() : org.bukkit.ChatColor.GREEN + queueTarget.getQueueName()));
                        }

                        @Override
                        public List<String> getDescription(Player player) {
                            List<String> description = Lists.newArrayList();
                            description.add(Chat.format("&7&m" + Chat.LINE));
                            description.add(Chat.format("&6In Queue&7: &r" + queueTarget.getPlayersInQueue().size()));
                            description.add(Chat.format("&6Paused&7: &r" + (queueTarget.isPaused() ? "&atrue" : "&cfalse")));
                            description.add(Chat.format("&7&m" + Chat.LINE));
                            return description;
                        }

                        @Override
                        public Material getMaterial(Player player) {
                            return (queueTarget.isPaused() ? Material.REDSTONE_BLOCK : Material.EMERALD_BLOCK);
                        }

                        @Override
                        public void clicked(Player player, int slot, ClickType clickType) {
                            new PaginatedMenu() {

                                @Override
                                public boolean isAutoUpdate() {
                                    return true;
                                }

                                @Override
                                public String getPrePaginatedTitle(Player player) {
                                    return Chat.format("Queued Players");
                                }

                                @Override
                                public Map<Integer, Button> getAllPagesButtons(Player player) {
                                    Map<Integer, Button> buttons = Maps.newHashMap();
                                    int index = 0;
                                    for (QueuePlayer target : queueTarget.getPlayersInQueue()) {
                                        buttons.put(index, new Button() {
                                            @Override
                                            public String getName(Player player) {
                                                return FrozenUUIDCache.name(target.getUuid());
                                            }

                                            @Override
                                            public List<String> getDescription(Player player) {
                                                List<String> description = Lists.newArrayList();
                                                description.add(Chat.format("&7&m" + Chat.LINE));
                                                description.add(Chat.format("&6Position&7: &r") + hQueue.getQueueManager().getPlayersQueue(player.getUniqueId()).getPosition(target.getUuid()));
                                                description.add(Chat.format("&r"));
                                                description.add(Chat.format("&c&lClick to remove from queue."));
                                                description.add(Chat.format("&7&m" + Chat.LINE));
                                                return description;
                                            }

                                            @Override
                                            public Material getMaterial(Player player) {
                                                return Material.SKULL_ITEM;
                                            }

                                            @Override
                                            public void clicked(Player player, int slot, ClickType clickType) {
                                                queueTarget.getPlayersInQueue().remove(target);
                                                player.sendMessage(Chat.format("&aSuccessfully removed &r" + FrozenUUIDCache.name(target.getUuid()) + "&r &afrom the queue."));
                                            }
                                        });
                                        index++;
                                    }
                                    return buttons;
                                }
                            }.openMenu(player);
                        }
                    });
                    index++;
                }
                return buttons;
            }
        }.openMenu(player);
    }
}