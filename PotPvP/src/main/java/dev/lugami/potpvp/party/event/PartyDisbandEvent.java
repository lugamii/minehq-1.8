package dev.lugami.potpvp.party.event;

import dev.lugami.potpvp.party.Party;

import org.bukkit.event.HandlerList;

import lombok.Getter;

/**
 * Called when a {@link Party} is disbanded.
 * @see dev.lugami.potpvp.party.command.PartyDisbandCommand
 * @see Party#disband()
 */
public final class PartyDisbandEvent extends PartyEvent {

    @Getter private static HandlerList handlerList = new HandlerList();

    public PartyDisbandEvent(Party party) {
        super(party);
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}