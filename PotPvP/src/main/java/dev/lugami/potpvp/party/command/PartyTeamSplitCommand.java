package dev.lugami.potpvp.party.command;

import dev.lugami.potpvp.PotPvPLang;
import dev.lugami.potpvp.PotPvPSI;
import dev.lugami.potpvp.party.Party;
import dev.lugami.potpvp.party.PartyHandler;
import dev.lugami.potpvp.party.PartyUtils;
import dev.lugami.qlib.command.Command;

import org.bukkit.entity.Player;

public final class PartyTeamSplitCommand {

    @Command(names = {"party teamsplit", "p teamsplit", "t teamsplit", "team teamsplit", "f teamsplit"}, permission = "")
    public static void partyTeamSplit(Player sender) {
        PartyHandler partyHandler = PotPvPSI.getInstance().getPartyHandler();
        Party party = partyHandler.getParty(sender);

        if (party == null) {
            sender.sendMessage(PotPvPLang.NOT_IN_PARTY);
        } else if (!party.isLeader(sender.getUniqueId())) {
            sender.sendMessage(PotPvPLang.NOT_LEADER_OF_PARTY);
        } else {
            PartyUtils.startTeamSplit(party, sender);
        }
    }

}