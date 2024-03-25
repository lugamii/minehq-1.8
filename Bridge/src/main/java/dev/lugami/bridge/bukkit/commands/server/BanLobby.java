package dev.lugami.bridge.bukkit.commands.server;

import lombok.Getter;
import dev.lugami.bridge.bukkit.Bridge;

public class BanLobby {

    @Getter
    private boolean maintenance, restricted, banLobbyEnabled;

    public BanLobby() {
        this.banLobbyEnabled = Bridge.getInstance().getConfig().getBoolean("server.ban-lobby");
    }

    //TODO: Do this shit later

}

