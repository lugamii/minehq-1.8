package dev.lugami.potpvp.match.event;

import dev.lugami.potpvp.match.Match;

import org.bukkit.event.HandlerList;

import lombok.Getter;

/**
 * Called when a match's countdown starts (when its {@link dev.lugami.potpvp.match.MatchState} changes
 * to {@link dev.lugami.potpvp.match.MatchState#COUNTDOWN})
 * @see dev.lugami.potpvp.match.MatchState#COUNTDOWN
 */
public final class MatchCountdownStartEvent extends MatchEvent {

    @Getter private static HandlerList handlerList = new HandlerList();

    public MatchCountdownStartEvent(Match match) {
        super(match);
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}