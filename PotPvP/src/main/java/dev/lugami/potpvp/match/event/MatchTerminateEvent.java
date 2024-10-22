package dev.lugami.potpvp.match.event;

import dev.lugami.potpvp.match.Match;

import org.bukkit.event.HandlerList;

import lombok.Getter;

/**
 * Called when a match is terminated (when its {@link dev.lugami.potpvp.match.MatchState} changes
 * to {@link dev.lugami.potpvp.match.MatchState#TERMINATED})
 * @see dev.lugami.potpvp.match.MatchState#TERMINATED
 */
public final class MatchTerminateEvent extends MatchEvent {

    @Getter private static HandlerList handlerList = new HandlerList();


    public MatchTerminateEvent(Match match) {
        super(match);
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}