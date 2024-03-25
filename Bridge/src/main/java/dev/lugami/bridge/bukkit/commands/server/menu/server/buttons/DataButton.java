package dev.lugami.bridge.bukkit.commands.server.menu.server.buttons;

import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import dev.lugami.qlib.menu.Button;

import java.util.Collections;
import java.util.List;

@AllArgsConstructor
public class DataButton extends Button {

    private String data;
    private String value;

    @Override
    public String getName(Player player) {
        return ChatColor.LIGHT_PURPLE + data;
    }

    @Override
    public List<String> getDescription(Player player) {
        return Collections.singletonList(ChatColor.GRAY + value);
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.PAPER;
    }
}
