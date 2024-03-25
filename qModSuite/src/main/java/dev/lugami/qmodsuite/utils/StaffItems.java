package dev.lugami.qmodsuite.utils;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class StaffItems {
   public static ItemStack COMPASS;
   public static ItemStack INSPECT_BOOK;
   public static ItemStack WAND;
   public static ItemStack GO_VIS;
   public static ItemStack GO_INVIS;
   public static ItemStack ONLINE_STAFF;
   public static ItemStack CARPET;
   public static ItemStack LAST_PVP;

   public static ItemStack build(Material type, String displayName) {
      return build(type, 1, (byte)0, displayName);
   }

   public static ItemStack build(Material type, int amount, byte data, String displayName) {
      ItemStack stack = new ItemStack(type, amount, (short)1, data);
      ItemMeta meta = stack.getItemMeta();
      meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
      stack.setItemMeta(meta);
      return stack;
   }

   static {
      COMPASS = build(Material.COMPASS, ChatColor.AQUA + "Compass");
      INSPECT_BOOK = build(Material.BOOK, ChatColor.AQUA + "Inspection Book");
      WAND = build(Material.WOOD_AXE, ChatColor.AQUA + "WorldEdit Wand");
      GO_VIS = build(Material.INK_SACK, 1, DyeColor.GRAY.getDyeData(), ChatColor.AQUA + "Become Visible");
      GO_INVIS = build(Material.INK_SACK, 1, DyeColor.LIME.getDyeData(), ChatColor.AQUA + "Become Invisible");
      ONLINE_STAFF = build(Material.SKULL_ITEM, 1, (byte)3, ChatColor.AQUA + "Online Staff");
      CARPET = build(Material.CARPET, 1, DyeColor.RED.getDyeData(), " ");
      LAST_PVP = build(Material.EMERALD, ChatColor.AQUA + "Last PvP");
      }
}
