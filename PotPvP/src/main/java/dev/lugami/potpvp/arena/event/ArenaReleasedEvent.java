package dev.lugami.potpvp.arena.event;

import dev.lugami.potpvp.arena.Arena;

import org.bukkit.event.HandlerList;

import lombok.Getter;

/**
 * Called when an {@link Arena} is done being used by a
 * {@link dev.lugami.potpvp.match.Match}
 */
public final class ArenaReleasedEvent extends ArenaEvent {

    @Getter private static HandlerList handlerList = new HandlerList();

    public ArenaReleasedEvent(Arena arena) {
        super(arena);
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}