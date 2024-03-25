package dev.lugami.potpvp.duel;

import java.util.Set;

import dev.lugami.potpvp.kittype.KitType;
import dev.lugami.potpvp.party.Party;

public final class PartyDuelInvite extends DuelInvite<Party> {

    public PartyDuelInvite(Party sender, Party target, KitType kitTypes) {
        super(sender, target, kitTypes);
    }

}