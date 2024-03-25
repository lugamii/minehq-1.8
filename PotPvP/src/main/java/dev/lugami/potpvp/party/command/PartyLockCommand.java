package dev.lugami.potpvp.party.command;

import dev.lugami.potpvp.PotPvPLang;
import dev.lugami.potpvp.PotPvPSI;
import dev.lugami.potpvp.party.Party;
import dev.lugami.potpvp.party.PartyAccessRestriction;
import dev.lugami.qlib.command.Command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class PartyLockCommand {

    @Command(names = {"party lock", "p lock", "t lock", "team lock", "f lock"}, permission = "")
    public static void partyLock(Player sender) {
        Party party = PotPvPSI.getInstance().getPartyHandler().getParty(sender);

        if (party == null) {
            sender.sendMessage(PotPvPLang.NOT_IN_PARTY);
        } else if (!party.isLeader(sender.getUniqueId())) {
            sender.sendMessage(PotPvPLang.NOT_LEADER_OF_PARTY);
        } else if (party.getAccessRestriction() == PartyAccessRestriction.INVITE_ONLY) {
            sender.sendMessage(ChatColor.RED + "Your party is already locked.");
        } else {
            party.setAccessRestriction(PartyAccessRestriction.INVITE_ONLY);
            sender.sendMessage(ChatColor.YELLOW + "Your party is now " + ChatColor.RED + "locked" + ChatColor.YELLOW + ".");
        }
    }

}
