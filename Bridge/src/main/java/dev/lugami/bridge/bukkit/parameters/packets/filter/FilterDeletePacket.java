package dev.lugami.bridge.bukkit.parameters.packets.filter;

import dev.lugami.bridge.BridgeGlobal;
import org.bukkit.Bukkit;
import dev.lugami.bridge.global.filter.Filter;
import dev.lugami.qlib.xpacket.XPacket;

public class FilterDeletePacket implements XPacket {

    private String filter;
    private String sender;

    public FilterDeletePacket(Filter filter, String sender) {
        this.filter = filter.getPattern();
        this.sender = sender;
    }

    @Override
    public void onReceive() {
        if(sender.equalsIgnoreCase(Bukkit.getServerName())) return;
        BridgeGlobal.getFilterHandler().removeFilter(BridgeGlobal.getFilterHandler().getFilter(filter));
    }
}
