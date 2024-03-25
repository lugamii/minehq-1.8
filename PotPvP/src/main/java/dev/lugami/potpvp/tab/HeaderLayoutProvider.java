package dev.lugami.potpvp.tab;

import dev.lugami.potpvp.PotPvPSI;
import dev.lugami.potpvp.match.MatchHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.function.BiConsumer;

final class HeaderLayoutProvider implements BiConsumer<Player, PotPvPLayoutProvider.TabLayout> {

    @Override
    public void accept(Player player, PotPvPLayoutProvider.TabLayout tabLayout) {
        MatchHandler matchHandler = PotPvPSI.getInstance().getMatchHandler();

        header: {
            tabLayout.put(1, 0, ChatColor.AQUA.toString() + ChatColor.BOLD + "Practice");
        }

        status: {
            tabLayout.put(0, 1, ChatColor.GRAY + "Online: " + Bukkit.getOnlinePlayers().size());
            //tabLayout.put(1, 1, ChatColor.GRAY + "Your Connection", Math.max(((PlayerUtils.getPing(player) + 5) / 10) * 10, 1));
            tabLayout.put(2, 1, ChatColor.GRAY + "In Fights: " + matchHandler.countPlayersPlayingInProgressMatches());
        }
    }
}
