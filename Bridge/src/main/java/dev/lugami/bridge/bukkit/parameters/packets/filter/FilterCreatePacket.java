package dev.lugami.bridge.bukkit.parameters.packets.filter;

import dev.lugami.bridge.BridgeGlobal;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import dev.lugami.bridge.global.filter.Filter;
import dev.lugami.qlib.xpacket.XPacket;

@AllArgsConstructor
public class FilterCreatePacket implements XPacket {

    private Filter filter;
    private String sender;

    @Override
    public void onReceive() {
        if(sender.equalsIgnoreCase(Bukkit.getServerName())) return;
        BridgeGlobal.getFilterHandler().addFilter(filter);
    }
}
