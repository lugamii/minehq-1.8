package dev.lugami.potpvp.match.event;

import dev.lugami.potpvp.match.Match;

import org.bukkit.event.HandlerList;

import lombok.Getter;

/**
 * Called when a match's countdown ends (when its {@link dev.lugami.potpvp.match.MatchState} changes
 * to {@link dev.lugami.potpvp.match.MatchState#IN_PROGRESS})
 * @see dev.lugami.potpvp.match.MatchState#IN_PROGRESS
 */
public final class MatchStartEvent extends MatchEvent {

    @Getter private static HandlerList handlerList = new HandlerList();

    public MatchStartEvent(Match match) {
        super(match);
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}