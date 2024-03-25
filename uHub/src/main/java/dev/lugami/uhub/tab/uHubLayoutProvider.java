package dev.lugami.uhub.tab;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import dev.lugami.qlib.tab.provider.TabProvider;
import dev.lugami.qlib.util.ChatUtils;
import dev.lugami.uhub.uHub;
import org.bukkit.entity.Player;

import java.util.function.BiConsumer;

public final class uHubLayoutProvider implements TabProvider {

    static final int MAX_TAB_Y = 20;
    private static boolean testing = true;

    private final BiConsumer<Player, TabLayout> onlinePlayersLayoutProvider = new OnlinePlayersLayoutProvider();

    @Override
    public Table<Integer, Integer, String> provide(Player player) {
        if (uHub.getInstance() == null) return new TabLayout().build();
        TabLayout tabLayout = new TabLayout();

        onlinePlayersLayoutProvider.accept(player, tabLayout);

        return tabLayout.build();
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