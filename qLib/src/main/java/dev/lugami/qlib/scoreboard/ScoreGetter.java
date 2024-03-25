package dev.lugami.qlib.scoreboard;

import dev.lugami.qlib.util.LinkedList;
import org.bukkit.entity.Player;

public interface ScoreGetter {

    void getScores(LinkedList<String> var1, Player var2);

}

