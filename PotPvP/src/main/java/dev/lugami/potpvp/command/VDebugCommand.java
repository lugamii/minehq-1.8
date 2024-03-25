package dev.lugami.potpvp.command;

import dev.lugami.potpvp.PotPvPSI;
import dev.lugami.qlib.command.Command;
import dev.lugami.qlib.command.Param;
import net.minecraft.server.v1_8_R3.EntityTracker;
import net.minecraft.server.v1_8_R3.EntityTrackerEntry;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public final class VDebugCommand {

    @Command(names = {"weirdcommand"}, permission = "op")
    public static void vdebug(Player sender, @Param(name="a") Player a, @Param(name="b") Player b, @Param(name="modify", defaultValue = "0") int modify) {
        CraftPlayer aCraft = (CraftPlayer) a;
        CraftPlayer bCraft = (CraftPlayer) b;
        EntityTracker tracker = ((WorldServer) aCraft.getHandle().world).tracker;
        EntityTrackerEntry aTracker = tracker.trackedEntities.get(aCraft.getHandle().getId());
        EntityTrackerEntry bTracker = tracker.trackedEntities.get(bCraft.getHandle().getId());

        if (modify == 1) {
            a.showPlayer(b);
            b.showPlayer(a);

            sender.sendMessage(ChatColor.DARK_RED + "✖ " + ChatColor.RED + "Performed soft modify.");
            return;
        } else if (modify == 2) {
            aCraft.getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, bCraft.getHandle()));
            bCraft.getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, aCraft.getHandle()));

            sender.sendMessage(ChatColor.DARK_RED + "✖ " + ChatColor.RED + "Performed hard modify.");
            return;
        } else if (modify == 3) {
            a.hidePlayer(b);
            b.hidePlayer(a);

            Bukkit.getScheduler().runTaskLater(PotPvPSI.getInstance(), () -> {
                a.showPlayer(b);
                b.showPlayer(a);
            }, 10L);

            sender.sendMessage(ChatColor.DARK_RED + "✖ " + ChatColor.RED + "Performed flicker modify.");
            return;
        }

        sender.sendMessage(ChatColor.AQUA.toString() + ChatColor.UNDERLINE + a.getName() + " <-> " + b.getName() + ":");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.BLUE + "a Validation: " + ChatColor.WHITE + (aCraft.getHandle().playerConnection != null && !a.equals(b)));
        sender.sendMessage(ChatColor.BLUE + "b Validation: " + ChatColor.WHITE + (bCraft.getHandle().playerConnection != null && !b.equals(a)));

        sender.sendMessage(ChatColor.BLUE + "a.canSee(b): " + ChatColor.WHITE + a.canSee(b));
        sender.sendMessage(ChatColor.BLUE + "b.canSee(a): " + ChatColor.WHITE + b.canSee(a));

        sender.sendMessage(ChatColor.BLUE + "a Tracker Entry: " + ChatColor.WHITE + aTracker);
        sender.sendMessage(ChatColor.BLUE + "b Tracker Entry: " + ChatColor.WHITE + bTracker);

        sender.sendMessage(ChatColor.BLUE + "aTracker.trackedPlayers.contains(b): " + ChatColor.WHITE + aTracker.trackedPlayers.contains(bCraft.getHandle()));
        sender.sendMessage(ChatColor.BLUE + "bTracker.trackedPlayers.contains(a): " + ChatColor.WHITE + bTracker.trackedPlayers.contains(aCraft.getHandle()));
    }

}