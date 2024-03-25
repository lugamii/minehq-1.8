package dev.lugami.uhub.selector.hub.button;

import dev.lugami.bridge.global.status.BridgeServer;
import dev.lugami.uhub.util.Style;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import dev.lugami.hqueue.hQueue;
import dev.lugami.qlib.menu.Button;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;

public class JoinHubButton extends Button {

    BridgeServer server;
    String permission;

    public JoinHubButton(BridgeServer server, String permission) {
        this.server = server;
        this.permission = permission;
    }

    @Override
    public String getName(Player player) {
        return (server.isOnline() ? ChatColor.GREEN + ChatColor.BOLD.toString() : ChatColor.RED + ChatColor.BOLD.toString()) + server.getName();
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> description = new ArrayList<>();

        if (server.isOnline()) {
            description.add(ChatColor.YELLOW + "Players: " + ChatColor.WHITE + "(" + server.getOnline() + "/" + server.getMaximum() + ")");
            description.add("");
            description.add(ChatColor.GRAY + "* " + ChatColor.GOLD + "This is a public hub");
            description.add(ChatColor.GRAY + "* " + ChatColor.GOLD + "for all players");
            description.add("");
            description.add((server.isOnline() ? ChatColor.GRAY + Style.UNICODE_ARROWS_RIGHT + ChatColor.YELLOW + " Click to join! " + ChatColor.GRAY + Style.UNICODE_ARROWS_LEFT : Style.YELLOW + "This server is offline"));
        } else {
            description.add("");
            description.add(ChatColor.GRAY + "* " + ChatColor.GOLD + "This is a public hub");
            description.add(ChatColor.GRAY + "* " + ChatColor.GOLD + "for all players");
            description.add("");
            description.add((server.isOnline() ? ChatColor.GRAY + Style.UNICODE_ARROWS_RIGHT + ChatColor.YELLOW + " Click to join! " + ChatColor.GRAY + Style.UNICODE_ARROWS_LEFT : Style.YELLOW + "This server is offline"));
        }
            return description;
        }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        if(!server.isOnline()) {
            player.sendMessage(Style.translate("&eThis server is offline"));
            return;
        }

        if(!player.hasPermission(permission)) {
            player.sendMessage(Style.translate("&cNo permission."));
            return;
        }

        if(player.getServer().getServerName().equalsIgnoreCase(server.getName())) {
            player.sendMessage(Style.translate("&cYou are already connected to this server."));
            return;
        }

        sendPlayerToServer(player, server.getName());
    }


    @Override
    public Material getMaterial(Player player) {
        if (!server.isOnline()) return Material.REDSTONE_BLOCK;

        return Material.EYE_OF_ENDER;
    }

    public void sendPlayerToServer(Player player, String server) {
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            out.writeUTF("Connect");
            out.writeUTF(server);
            player.sendPluginMessage(hQueue.getInstance(), "BungeeCord", b.toByteArray());
            b.close();
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
