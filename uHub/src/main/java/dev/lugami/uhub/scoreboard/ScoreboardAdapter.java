package dev.lugami.uhub.scoreboard;

import dev.lugami.uhub.uHub;
import dev.lugami.hqueue.data.QueuePlayer;
import org.bukkit.ChatColor;
import dev.lugami.hqueue.data.Queue;
import dev.lugami.hqueue.hQueue;
import dev.lugami.bridge.BridgeGlobal;
import dev.lugami.bridge.global.profile.Profile;
import dev.lugami.bridge.global.ranks.Rank;
import dev.lugami.qlib.scoreboard.ScoreboardConfiguration;
import dev.lugami.qlib.scoreboard.TitleGetter;
import org.bukkit.entity.Player;

public class ScoreboardAdapter {

    public static ScoreboardConfiguration create() {
        ScoreboardConfiguration cfg = new ScoreboardConfiguration();

        cfg.setTitleGetter(new TitleGetter() {
            @Override
            public String getTitle(Player player) {
                return uHub.getInstance().getConfig().getString("scoreboard.title");

            }
        });

        cfg.setScoreGetter((l, player) -> {
            Queue queue = hQueue.getQueueManager().getPlayersQueue(player.getUniqueId());
            Profile profile = BridgeGlobal.getProfileHandler().getProfileByUUID(player.getUniqueId());
            Rank disguisedRank = null;
            if (player.isDisguised()) {
                if (profile.getDisguisedRank() != null) {
                    disguisedRank = profile.getDisguisedRank();
                } else {
                    disguisedRank = profile.getCurrentGrant().getRank();
                }
            }
            Rank rank = profile.getCurrentGrant().getRank();
            if (!isInQueue(player)) {
                for (String line : uHub.getInstance().getConfig().getStringList("scoreboard.normal")) {
                    if (disguisedRank != null) {
                        line = line.replaceAll("%rank%", uHub.getInstance().getConfig().getBoolean("scoreboard.rankcolored") ? disguisedRank.getColor() + disguisedRank.getDisplayName() : disguisedRank.getDisplayName());
                    } else if (rank == null) {
                        line = line.replaceAll("%rank%", ChatColor.WHITE + "Unknown");
                    } else {
                        line = line.replaceAll("%rank%", uHub.getInstance().getConfig().getBoolean("scoreboard.rankcolored") ? rank.getColor() + rank.getDisplayName() : rank.getDisplayName());
                    }
                    line = line.replaceAll("%players%", "" + uHub.getInstance().getPlayerCountBungee());
                    l.add(line);
                }
            } else {
                for (String line : uHub.getInstance().getConfig().getStringList("scoreboard.queue")) {
                    if (disguisedRank != null) {
                        line = line.replaceAll("%rank%", uHub.getInstance().getConfig().getBoolean("scoreboard.rankcolored") ? disguisedRank.getColor() + disguisedRank.getDisplayName() : disguisedRank.getDisplayName());
                    } else if (rank == null) {
                        line = line.replaceAll("%rank%", ChatColor.WHITE + "Unknown");
                    } else {
                        line = line.replaceAll("%rank%", uHub.getInstance().getConfig().getBoolean("scoreboard.rankcolored") ? rank.getColor() + rank.getDisplayName() : rank.getDisplayName());
                        line = line.replaceAll("%players%", "" + uHub.getInstance().getPlayerCountBungee());
                        line = line.replaceAll("%queue%", queue.getQueueName()).replaceAll("%place%", queue.getPosition(player.getUniqueId()) + "").replaceAll("%total%", queue.getPlayersInQueue().size() + "");
                        l.add(line);
                    }
                }
            }
        });

        return cfg;
    }

    public static boolean isInQueue(Player player) {
        for (Queue queue : hQueue.getQueueManager().getQueues()) {
            for (QueuePlayer queuePlayer : queue.getPlayersInQueue()) {
                if (!queuePlayer.getUuid().equals(player.getUniqueId())) {
                    continue;
                } else {
                    return true;
                }
            }
        }
        return false;
    }

}