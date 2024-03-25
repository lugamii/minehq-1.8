package dev.lugami.hqueue.command.param;

import dev.lugami.hqueue.data.Queue;
import dev.lugami.hqueue.hQueue;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import dev.lugami.qlib.command.ParameterType;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class QueueParamType implements ParameterType<Queue> {

    public Queue transform(CommandSender sender, String source) {
        Queue queue = hQueue.getQueueManager().getQueue(source);
        if (queue == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hQueue.getInstance().getConfig().getString("lang.queue.doesnt_exist").replace("%queue%", source)));
            return null;
        }
        return queue;
    }

    public List<String> tabComplete(Player sender, Set<String> flags, String source) {
        return hQueue.getQueueManager().getQueues().stream().map(Queue::getQueueName).collect(Collectors.toList());
    }
}

