package dev.lugami.potpvp.rematch;

import dev.lugami.qlib.util.ItemUtils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import lombok.experimental.UtilityClass;

import static dev.lugami.potpvp.PotPvPLang.LEFT_ARROW;
import static dev.lugami.potpvp.PotPvPLang.RIGHT_ARROW;
import static org.bukkit.ChatColor.BOLD;
import static org.bukkit.ChatColor.DARK_PURPLE;
import static org.bukkit.ChatColor.GREEN;

@UtilityClass
public final class RematchItems {

    public static final ItemStack REQUEST_REMATCH_ITEM = new ItemStack(Material.DIAMOND);
    public static final ItemStack SENT_REMATCH_ITEM = new ItemStack(Material.DIAMOND);
    public static final ItemStack ACCEPT_REMATCH_ITEM = new ItemStack(Material.DIAMOND);

    static {
        ItemUtils.setDisplayName(REQUEST_REMATCH_ITEM, DARK_PURPLE + "Request Rematch");
        ItemUtils.setDisplayName(SENT_REMATCH_ITEM, GREEN + "Sent Rematch");
        ItemUtils.setDisplayName(ACCEPT_REMATCH_ITEM, GREEN + "Accept Rematch");
    }

}