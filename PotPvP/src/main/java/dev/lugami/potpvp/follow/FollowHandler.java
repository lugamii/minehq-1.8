package dev.lugami.potpvp.follow;

import dev.lugami.potpvp.PotPvPSI;
import dev.lugami.potpvp.follow.listener.FollowGeneralListener;
import dev.lugami.potpvp.match.Match;
import dev.lugami.potpvp.match.MatchHandler;
import dev.lugami.potpvp.match.MatchState;
import dev.lugami.potpvp.util.InventoryUtils;
import dev.lugami.potpvp.util.VisibilityUtils;
import dev.lugami.qlib.nametag.FrozenNametagHandler;
import dev.lugami.qlib.util.UUIDUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class FollowHandler {

    // (follower -> target)
    private final Map<UUID, UUID> followingData = new ConcurrentHashMap<>();

    public FollowHandler() {
        Bukkit.getPluginManager().registerEvents(new FollowGeneralListener(this), PotPvPSI.getInstance());
    }

    public Optional<UUID> getFollowing(Player player) {
        return Optional.ofNullable(followingData.get(player.getUniqueId()));
    }

    public void startFollowing(Player player, Player target) {
        followingData.put(player.getUniqueId(), target.getUniqueId());
        player.sendMessage(ChatColor.BLUE + "Now following " + ChatColor.YELLOW + target.getName() + ChatColor.BLUE + ", exit with /unfollow.");

        MatchHandler matchHandler = PotPvPSI.getInstance().getMatchHandler();
        Match targetMatch = matchHandler.getMatchPlayingOrSpectating(target);

        if (targetMatch != null && targetMatch.getState() != MatchState.ENDING) {
            targetMatch.addSpectator(player, target);
        } else {
            InventoryUtils.resetInventoryDelayed(player);
            VisibilityUtils.updateVisibility(player);
            FrozenNametagHandler.reloadOthersFor(player);

            player.teleport(target);
        }
    }

    public void stopFollowing(Player player) {
        UUID prevTarget = followingData.remove(player.getUniqueId());

        if (prevTarget != null) {
            player.sendMessage(ChatColor.BLUE + "Stopped following " + ChatColor.YELLOW + UUIDUtils.name(prevTarget) + ChatColor.BLUE + ".");
            InventoryUtils.resetInventoryDelayed(player);
            VisibilityUtils.updateVisibility(player);
            FrozenNametagHandler.reloadOthersFor(player);
        }
    }

    public Set<UUID> getFollowers(Player player) {
        Set<UUID> followers = new HashSet<>();

        followingData.forEach((follower, followed) -> {
            if (followed == player.getUniqueId()) {
                followers.add(follower);
            }
        });

        return followers;
    }

}