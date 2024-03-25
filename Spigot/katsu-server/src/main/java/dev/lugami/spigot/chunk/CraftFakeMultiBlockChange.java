package dev.lugami.spigot.chunk;

import net.minecraft.server.PacketPlayOutMultiBlockChange;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class CraftFakeMultiBlockChange implements FakeMultiBlockChange {

	private final PacketPlayOutMultiBlockChange packet;

	public CraftFakeMultiBlockChange(PacketPlayOutMultiBlockChange packet) {
		this.packet = packet;
	}

	@Override
	public void sendTo(Player player) {
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(this.packet);
	}
}
