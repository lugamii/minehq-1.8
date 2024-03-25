package dev.lugami.qmodsuite.nametag;

import java.util.ArrayList;
import java.util.List;
import dev.lugami.qlib.nametag.NametagInfo;
import dev.lugami.qlib.nametag.NametagProvider;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class qModSuiteNametagProvider extends NametagProvider {
   public qModSuiteNametagProvider() {
      super("qmodsuite Provider", 5);
   }

   public NametagInfo fetchNametag(Player toRefresh, Player refreshFor) {
      List<String> modMode = new ArrayList();
      modMode.add(ChatColor.GRAY.toString() + "[Mod Mode]");
      modMode.add(ChatColor.GRAY + toRefresh.getName());
      return null;
   }
}
