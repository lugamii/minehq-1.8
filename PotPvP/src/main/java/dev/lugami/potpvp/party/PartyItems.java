package dev.lugami.potpvp.party;

import dev.lugami.qlib.util.ItemUtils;
import dev.lugami.qlib.util.UUIDUtils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import lombok.experimental.UtilityClass;

import static dev.lugami.potpvp.PotPvPLang.LEFT_ARROW;
import static dev.lugami.potpvp.PotPvPLang.RIGHT_ARROW;
import static org.bukkit.ChatColor.*;

@UtilityClass
public final class PartyItems {

    public static final Material ICON_TYPE = Material.NETHER_STAR;

    public static final ItemStack LEAVE_PARTY_ITEM = new ItemStack(Material.FIRE);
    public static final ItemStack ASSIGN_CLASSES = new ItemStack(Material.ITEM_FRAME);
    public static final ItemStack START_TEAM_SPLIT_ITEM = new ItemStack(Material.DIAMOND_SWORD);
    public static final ItemStack START_FFA_ITEM = new ItemStack(Material.GOLD_SWORD);
    public static final ItemStack OTHER_PARTIES_ITEM = new ItemStack(Material.SKULL_ITEM);

    static {
        ItemUtils.setDisplayName(LEAVE_PARTY_ITEM, RED + "Leave Party");
        ItemUtils.setDisplayName(ASSIGN_CLASSES, GOLD + "HCF Kits");
        ItemUtils.setDisplayName(START_TEAM_SPLIT_ITEM, YELLOW + "Start Team Split");
        ItemUtils.setDisplayName(START_FFA_ITEM, YELLOW + "Start Party FFA");
        ItemUtils.setDisplayName(OTHER_PARTIES_ITEM, GREEN + "Other Parties");
    }

    public static ItemStack icon(Party party) {
        ItemStack item = new ItemStack(ICON_TYPE);

        String leaderName = UUIDUtils.name(party.getLeader());
        String displayName = LEFT_ARROW + AQUA.toString() + BOLD + leaderName + AQUA + "'s Party" + RIGHT_ARROW;

        ItemUtils.setDisplayName(item, displayName);
        return item;
    }

}
