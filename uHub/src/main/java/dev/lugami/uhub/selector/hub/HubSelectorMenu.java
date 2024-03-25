package dev.lugami.uhub.selector.hub;

import dev.lugami.uhub.selector.hub.button.JoinHubButton;
import dev.lugami.uhub.uHub;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import dev.lugami.bridge.BridgeGlobal;
import dev.lugami.bridge.global.status.BridgeServer;
import dev.lugami.qlib.menu.Button;
import dev.lugami.qlib.menu.Menu;

import java.util.HashMap;
import java.util.Map;

public class HubSelectorMenu extends Menu {

    @Override
    public boolean isAutoUpdate() {
        return true;
    }

    public String getTitle(Player player) {
        return ChatColor.DARK_GRAY + "Select a hub to join";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();

        int i = 0;
        for (String key : uHub.getInstance().getConfig().getConfigurationSection("hubs").getKeys(false)) {
            BridgeServer server = BridgeGlobal.getServerHandler().getServer(key);
            String permission = uHub.getInstance().getConfig().getString("hubs." + key + ".permission");
            if (server == null) continue;
            buttons.put(i++, new JoinHubButton(server, permission == null ? "" : permission));
        }
        return buttons;
    }
}