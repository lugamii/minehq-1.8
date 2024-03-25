package dev.lugami.bridge.bukkit.commands;

import com.google.common.collect.Maps;
import net.minecraft.server.v1_8_R3.DedicatedServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import dev.lugami.qlib.command.Command;

import java.util.HashMap;
import java.util.Set;

public class ThreadProfilerCommands {

    @Command(names = {"threadprofiler list"}, permission = "op", hidden = true, async = true)
    public static void threadprofilerlist(CommandSender sender) {
        Set<Thread> threads = Thread.getAllStackTraces().keySet();
        for (Thread thread : threads) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes
                    ('&', " &6* &e"
                            + thread.getName() + " &d(State: "
                            + (thread.getState()) + ", Priority: "
                            + thread.getPriority() + ")"));
        }

    }

    @Command(names = {"threadprofiler gc"}, permission = "op", hidden = true, async = true)
    public static void threadprofilergc(CommandSender sender) {
        Runtime runtime = Runtime.getRuntime();
        sender.sendMessage(ChatColor.YELLOW + "Trying to run Java garbage collector to free up memory.");
        long before = System.currentTimeMillis();
        runtime.gc();
        long after = System.currentTimeMillis();
        sender.sendMessage(ChatColor.GOLD
                + "* "
                + ChatColor.YELLOW + "Finished! Took "
                + ChatColor.LIGHT_PURPLE
                + (after - before)
                + "ms");

    }

    @Command(names = {"threadprofiler threads"}, permission = "op", hidden = true, async = true)
    public static void threadprofilerthreads(CommandSender sender) {
        Set<Thread> threads = Thread.getAllStackTraces().keySet();
        Runtime runtime = Runtime.getRuntime();
        HashMap<Plugin, Integer> pending = Maps.newHashMap();
        for (BukkitTask worker : Bukkit.getScheduler().getPendingTasks()) {
            pending.put(worker.getOwner(), pending.getOrDefault(worker.getOwner(), 0) + 1);
        }
        sender.sendMessage(ChatColor.GOLD + "Alive Threads: " + ChatColor.LIGHT_PURPLE + Thread.getAllStackTraces().keySet().parallelStream().filter(Thread::isAlive).count());
        sender.sendMessage(ChatColor.GOLD + "Daemon Threads: " + ChatColor.LIGHT_PURPLE + Thread.getAllStackTraces().keySet().parallelStream().filter(Thread::isDaemon).count());
        sender.sendMessage(ChatColor.GOLD + "Interrupted Threads: " + ChatColor.LIGHT_PURPLE + Thread.getAllStackTraces().keySet().parallelStream().filter(Thread::isInterrupted).count());
        sender.sendMessage(ChatColor.GOLD + "Active Workers: " + ChatColor.LIGHT_PURPLE + Bukkit.getScheduler().getActiveWorkers().size());
        sender.sendMessage(ChatColor.GOLD + "Pending Tasks: " + ChatColor.LIGHT_PURPLE + Bukkit.getScheduler().getPendingTasks().size());
        sender.sendMessage(ChatColor.GOLD + "Threads: " + ChatColor.YELLOW + "(" + threads.size() + " Active) (Ram " + ChatColor.LIGHT_PURPLE + format(runtime.freeMemory()) + " free out of " + ChatColor.LIGHT_PURPLE + format(runtime.maxMemory()) + " " + ChatColor.LIGHT_PURPLE + format(runtime.maxMemory() - runtime.freeMemory()) + " used" + ChatColor.GOLD + ")");
        sender.sendMessage("");
        pending.keySet().stream().sorted((o1, o2) -> pending.get(o2) - pending.get(o1)).forEachOrdered(plugin -> sender.sendMessage(ChatColor.RED + "* " + ChatColor.YELLOW + plugin.getName() + ": " + ChatColor.WHITE + pending.get(plugin)));

    }

    private static String format(double tps) {
        return ((tps / 2 > 18.0) ? ChatColor.GREEN : (tps / 2 > 16.0) ? ChatColor.YELLOW : ChatColor.RED).toString()
                + ((tps / 2 > 20.0) ? "*" : "") + Math.min(Math.round(tps * 100.0) / 100.0, DedicatedServer.TPS);
    }
}
