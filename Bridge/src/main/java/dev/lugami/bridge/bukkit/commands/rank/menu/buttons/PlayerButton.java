package dev.lugami.bridge.bukkit.commands.rank.menu.buttons;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import dev.lugami.bridge.bukkit.BukkitAPI;
import dev.lugami.bridge.bukkit.commands.grant.menu.grants.GrantsMenu;
import dev.lugami.bridge.global.grant.Grant;
import dev.lugami.bridge.global.profile.Profile;
import dev.lugami.qlib.menu.Button;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
public class PlayerButton extends Button {

    private Profile profile;

    @Override
    public String getName(Player player) {
        return profile.getColor() + profile.getUsername();
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lore = Arrays.asList(
                ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 34),
                ChatColor.YELLOW + "Rank: " + ChatColor.RED + BukkitAPI.getPlayerRank(profile).getColor() + BukkitAPI.getPlayerRank(profile).getDisplayName(),
                ChatColor.YELLOW + "Ranks: ");

        profile.getActiveGrants().forEach(grant -> {
            lore.add(ChatColor.GRAY + " * " + grant.getRank().getColor() + grant.getRank().getDisplayName() +  ChatColor.GRAY + "(" + StringUtils.join(grant.getScope(), ", ") + ")");
        });
        lore.addAll(Arrays.asList("",
                ChatColor.RED.toString() + ChatColor.BOLD + "Click to open",
                ChatColor.RED.toString() + ChatColor.BOLD + "players grants",
                ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 34)));
        return lore;
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.SKULL_ITEM;
    }

    @Override
    public byte getDamageValue(Player player) {
        return 3;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        player.closeInventory();
        List<Grant> allGrants = profile.getGrants();
        allGrants.sort((first, second) -> {
            if (first.getInitialTime() > second.getInitialTime()) {
                return -1;
            }
            else {
                return 1;
            }
        });
        new GrantsMenu(profile, allGrants).openMenu(player);
    }
}
