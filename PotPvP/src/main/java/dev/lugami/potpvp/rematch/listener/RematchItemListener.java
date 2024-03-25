package dev.lugami.potpvp.rematch.listener;

import dev.lugami.potpvp.duel.command.AcceptCommand;
import dev.lugami.potpvp.duel.command.DuelCommand;
import dev.lugami.potpvp.rematch.RematchData;
import dev.lugami.potpvp.rematch.RematchHandler;
import dev.lugami.potpvp.rematch.RematchItems;
import dev.lugami.potpvp.util.InventoryUtils;
import dev.lugami.potpvp.util.ItemListener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableSet;

public final class RematchItemListener extends ItemListener {

    public RematchItemListener(RematchHandler rematchHandler) {
        addHandler(RematchItems.REQUEST_REMATCH_ITEM, player -> {
            RematchData rematchData = rematchHandler.getRematchData(player);

            if (rematchData != null) {
                Player target = Bukkit.getPlayer(rematchData.getTarget());
                DuelCommand.duel(player, target, rematchData.getKitType());

                InventoryUtils.resetInventoryDelayed(player);
                InventoryUtils.resetInventoryDelayed(target);
            }
        });

        addHandler(RematchItems.SENT_REMATCH_ITEM, p -> p.sendMessage(ChatColor.RED + "You have already sent a rematch request."));

        addHandler(RematchItems.ACCEPT_REMATCH_ITEM, player -> {
            RematchData rematchData = rematchHandler.getRematchData(player);

            if (rematchData != null) {
                Player target = Bukkit.getPlayer(rematchData.getTarget());
                AcceptCommand.accept(player, target);
            }
        });
    }

}