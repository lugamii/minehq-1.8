package dev.lugami.qmodsuite.utils.chest;

import java.lang.reflect.Field;

import dev.lugami.qmodsuite.utils.chest.SilentContainerChest;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.IInventory;
import net.minecraft.server.v1_8_R3.PacketPlayOutOpenWindow;
import org.bukkit.ChatColor;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventory;
import org.bukkit.entity.Player;

public class ChestUtils {
   private static Field windowField = null;

   public static void openSilently(Player p, Chest c) {
      try {
         p.sendMessage(ChatColor.RED + "Opening chest silently...");
         EntityPlayer player = ((CraftPlayer)p).getHandle();
         IInventory inventory = ((CraftInventory)c.getInventory()).getInventory();
         player.nextContainerCounter();
         int counter = windowField.getInt(player);
         SilentContainerChest silentChest = new SilentContainerChest(player.inventory, inventory);
         player.playerConnection.sendPacket(new PacketPlayOutOpenWindow(counter, player.inventory.getName(), player.inventory.getScoreboardDisplayName(), inventory.getSize(), 0));
         player.activeContainer = silentChest;
         player.activeContainer.windowId = counter;
         player.activeContainer.addSlotListener(player);
         c.update();
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   static {
      try {
         windowField = EntityPlayer.class.getDeclaredField("containerCounter");
         windowField.setAccessible(true);
      } catch (NoSuchFieldException var1) {
         var1.printStackTrace();
      }

   }
}
