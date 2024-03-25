package dev.lugami.potpvp.follow.command;

import dev.lugami.potpvp.PotPvPSI;
import dev.lugami.potpvp.match.command.LeaveCommand;
import dev.lugami.qlib.command.Command;
import dev.lugami.qlib.command.Param;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class SilentFollowCommand {

    @Command(names = "silentfollow", permission = "potpvp.silent")
    public static void silentfollow(Player sender, @Param(name = "target") Player target) {
        sender.setMetadata("ModMode", new FixedMetadataValue(PotPvPSI.getInstance(), true));
        sender.setMetadata("invisible", new FixedMetadataValue(PotPvPSI.getInstance(), true));

        if (PotPvPSI.getInstance().getPartyHandler().hasParty(sender)) {
            LeaveCommand.leave(sender);
        }

        FollowCommand.follow(sender, target);
    }

}
