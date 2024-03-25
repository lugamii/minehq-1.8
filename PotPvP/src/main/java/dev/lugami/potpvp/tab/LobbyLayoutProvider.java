package dev.lugami.potpvp.tab;

import com.google.common.collect.Sets;

import dev.lugami.potpvp.PotPvPSI;
import dev.lugami.potpvp.elo.EloHandler;
import dev.lugami.potpvp.kittype.KitType;
import dev.lugami.potpvp.party.Party;
import dev.lugami.potpvp.util.C;
import dev.lugami.qlib.util.TabLayout;
import dev.lugami.qlib.util.UUIDUtils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;

final class LobbyLayoutProvider implements BiConsumer<Player, PotPvPLayoutProvider.TabLayout> {

    @Override
    public void accept(Player player, PotPvPLayoutProvider.TabLayout tabLayout) {
        Party party = PotPvPSI.getInstance().getPartyHandler().getParty(player);
        EloHandler eloHandler = PotPvPSI.getInstance().getEloHandler();

        rankings: {
            tabLayout.put(1, 3, C.C(PotPvPSI.getInstance().getDominantColor().toString() + ChatColor.BOLD + "Your Rankings"));

            int x = 0;
            int y = 4;

            for (KitType kitType : KitType.getAllTypes()) {
                if (kitType.isHidden() || !kitType.isSupportsRanked()) {
                    continue;
                }

                tabLayout.put(x++, y, ChatColor.GRAY + kitType.getDisplayName() + " - " + eloHandler.getElo(player, kitType));

                if (x == 3) {
                    x = 0;
                    y++;
                }
            }
        }

        party: {
            if (party == null) {
                return;
            }

            tabLayout.put(1, 8, C.C(ChatColor.BLUE.toString() + ChatColor.BOLD + "Your Party"));

            int x = 0;
            int y = 9;

            for (UUID member : getOrderedMembers(player, party)) {
                int ping = PotPvPLayoutProvider.getPingOrDefault(member);
                String suffix = member == party.getLeader() ? ChatColor.GRAY + "*" : "";
                String displayName = ChatColor.BLUE + UUIDUtils.name(member) + suffix;

                tabLayout.put(x++, y, displayName);

                if (x == 3 && y == PotPvPLayoutProvider.MAX_TAB_Y) {
                    break;
                }

                if (x == 3) {
                    x = 0;
                    y++;
                }
            }
        }
    }

    // player first, leader next, then all other members
    private Set<UUID> getOrderedMembers(Player viewer, Party party) {
        Set<UUID> orderedMembers = Sets.newSetFromMap(new LinkedHashMap<>());
        UUID leader = party.getLeader();

        orderedMembers.add(viewer.getUniqueId());

        // if they're the leader we don't display them twice
        if (viewer.getUniqueId() != leader) {
            orderedMembers.add(leader);
        }

        for (UUID member : party.getMembers()) {
            // don't display the leader or the viewer again
            if (member == leader || member == viewer.getUniqueId()) {
                continue;
            }

            orderedMembers.add(member);
        }

        return orderedMembers;
    }

}