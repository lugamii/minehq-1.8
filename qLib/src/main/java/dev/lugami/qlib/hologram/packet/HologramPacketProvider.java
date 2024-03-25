package dev.lugami.qlib.hologram.packet;

import dev.lugami.qlib.hologram.construct.HologramLine;
import org.bukkit.Location;

public interface HologramPacketProvider {

	HologramPacket getPacketsFor(Location location, HologramLine line);

	//TODO: add more versions
}
