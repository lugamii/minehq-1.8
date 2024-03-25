package dev.lugami.potpvp.scoreboard;

import dev.lugami.potpvp.PotPvPSI;
import dev.lugami.potpvp.match.MatchHandler;
import dev.lugami.potpvp.setting.Setting;
import dev.lugami.potpvp.setting.SettingHandler;
import dev.lugami.qlib.scoreboard.ScoreGetter;
import dev.lugami.qlib.util.LinkedList;
import org.bukkit.entity.Player;

import java.util.function.BiConsumer;

final class MultiplexingScoreGetter implements ScoreGetter {

    private final BiConsumer<Player, LinkedList<String>> matchScoreGetter;
    private final BiConsumer<Player, LinkedList<String>> LobbyScoreGetter;

    MultiplexingScoreGetter(
            BiConsumer<Player, LinkedList<String>> matchScoreGetter,
            BiConsumer<Player, LinkedList<String>> LobbyScoreGetter
    ) {
        this.matchScoreGetter = matchScoreGetter;
        this.LobbyScoreGetter = LobbyScoreGetter;
    }

    @Override
    public void getScores(LinkedList<String> scores, Player player) {
        if (PotPvPSI.getInstance() == null) return;
        MatchHandler matchHandler = PotPvPSI.getInstance().getMatchHandler();
        SettingHandler settingHandler = PotPvPSI.getInstance().getSettingHandler();

        if (settingHandler.getSetting(player, Setting.SHOW_SCOREBOARD)) {
            if (matchHandler.isPlayingOrSpectatingMatch(player)) {
                matchScoreGetter.accept(player, scores);
                } else {
                    LobbyScoreGetter.accept(player, scores);
                }
        }

        if (!scores.isEmpty()) {
            scores.addFirst("&a&7&m--------------------");
            scores.add("");
            scores.add("&bwww.potpvp.net");
            scores.add("&f&7&m--------------------");
        }
    }

}