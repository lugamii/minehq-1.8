package dev.lugami.potpvp.arena.event;

import dev.lugami.potpvp.arena.Arena;

import org.bukkit.event.HandlerList;

import lombok.Getter;

/**
 * Called when an {@link Arena} is allocated for use by a
 * {@link dev.lugami.potpvp.match.Match}
 */
public final class ArenaAllocatedEvent extends ArenaEvent {

    @Getter private static HandlerList handlerList = new HandlerList();

    public ArenaAllocatedEvent(Arena arena) {
        super(arena);
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}