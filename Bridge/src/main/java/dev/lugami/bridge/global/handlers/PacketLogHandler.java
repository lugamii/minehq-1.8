package dev.lugami.bridge.global.handlers;

import com.google.common.collect.Maps;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import dev.lugami.bridge.bukkit.Bridge;
import dev.lugami.bridge.global.packetlogger.PacketLog;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class PacketLogHandler {
    private Map<UUID, ArrayList<PacketLog>> packetlog = Maps.newHashMap();


    public void log (Player player, Packet packet) {
        ArrayList<PacketLog> packetLogs;
        if (!packetlog.containsKey(player.getUniqueId())) {
            packetLogs = new ArrayList<>();
        } else {
            packetLogs = packetlog.get(player.getUniqueId());
        }
        packetLogs.add(new PacketLog(packet, player.getLocation(), System.currentTimeMillis(), Bridge.getInstance().getBlockedPackets().contains(packet)));
        packetlog.put(player.getUniqueId(), packetLogs);
        Bridge.getInstance().getBlockedPackets().remove(packet);
    }
    public ArrayList<PacketLog> getLog(UUID uuid) {
        if (!packetlog.containsKey(uuid)) {
            return new ArrayList<>();
        }
        return packetlog.get(uuid);
    }
}
