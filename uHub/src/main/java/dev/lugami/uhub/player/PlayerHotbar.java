package dev.lugami.uhub.player;

import dev.lugami.uhub.util.Items;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerHotbar {

    public static void give(Player player) {
        player.getInventory().setContents(new ItemStack[36]);

        player.getInventory().setItem(2, Items.selector);
        player.getInventory().setItem(4, Items.visibility(player));
        //player.getInventory().setItem(4, Items.cosmetics);
        player.getInventory().setItem(6, Items.enderbutt);
//        player.getInventory().setItem(7, Items.hubSelector);
        player.updateInventory();
    }
}