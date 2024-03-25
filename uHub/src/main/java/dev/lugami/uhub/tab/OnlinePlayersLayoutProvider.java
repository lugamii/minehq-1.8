package dev.lugami.uhub.tab;

import dev.lugami.bridge.BridgeGlobal;
import dev.lugami.bridge.global.profile.Profile;
import dev.lugami.bridge.global.ranks.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import dev.lugami.qlib.util.PlayerUtils;
import dev.lugami.qlib.util.UUIDUtils;
import dev.lugami.uhub.uHub;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.function.BiConsumer;

public class OnlinePlayersLayoutProvider implements Listener, BiConsumer<Player, uHubLayoutProvider.TabLayout> {

    private Map<UUID, String> playersMap = generateNewTreeMap();

    public OnlinePlayersLayoutProvider() {
        Bukkit.getPluginManager().registerEvents(this, uHub.getInstance());
        Bukkit.getScheduler().runTaskTimerAsynchronously(uHub.getInstance(), this::rebuildCache, 0, 20 * 5);
    }

    @Override
    public void accept(Player player, uHubLayoutProvider.TabLayout tabLayout) {
        int x = 0;
        int y = 0;
        boolean isStaff = player.hasPermission("basic.staff");
        for (Map.Entry<UUID, String> entry : playersMap.entrySet()) {
            if (x == 3) {
                x = 0;
                y++;
            }

            if (entry.getValue() == null) {
                continue;
            }

            Player otherPlayer = Bukkit.getPlayer(entry.getKey());
            if (otherPlayer.hasMetadata("ModMode")) {
                if (!isStaff) {
                    continue;
                }

                tabLayout.put(x++, y, ChatColor.GRAY + "*" + entry.getValue());
            } else {
                tabLayout.put(x++, y, entry.getValue());
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        playersMap.put(event.getPlayer().getUniqueId(), getName(event.getPlayer().getUniqueId()));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        playersMap.remove(event.getPlayer().getUniqueId());
    }

    private void rebuildCache() {
        TreeMap<UUID, String> newTreeMap = generateNewTreeMap();

        Bukkit.getOnlinePlayers().forEach(player -> newTreeMap.put(player.getUniqueId(), getName(player.getUniqueId())));

        this.playersMap = newTreeMap;
    }

    private String getName(UUID uuid) {
        Profile profile = BridgeGlobal.getProfileHandler().getProfileByUUID(uuid);
        if (profile == null) {
            return null;
        }

        Rank bestDisplayRank = profile.getDisguise() != null ? profile.getDisguise().getDisguiseRank() : profile.getCurrentGrant().getRank();

        return bestDisplayRank.getColor() + (profile.getDisguise() != null ? profile.getDisguise().getDisguiseName() : UUIDUtils.name(uuid));
    }

    private TreeMap<UUID, String> generateNewTreeMap() {
        return new TreeMap<>((first, second) -> {
            Profile firstProfile = BridgeGlobal.getProfileHandler().getProfileByUUID(first);
            Profile secondProfile = BridgeGlobal.getProfileHandler().getProfileByUUID(second);

            if (firstProfile != null && secondProfile != null) {
                int compare = Integer.compare(secondProfile.getDisguise() != null ? secondProfile.getDisguise().getDisguiseRank().getPriority() : secondProfile.getCurrentGrant().getRank().getPriority(), firstProfile.getDisguise() != null ? firstProfile.getDisguise().getDisguiseRank().getPriority() : firstProfile.getCurrentGrant().getRank().getPriority());
                if (compare == 0) {
                    return tieBreaker(first, second);
                }

                return compare;
            } else if (firstProfile != null) {
                return -1;
            } else if (secondProfile != null) {
                return 1;
            } else {
                return tieBreaker(first, second);
            }
        });
    }

    private int tieBreaker(UUID first, UUID second) {
        Profile firstProfile = BridgeGlobal.getProfileHandler().getProfileByUUID(first);
        Profile secondProfile = BridgeGlobal.getProfileHandler().getProfileByUUID(second);

        String firstName = firstProfile != null && firstProfile.getDisguise() != null ? firstProfile.getDisguise().getDisguiseName()
                : UUIDUtils.name(first);
        String secondName = secondProfile != null && secondProfile.getDisguise() != null ? secondProfile.getDisguise().getDisguiseName()
                : UUIDUtils.name(second);

        return firstName.compareTo(secondName);
    }

}