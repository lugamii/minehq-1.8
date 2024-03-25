package dev.lugami.bridge.bukkit.commands.rank;

import org.bukkit.command.CommandSender;
import dev.lugami.bridge.BridgeGlobal;
import dev.lugami.bridge.global.packet.PacketHandler;
import dev.lugami.bridge.global.packet.types.NetworkBroadcastPacket;
import dev.lugami.bridge.global.packet.types.RankUpdatePacket;
import dev.lugami.bridge.global.ranks.Rank;
import dev.lugami.qlib.command.Command;
import dev.lugami.qlib.command.Param;

public class RankPermissionCommand {

    @Command(names = {"rank permission", "rank perm"}, permission = "bridge.rank", description = "Add/remove a permission", hidden = true, async = true)
    public static void RankPermissionCmd(CommandSender s, @Param(name = "rank") Rank r, @Param(name = "permission") String perm, @Param(name = "group", defaultValue = "§") String serverGroup) {
        String group = serverGroup.equals("§") ? "Global" : serverGroup;
        boolean b = r.togglePerm(perm, group);
        r.saveRank();
        s.sendMessage("§aSuccessfully " + (b ? "added" : "removed") + " the permission " + perm + " to the scope: " + group);
        PacketHandler.sendToAll(new RankUpdatePacket(r, s.getName(), BridgeGlobal.getSystemName()));
    }
}
