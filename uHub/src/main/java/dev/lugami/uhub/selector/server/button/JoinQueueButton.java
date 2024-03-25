package dev.lugami.uhub.selector.server.button;

import com.google.gson.JsonObject;
import dev.lugami.uhub.uHub;
import dev.lugami.uhub.util.ItemBuilder;
import dev.lugami.uhub.util.Style;
import dev.lugami.bridge.global.status.BridgeServer;
import dev.lugami.hqueue.data.Queue;
import dev.lugami.hqueue.hQueue;
import dev.lugami.hqueue.payloads.LeaveQueuePacket;
import dev.lugami.hqueue.payloads.handler.PacketHandler;
import dev.lugami.qlib.menu.Button;
import dev.lugami.uhub.selector.server.UHCSelectorMenu;
import lombok.AllArgsConstructor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@AllArgsConstructor
public class JoinQueueButton extends Button {

    private final BridgeServer serverData;
    private final String name;
    private final Material material;
    private final int durability;
    List<String> description;

    @Override
    public String getName(Player player) {
        return null;
    }

    @Override
    public List<String> getDescription(Player player) {
        return null;
    }

    @Override
    public Material getMaterial(Player player) {
        return null;
    }


    public ItemStack getButtonItem(Player player) {
        if (this.serverData == null) {
            hQueue.getQueueManager().getPlayersQueue(player.getUniqueId());
            List<String> lore = new ArrayList<>();
            lore.add("");
            for (String line : description) {
                lore.add(Style.GRAY + "* " + Style.GRAY + line);
            }
            lore.add("");
            lore.add(Style.YELLOW + "This server is offline");

            return new ItemBuilder(material)
                    .name(Style.RED + Style.BOLD + name)
                    .durability(durability)
                    .lore(lore)
                    .build();
        }

        Material material;
        List<String> description;
        int durability;

        if (this.serverData.getName().contains(uHub.getInstance().getConfig().getString( "queues." + this.name + ".id")) && this.serverData.isOnline()) {
            material = Material.valueOf(uHub.getInstance().getConfig().getString( "queues." + this.name + ".item.material"));
            durability = uHub.getInstance().getConfig().getInt("queues." + this.name + ".item.data");
            description = uHub.getInstance().getConfig().getStringList("queues." + this.name + ".description");
        } else {
            material = Material.REDSTONE_BLOCK;
            durability = 0;
            description = uHub.getInstance().getConfig().getStringList("queues." + this.name + ".description");
        }

        String name;

        if (this.serverData.isOnline()) {
            name = Style.GREEN + Style.BOLD + this.name;
        } else if (this.serverData.isWhitelisted()) {
            name = Style.YELLOW + Style.BOLD + this.name;
        } else {
            name = Style.RED + Style.BOLD + this.name;
        }

        List<String> lore = new ArrayList<>();

        if(this.serverData.isOnline() && !this.serverData.getName().endsWith("-Lobby")) {
            lore.add(Style.YELLOW + "Players: " + Style.WHITE + "(" + this.serverData.getOnline() + "/" + this.serverData.getMaximum() + ")");
            lore.add("");
        } else {
            lore.add("");
        }

        for (String line : description) {
            lore.add(Style.GRAY + "* " + Style.GRAY + line);
        }

        lore.add("");
        if (hQueue.getQueueManager().getPlayersQueue(player.getUniqueId()) != null) {
            lore.add("§dPosition #§e" + hQueue.getQueueManager().getPlayersQueue(player.getUniqueId()).getPosition(player.getUniqueId()) + " §dof §e" + hQueue.getQueueManager().getPlayersQueue(player.getUniqueId()).getPlayersInQueue().size());
            lore.add("§6Right click to leave the queue");
        } else {
            if (!this.serverData.isOnline()) {
                lore.add(Style.YELLOW + "This server is offline");
            } else {
                lore.add(Style.GRAY + Style.UNICODE_ARROWS_RIGHT + Style.YELLOW + " Click to join the queue! " + Style.GRAY + Style.UNICODE_ARROWS_LEFT);
                if (this.serverData.getName().endsWith("-Lobby")) {
                    lore.remove(Style.GRAY + Style.UNICODE_ARROWS_RIGHT + Style.YELLOW + " Click to join the queue! " + Style.GRAY + Style.UNICODE_ARROWS_LEFT);
                    lore.add(Style.GRAY + Style.UNICODE_ARROWS_RIGHT + Style.YELLOW + " Click to join! " + Style.GRAY + Style.UNICODE_ARROWS_LEFT);
                } else if (this.serverData.getName().endsWith("UHC")) {
                    lore.add(Style.GRAY + Style.UNICODE_ARROWS_RIGHT + Style.YELLOW + " Click select a server! " + Style.GRAY + Style.UNICODE_ARROWS_LEFT);
                    lore.remove(Style.GRAY + Style.UNICODE_ARROWS_RIGHT + Style.YELLOW + " Click to join the queue! " + Style.GRAY + Style.UNICODE_ARROWS_LEFT);
                    lore.remove(Style.GRAY + Style.UNICODE_ARROWS_RIGHT + Style.YELLOW + " Click to join! " + Style.GRAY + Style.UNICODE_ARROWS_LEFT);
                }
            }
            lore.add(Style.GRAY + Style.UNICODE_ARROWS_RIGHT + Style.YELLOW + " Click to join the queue! " + Style.GRAY + Style.UNICODE_ARROWS_LEFT);
            if (this.serverData.getName().endsWith("-Lobby")) {
                lore.remove(Style.GRAY + Style.UNICODE_ARROWS_RIGHT + Style.YELLOW + " Click to join the queue! " + Style.GRAY + Style.UNICODE_ARROWS_LEFT);
                lore.add(Style.GRAY + Style.UNICODE_ARROWS_RIGHT + Style.YELLOW + " Click to join! " + Style.GRAY + Style.UNICODE_ARROWS_LEFT);
            } else if (this.serverData.getName().endsWith("UHC")) {
                lore.add(Style.GRAY + Style.UNICODE_ARROWS_RIGHT + Style.YELLOW + " Click select a server! " + Style.GRAY + Style.UNICODE_ARROWS_LEFT);
                lore.remove(Style.GRAY + Style.UNICODE_ARROWS_RIGHT + Style.YELLOW + " Click to join the queue! " + Style.GRAY + Style.UNICODE_ARROWS_LEFT);
                lore.remove(Style.GRAY + Style.UNICODE_ARROWS_RIGHT + Style.YELLOW + " Click to join! " + Style.GRAY + Style.UNICODE_ARROWS_LEFT);
            }
        }

        return new ItemBuilder(material)
                .name(name)
                .lore(lore)
                .durability(durability)
                .build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        if (this.serverData == null) {
            return;
        }

        if (name.equals("UHC") && this.serverData != null) {
            new UHCSelectorMenu(serverData).openMenu(player);
            return;
        }

        Queue queue = hQueue.getQueueManager().getPlayersQueue(player.getUniqueId());

        if(clickType == ClickType.LEFT && this.serverData.isOnline()) {
            player.closeInventory();
            player.performCommand("joinqueue " + this.serverData.getName());

        }else if(clickType == ClickType.RIGHT && this.serverData.isOnline() && queue != null) {
            JsonObject data = new JsonObject();
            data.addProperty("uuid", player.getUniqueId().toString());

            PacketHandler.send(new LeaveQueuePacket(player.getUniqueId(), queue, ChatColor.translateAlternateColorCodes('&', "&cLeft the queue for " + queue.getQueueName() + ".").replace("%queue%", queue.getQueueName()), Bukkit.getServerName()));
        }
    }
}