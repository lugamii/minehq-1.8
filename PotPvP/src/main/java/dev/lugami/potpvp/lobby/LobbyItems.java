package dev.lugami.potpvp.lobby;

import dev.lugami.potpvp.util.C;
import dev.lugami.qlib.util.ItemUtils;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import lombok.experimental.UtilityClass;

import static dev.lugami.potpvp.PotPvPLang.LEFT_ARROW;
import static dev.lugami.potpvp.PotPvPLang.RIGHT_ARROW;
import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.BOLD;
import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;
import static org.bukkit.ChatColor.DARK_PURPLE;
import static org.bukkit.ChatColor.YELLOW;

import org.bukkit.ChatColor;

@UtilityClass
public final class LobbyItems {

    public static final ItemStack SPECTATE_RANDOM_ITEM = new ItemStack(Material.COMPASS);
    public static final ItemStack SPECTATE_MENU_ITEM = new ItemStack(Material.PAPER);
    public static final ItemStack ENABLE_SPEC_MODE_ITEM = new ItemStack(Material.REDSTONE_TORCH_ON);
    public static final ItemStack DISABLE_SPEC_MODE_ITEM = new ItemStack(Material.LEVER);
    public static final ItemStack MANAGE_ITEM = new ItemStack(Material.ANVIL);
    public static final ItemStack UNFOLLOW_ITEM = new ItemStack(Material.INK_SACK, 1, DyeColor.RED.getDyeData());
    public static final ItemStack PLAYER_STATISTICS = new ItemStack(Material.EMERALD, 1, (byte) 3);
    public static final ItemStack CREATE_PARTY = new ItemStack(Material.NAME_TAG, 1);

    static {
        ItemUtils.setDisplayName(SPECTATE_RANDOM_ITEM, LEFT_ARROW + YELLOW.toString() + BOLD + "Spectate Random Match" + RIGHT_ARROW);
        ItemUtils.setDisplayName(SPECTATE_MENU_ITEM, LEFT_ARROW + GREEN.toString() + BOLD + "Spectate Menu" + RIGHT_ARROW);
        ItemUtils.setDisplayName(ENABLE_SPEC_MODE_ITEM, LEFT_ARROW + AQUA.toString() + BOLD + "Enable Spectator Mode" + RIGHT_ARROW);
        ItemUtils.setDisplayName(DISABLE_SPEC_MODE_ITEM, LEFT_ARROW + AQUA.toString() + BOLD + "Disable Spectator Mode" + RIGHT_ARROW);
        ItemUtils.setDisplayName(MANAGE_ITEM, RED + "Manage PotPvP");
        ItemUtils.setDisplayName(CREATE_PARTY, C.C("&dCreate Party"));
        ItemUtils.setDisplayName(UNFOLLOW_ITEM, LEFT_ARROW + RED + BOLD.toString() + "Stop Following" + RIGHT_ARROW);
        ItemUtils.setDisplayName(PLAYER_STATISTICS, C.C("&5View Leaderboards"));
    }

}