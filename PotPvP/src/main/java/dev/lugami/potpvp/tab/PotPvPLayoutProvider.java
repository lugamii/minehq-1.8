package dev.lugami.potpvp.tab;

import java.util.UUID;
import java.util.function.BiConsumer;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import dev.lugami.qlib.tab.provider.TabProvider;
import dev.lugami.qlib.util.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import dev.lugami.potpvp.PotPvPSI;
import dev.lugami.potpvp.match.Match;
import dev.lugami.qlib.util.PlayerUtils;

public final class PotPvPLayoutProvider implements TabProvider {

    static final int MAX_TAB_Y = 20;

    private final BiConsumer<Player, TabLayout> headerLayoutProvider = new HeaderLayoutProvider();
    private final BiConsumer<Player, TabLayout> lobbyLayoutProvider = new LobbyLayoutProvider();
    private final BiConsumer<Player, TabLayout> matchSpectatorLayoutProvider = new MatchSpectatorLayoutProvider();
    private final BiConsumer<Player, TabLayout> matchParticipantLayoutProvider = new MatchParticipantLayoutProvider();
    private final BiConsumer<Player, TabLayout> onlinePlayersLayoutProvider = new OnlinePlayersLayoutProvider();
    private final BiConsumer<Player, TabLayout> alternateOnlinePlayersLayoutProvider = new AlternateOnlinePlayersLayoutProvider();

    @Override
    public Table<Integer, Integer, String> provide(Player player) {
        TabLayout tabLayout = new TabLayout();
        if (PotPvPSI.getInstance() == null) return tabLayout.build();

        if (PotPvPSI.getInstance().getDominantColor() == ChatColor.LIGHT_PURPLE) {
            alternateOnlinePlayersLayoutProvider.accept(player, tabLayout);
        } else {
            Match match = PotPvPSI.getInstance().getMatchHandler().getMatchPlayingOrSpectating(player);
            headerLayoutProvider.accept(player, tabLayout);

            if (match != null) {
                if (match.isSpectator(player.getUniqueId())) {
                    matchSpectatorLayoutProvider.accept(player, tabLayout);
                } else {
                    matchParticipantLayoutProvider.accept(player, tabLayout);
                }
            } else {
                lobbyLayoutProvider.accept(player, tabLayout);
            }

            onlinePlayersLayoutProvider.accept(player, tabLayout);
        }

        onlinePlayersLayoutProvider.accept(player, tabLayout);

        return tabLayout.build();
    }

    static int getPingOrDefault(UUID check) {
        Player player = Bukkit.getPlayer(check);
        return player != null ? PlayerUtils.getPing(player) : 0;
    }

    public static class TabLayout {
        private final Table<Integer, Integer, String> layout;
        public TabLayout() {
            layout = HashBasedTable.create();
            for(int r = 0; r < 20; ++r) {
                for (int c = 0; c < 4; ++c) {
                    layout.put(r, c, " ");
                }
            }
        }
        public void put(Integer var1, Integer var2, String var3) {
            layout.put(var2, var1, ChatUtils.colorize(var3));
        }
        public Table<Integer, Integer, String> build() {
            return layout;
        }
    }

}