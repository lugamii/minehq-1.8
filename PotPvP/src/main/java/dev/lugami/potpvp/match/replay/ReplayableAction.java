package dev.lugami.potpvp.match.replay;

import org.bukkit.entity.Player;

public interface ReplayableAction {

    public void replay(Player replayFor);
}
