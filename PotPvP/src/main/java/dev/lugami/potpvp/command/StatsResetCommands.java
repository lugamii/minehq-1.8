package dev.lugami.potpvp.command;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.base.Objects;

import dev.lugami.potpvp.PotPvPSI;
import dev.lugami.qlib.qLib;
import dev.lugami.qlib.command.Command;
import dev.lugami.qlib.command.Param;
import dev.lugami.qlib.menu.menus.ConfirmMenu;
import dev.lugami.qlib.redis.RedisCommand;
import dev.lugami.qlib.util.Callback;
import dev.lugami.qlib.util.UUIDUtils;
import redis.clients.jedis.Jedis;

public class StatsResetCommands {
    private static String REDIS_PREFIX = "PotPvP:statsResetToken:";

    @Command(names = { "statsreset addtoken" }, permission = "op", async = true)
    public static void addToken(CommandSender sender, @Param(name = "player") String playerName, @Param(name = "amount") int amount) {
        UUID uuid = UUIDUtils.uuid(playerName);

        if (uuid == null) {
            sender.sendMessage(ChatColor.RED + "Unable to locate '" + playerName + "'.");
            return;
        }

        addTokens(uuid, amount);
        sender.sendMessage(ChatColor.GREEN + "Added " + amount + " token" + (amount == 1 ? "" : "s") + " to " + UUIDUtils.name(uuid) + ".");
    }

    @Command(names = {"statsreset"}, permission = "", async = true)
    public static void reset(Player sender) {
        int tokens = getTokens(sender.getUniqueId());
        if (tokens <= 0) {
            sender.sendMessage(ChatColor.RED + "You need at least one token to reset your stats.");
            return;
        }

        Bukkit.getScheduler().runTask(PotPvPSI.getInstance(), () -> {
            new ConfirmMenu("Stats reset", new Callback<Boolean>() {

                @Override
                public void callback(Boolean reset) {
                    if (!reset) {
                        sender.sendMessage(ChatColor.RED + "Stats reset aborted.");
                        return;
                    }

                    Bukkit.getScheduler().runTaskAsynchronously(PotPvPSI.getInstance(), () -> {
                        PotPvPSI.getInstance().getEloHandler().resetElo(sender.getUniqueId());
                        removeTokens(sender.getUniqueId(), 1);
                        int tokens = getTokens(sender.getUniqueId());
                        sender.sendMessage(ChatColor.GREEN + "Reset your stats! Used one token. " + tokens + " token" + (tokens == 1 ? "" : "s") + " left.");
                    });
                }
                
            }).openMenu(sender); 
        });
    }

    private static int getTokens(UUID player) {
        return qLib.getInstance().runBackboneRedisCommand(new RedisCommand<Integer>() {

            @Override
            public Integer execute(Jedis redis) {
                return Integer.valueOf(Objects.firstNonNull(redis.get(REDIS_PREFIX + player.toString()), "0"));
            }
            
        });
    }

    private static void addTokens(UUID player, int amountBy) {
        qLib.getInstance().runBackboneRedisCommand(new RedisCommand<Object>() {

            @Override
            public Object execute(Jedis redis) {
                redis.incrBy(REDIS_PREFIX + player.toString(), amountBy);
                return null;
            }
            
        });
    }

    public static void removeTokens(UUID player, int amountBy) {
        qLib.getInstance().runBackboneRedisCommand(new RedisCommand<Object>() {

            @Override
            public Object execute(Jedis redis) {
                redis.decrBy(REDIS_PREFIX + player.toString(), amountBy);
                return null;
            }
            
        });
    }
}
