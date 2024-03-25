package dev.lugami.bridge.bukkit.commands.rank;

import org.bukkit.command.CommandSender;
import dev.lugami.bridge.BridgeGlobal;
import dev.lugami.bridge.global.packet.PacketHandler;
import dev.lugami.bridge.global.packet.types.NetworkBroadcastPacket;
import dev.lugami.bridge.global.packet.types.RankUpdatePacket;
import dev.lugami.bridge.global.ranks.Rank;
import dev.lugami.qlib.command.Command;
import dev.lugami.qlib.command.Param;

public class RankSetDefaultCommand {

    @Command(names = {"rank setdefault"}, permission = "bridge.rank", description = "Set a rank default status", hidden = true, async = true)
    public static void RankSetDefaultCmd(CommandSender s, @Param(name = "rank") Rank r, @Param(name = "default") boolean b) {
        r.setDefaultRank(b);
        r.saveRank();
        s.sendMessage("§aSuccessfully changed the default status to " + b + "§a!");
        PacketHandler.sendToAll(new RankUpdatePacket(r, s.getName(), BridgeGlobal.getSystemName()));
        PacketHandler.sendToAll(new NetworkBroadcastPacket("bridge.update.view", "&8[&eServer Monitor&8] &fRefreshed rank " + r.getColor() + r.getDisplayName()));
    }
}
