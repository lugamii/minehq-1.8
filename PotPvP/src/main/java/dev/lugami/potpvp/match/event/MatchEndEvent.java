package dev.lugami.potpvp.match.event;

import dev.lugami.potpvp.match.Match;

import org.bukkit.event.HandlerList;

import lombok.Getter;

/**
 * Called when a match is ended (when its {@link dev.lugami.potpvp.match.MatchState} changes
 * to {@link dev.lugami.potpvp.match.MatchState#ENDING})
 * @see dev.lugami.potpvp.match.MatchState#ENDING
 */
public final class MatchEndEvent extends MatchEvent {

    @Getter private static HandlerList handlerList = new HandlerList();

    public MatchEndEvent(Match match) {
        super(match);
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}