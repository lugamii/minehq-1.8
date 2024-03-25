package dev.lugami.bridge.bukkit.commands.rank;

import org.bukkit.command.CommandSender;
import dev.lugami.bridge.BridgeGlobal;
import dev.lugami.bridge.global.packet.PacketHandler;
import dev.lugami.bridge.global.packet.types.NetworkBroadcastPacket;
import dev.lugami.bridge.global.packet.types.RankDeletePacket;
import dev.lugami.bridge.global.ranks.Rank;
import dev.lugami.qlib.command.Command;
import dev.lugami.qlib.command.Param;

public class RankDeleteCommand {

    @Command(names = {"rank delete", "rank remove"}, permission = "bridge.rank", description = "Delete a rank", hidden = true, async = true)
    public static void RankDeleteCmd(CommandSender s, @Param(name = "rank") Rank r) {
        s.sendMessage("§aSuccessfully deleted the rank " + r.getColor() + r.getName() + "§a!");
        BridgeGlobal.getMongoHandler().removeRank(r.getUuid(), callback -> {
        }, true);
        PacketHandler.sendToAll(new RankDeletePacket(r, s.getName(), BridgeGlobal.getServerName()));
        PacketHandler.sendToAll(new NetworkBroadcastPacket("bridge.update.view", "&8[&eServer Monitor&8] &fDeleted rank " + r.getColor() + r.getDisplayName()));
    }
}
