package dev.lugami.potpvp.party.event;

import dev.lugami.potpvp.party.Party;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import lombok.Getter;

/**
 * Called when a {@link Party} is created.
 * @see dev.lugami.potpvp.party.command.PartyCreateCommand
 * @see dev.lugami.potpvp.party.PartyHandler#getOrCreateParty(Player)
 */
public final class PartyCreateEvent extends PartyEvent {

    @Getter private static HandlerList handlerList = new HandlerList();

    public PartyCreateEvent(Party party) {
        super(party);
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}