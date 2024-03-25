package dev.lugami.bridge.global.packet.types;

import dev.lugami.bridge.BridgeGlobal;
import dev.lugami.bridge.global.grant.Grant;
import dev.lugami.bridge.global.packet.Packet;
import dev.lugami.bridge.global.packet.PacketHandler;
import dev.lugami.bridge.global.profile.Profile;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bukkit.ChatColor;

import java.util.UUID;

@AllArgsConstructor @NoArgsConstructor
public class GrantCreatePacket implements Packet {

    private Grant grant;
    private UUID target;
    private String creator, server;

    @Override
    public void onReceive() {
        if(server.equals(BridgeGlobal.getSystemName())) return;
        Profile pf = BridgeGlobal.getProfileHandler().getProfileByUUID(target);
        if(pf == null) {
            return;
        }
        pf.applyGrant(grant, null);
        PacketHandler.sendToAll(new NetworkBroadcastPacket("bridge.update.view", "&8[&eServer Monitor&8] &fSuccessfully granted " + pf.getColor() + pf.getUsername() + ChatColor.WHITE + " the " + grant.getRank().getColor() + grant.getRank().getDisplayName() + ChatColor.WHITE + " rank."));
    }

}
