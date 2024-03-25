package dev.lugami.potpvp.util;

import dev.lugami.qlib.util.UUIDUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

// we mess with fly mode in PotPvP, so we need to reset that with PlayerUtils (in qLib)
// unfortunately, that class doesn't reset fly mode - and plugins like qHub, which use doublejump
// (implemented with fly mode if you're not familiar) have already started using that method.
public class PatchedPlayerUtils {

    public static void resetInventory(Player player) {
        resetInventory(player, null);
    }

    public static void resetInventory(Player player, GameMode gameMode) {
        resetInventory(player, gameMode, false);
    }

    public static void resetInventory(Player player, GameMode gameMode, boolean skipInvReset) {
        player.setHealth(player.getMaxHealth());
        player.setFallDistance(0F);
        player.setFoodLevel(20);
        player.setSaturation(10F);
        player.setLevel(0);
        player.setExp(0F);

        if (!skipInvReset) {
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
        }

        player.setFireTicks(0);

        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(potionEffect.getType());
        }

        if (gameMode != null && player.getGameMode() != gameMode) {
            player.setGameMode(gameMode);
        }

        player.setAllowFlight(false);
        player.setFlying(false);
    }

    public static List<String> mapToNames(Collection<UUID> uuids) {
        return uuids.stream().map(UUIDUtils::name).collect(Collectors.toList());
    }

    public static String getFormattedName(UUID uuid) {

        return UUIDUtils.name(uuid);
            
            /*
            List<Rank> applicableRanks = .getInstance().getRankHandler().getRanks(uuid).stream().map(rankName -> Hydrogen.getInstance().getRankHandler().getRank(rankName)).filter(optional -> optional.isPresent()).map(optional -> optional.get()).collect(Collectors.toList());
            if (applicableRanks.isEmpty()) {
                return UUIDUtils.name(uuid);
            }

            Rank toDisplay = null;
            for (Rank rank : applicableRanks) {
                if (toDisplay == null || toDisplay.getDisplayWeight() <= rank.getDisplayWeight()) {
                    toDisplay = rank;
                }
            }

            return toDisplay.getGameColor() + UUIDUtils.name(uuid);
            */
    }
}