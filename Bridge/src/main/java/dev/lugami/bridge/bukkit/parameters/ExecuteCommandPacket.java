package dev.lugami.bridge.bukkit.parameters;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import dev.lugami.qlib.xpacket.XPacket;

@AllArgsConstructor
public class ExecuteCommandPacket implements XPacket {

    public String command;

    @Override
    public void onReceive() {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }
}
