package dev.lugami.uhub.util;

import dev.lugami.uhub.uHub;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Items {

    public static ItemStack visibility(Player player) {
        String visibilityName;

        if (uHub.getInstance().isHidingPlayers(player)) {
            visibilityName = Style.BLUE + Style.BOLD + Style.UNICODE_ARROWS_RIGHT + Style.GREEN + Style.BOLD + " Show Players " + Style.BLUE + Style.BOLD + Style.UNICODE_ARROWS_LEFT;
        } else {
            visibilityName = Style.BLUE + Style.BOLD + Style.UNICODE_ARROWS_RIGHT + Style.GRAY + Style.BOLD + " Hide Players " + Style.BLUE + Style.BOLD + Style.UNICODE_ARROWS_LEFT;
        }

        return new ItemBuilder(Material.INK_SACK)
                .name(visibilityName)
                .durability(8)
                .build();
    }

    public static ItemStack selector = new ItemBuilder(Material.WATCH)
            .name(Style.BLUE + Style.BOLD + Style.UNICODE_ARROWS_RIGHT + Style.YELLOW + Style.BOLD + " Server Selector " + Style.BLUE + Style.BOLD + Style.UNICODE_ARROWS_LEFT)
            .build();

    public static ItemStack hubSelector = new ItemBuilder(Material.BOOK)
            .name(Style.BLUE + Style.BOLD + Style.UNICODE_ARROWS_RIGHT + Style.GOLD + Style.BOLD + " Hub Selector " + Style.BLUE + Style.BOLD + Style.UNICODE_ARROWS_LEFT)
            .build();

    public static ItemStack cosmetics = new ItemBuilder(Material.FEATHER)
            .name(Style.BLUE + Style.BOLD + Style.UNICODE_ARROWS_RIGHT + Style.PINK + Style.BOLD + " Cosmetics " + Style.BLUE + Style.BOLD + Style.UNICODE_ARROWS_LEFT)
            .build();

    public static ItemStack enderbutt = new ItemBuilder(Material.ENDER_PEARL)
            .name(Style.BLUE + Style.BOLD + Style.UNICODE_ARROWS_RIGHT + Style.DARK_PURPLE + Style.BOLD + " Ender Butt " + Style.BLUE + Style.BOLD + Style.UNICODE_ARROWS_LEFT)
            .build();

}