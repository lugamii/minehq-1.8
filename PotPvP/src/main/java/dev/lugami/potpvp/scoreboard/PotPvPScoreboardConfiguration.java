package dev.lugami.potpvp.scoreboard;

import dev.lugami.potpvp.PotPvPSI;
import dev.lugami.qlib.scoreboard.ScoreboardConfiguration;
import dev.lugami.qlib.scoreboard.TitleGetter;
import org.apache.commons.lang.StringEscapeUtils;

public final class PotPvPScoreboardConfiguration {

    public static ScoreboardConfiguration create() {
        ScoreboardConfiguration configuration = new ScoreboardConfiguration();

        configuration.setTitleGetter(TitleGetter.forStaticString(PotPvPSI.getInstance().getDominantColor() + "&lPotPvP"));
        configuration.setScoreGetter(new MultiplexingScoreGetter(
                new MatchScoreGetter(),
                new LobbyScoreGetter()
        ));

        return configuration;
    }

}
