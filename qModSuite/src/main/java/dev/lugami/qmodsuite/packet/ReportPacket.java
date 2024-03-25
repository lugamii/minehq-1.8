package dev.lugami.qmodsuite.packet;

import com.google.common.base.Preconditions;
import java.util.Iterator;
import dev.lugami.qlib.xpacket.XPacket;
import dev.lugami.qmodsuite.qModSuite;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class ReportPacket implements XPacket {
   private static final String MESSAGE_FORMAT = ChatColor.translateAlternateColorCodes('&', "&9[Report] &7[%s] &b%s &7(%d) reported by &b%s\n     &9Reason: &7%s");
   private String server = Bukkit.getServerName();
   private String sender;
   private String target;
   private String reason;
   private int reportCount;

   public ReportPacket(Player sender, Player target, String reason, int reportCount) {
      this.sender = sender.getName();
      this.target = target.getName();
      this.reason = (String)Preconditions.checkNotNull(reason, "reason");
      this.reportCount = reportCount;
   }

   public void onReceive() {
      String finalMessage = String.format(MESSAGE_FORMAT, this.server, this.target, this.reportCount, this.sender, this.reason);
      Iterator var2 = Bukkit.getOnlinePlayers().iterator();

      while(var2.hasNext()) {
         Player player = (Player)var2.next();
         if (player.hasPermission("basic.staff") && !qModSuite.getInstance().getSilencedStaffMembers().contains(player.getUniqueId())) {
            player.sendMessage(finalMessage);
         }
      }

      qModSuite.getInstance().getLogger().info("[Report] " + this.target + " reported by " + this.sender + ": " + this.reason);
   }

   public String getServer() {
      return this.server;
   }

   public String getSender() {
      return this.sender;
   }

   public String getTarget() {
      return this.target;
   }

   public String getReason() {
      return this.reason;
   }

   public int getReportCount() {
      return this.reportCount;
   }
}
