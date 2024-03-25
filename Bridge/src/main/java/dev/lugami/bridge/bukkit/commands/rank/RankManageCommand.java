package dev.lugami.bridge.bukkit.commands.rank;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import dev.lugami.bridge.BridgeGlobal;
import dev.lugami.bridge.bukkit.commands.rank.menu.RankMenu;
import dev.lugami.bridge.global.ranks.Rank;
import dev.lugami.qlib.command.Command;
import dev.lugami.qlib.command.Param;

public class RankManageCommand {

    @Command(names = {"rank manage"}, permission = "bridge.rank", description = "Manage a rank", hidden = true, async = true)
    public static void RankMangeCmd(CommandSender s, @Param(name = "rank") String name) {
        Rank r = BridgeGlobal.getRankHandler().getRankByName(name);
        if (r == null) {
            s.sendMessage("§cThere is no such rank with the name \"" + name + "\".");
            return;
        }

        new RankMenu(r).openMenu((Player) s);
    }
}
