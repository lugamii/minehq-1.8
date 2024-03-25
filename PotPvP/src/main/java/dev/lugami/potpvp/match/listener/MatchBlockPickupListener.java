package dev.lugami.potpvp.match.listener;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
//import org.bukkit.event.block.BlockDropItemsEvent;
import org.bukkit.inventory.ItemStack;

import dev.lugami.potpvp.PotPvPSI;
import dev.lugami.potpvp.match.Match;

public class MatchBlockPickupListener implements Listener {

    /*@EventHandler
    public void onBlockDropItems(BlockDropItemsEvent event) {
        Player recipient = event.getPlayer();
        if (recipient == null) return;

        Match match = PotPvPSI.getInstance().getMatchHandler().getMatchPlaying(recipient);
        if (match == null) return;

        if (!match.getKitType().getId().equals("SPLEEF")) return;

        List<Item> items = event.getToDrop();
        for (Item item : items) {
            ItemStack stack = item.getItemStack();
            stack.setType(Material.SNOW_BALL);
            recipient.getInventory().addItem(stack);
        }

        items.clear();
    }*/
}
