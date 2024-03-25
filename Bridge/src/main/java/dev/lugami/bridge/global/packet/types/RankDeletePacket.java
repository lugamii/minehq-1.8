package dev.lugami.bridge.global.packet.types;

import dev.lugami.bridge.BridgeGlobal;
import dev.lugami.bridge.global.packet.Packet;
import dev.lugami.bridge.global.packet.PacketHandler;
import dev.lugami.bridge.global.ranks.Rank;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor
public class RankDeletePacket implements Packet {

    private Rank rank;
    private String creator, server;

    @Override
    public void onReceive() {
        rank.removeRank();
        BridgeGlobal.getRankHandler().getRanks().remove(rank);
        BridgeGlobal.getProfileHandler().getProfiles().forEach(profile -> {
            profile.getActiveGrants().forEach(grant -> {
                if (grant.getRank().getUuid().toString().equals(rank.toString())) {
                    profile.refreshCurrentGrant();
                }
            });
        });
        BridgeGlobal.sendLog("Deleted rank " + rank.getDisplayName() + " (Executed by " + creator + " on " + server + ")");
    }

}
