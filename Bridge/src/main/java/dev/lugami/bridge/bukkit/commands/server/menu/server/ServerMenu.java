package dev.lugami.bridge.bukkit.commands.server.menu.server;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import dev.lugami.bridge.BridgeGlobal;
import dev.lugami.bridge.bukkit.commands.server.menu.server.buttons.ServerButton;
import dev.lugami.bridge.global.status.BridgeServer;
import dev.lugami.qlib.menu.Button;
import dev.lugami.qlib.menu.Menu;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerMenu extends Menu {

    public ServerMenu() {
        super(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Servers");
        setAutoUpdate(true);
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttonMap = new HashMap<>();
        AtomicInteger atomicInteger = new AtomicInteger(0);
        BridgeGlobal.getServerHandler().getServers().values().stream().sorted(Comparator.comparingLong(this::getStartTime)).forEach(s -> {
            buttonMap.put(atomicInteger.getAndIncrement(), new ServerButton(s));
        });
        return buttonMap;
    }

    private long getStartTime(BridgeServer server) {
        try {
            return server.getBootTime();
        } catch (Exception ignored) {}
        return System.currentTimeMillis();
    }
}