package dev.lugami.bridge.global.disguise.runnable;

import com.google.common.collect.Sets;
import net.minecraft.server.v1_8_R3.*;
import net.minecraft.server.v1_8_R3.WorldServer;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import dev.lugami.bridge.global.util.Tasks;
import dev.lugami.bridge.global.util.mojang.GameProfileUtil;
import lombok.AllArgsConstructor;
import dev.lugami.qlib.nametag.FrozenNametagHandler;
import dev.lugami.qlib.qLib;
import com.mojang.authlib.GameProfile;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class DisguiseRunnable implements Runnable {

	private static Field ENTITY_HUMAN_I;

	static {
		try {
			ENTITY_HUMAN_I = EntityHuman.class.getDeclaredField("bH");
			ENTITY_HUMAN_I.setAccessible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Player player;
	private GameProfile newProfileData;
	private String name;

	@Override
	public void run() {
		MinecraftServer.getServer().getPlayerList().playersByName.remove(this.player.getName());
		MinecraftServer.getServer().getPlayerList().playersByName.put(this.name, ((CraftPlayer) this.player).getHandle());

//		Bukkit.broadcastMessage("DR -> Removing " + this.player.getName());
//		Bukkit.broadcastMessage("DR -> Adding " + this.name);

		EntityPlayer entityPlayer = ((CraftPlayer) this.player).getHandle();
		GameProfile currentProfile = entityPlayer.getProfile();
		currentProfile.getProperties().clear();

		this.newProfileData.getProperties().values()
				.forEach(property -> currentProfile.getProperties().put(property.getName(), property));

		GameProfileUtil.setName(currentProfile, this.name);

		try {
			ENTITY_HUMAN_I.set(entityPlayer, currentProfile);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		Tasks.run(() -> {
			Entity vehicle = this.player.getVehicle();

			if(vehicle != null) {
				vehicle.eject();
			}

			// this will not reload a skin for 1.7 player itself
			int playerId = entityPlayer.getId();
			Location location = this.player.getLocation();

			PacketPlayOutPlayerInfo removeInfo = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer);

			PacketPlayOutEntityDestroy removeEntity = new PacketPlayOutEntityDestroy(playerId);

			PacketPlayOutNamedEntitySpawn addNamed = new PacketPlayOutNamedEntitySpawn(entityPlayer);

			PacketPlayOutPlayerInfo addInfo = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer);

			PacketPlayOutRespawn respawn = new PacketPlayOutRespawn(((WorldServer) entityPlayer.getWorld()).dimension,
					entityPlayer.getWorld().getDifficulty(), entityPlayer.getWorld().worldData.getType(),
					WorldSettings.EnumGamemode.getById(this.player.getGameMode().getValue()));

			Set<PacketPlayOutPosition.EnumPlayerTeleportFlags> enums = Sets.newHashSet(PacketPlayOutPosition.EnumPlayerTeleportFlags.values());
			PacketPlayOutPosition pos = new PacketPlayOutPosition(location.getX(), location.getY(), location.getZ(), location.getYaw(),
					location.getPitch(), enums);

			PacketPlayOutEntityEquipment itemhand = new PacketPlayOutEntityEquipment(playerId, 0,
					CraftItemStack.asNMSCopy(this.player.getItemInHand()));

			PacketPlayOutEntityEquipment helmet = new PacketPlayOutEntityEquipment(playerId, 4,
					CraftItemStack.asNMSCopy(this.player.getInventory().getHelmet()));

			PacketPlayOutEntityEquipment chestplate = new PacketPlayOutEntityEquipment(playerId, 3,
					CraftItemStack.asNMSCopy(this.player.getInventory().getChestplate()));

			PacketPlayOutEntityEquipment leggings = new PacketPlayOutEntityEquipment(playerId, 2,
					CraftItemStack.asNMSCopy(this.player.getInventory().getLeggings()));

			PacketPlayOutEntityEquipment boots = new PacketPlayOutEntityEquipment(playerId, 1,
					CraftItemStack.asNMSCopy(this.player.getInventory().getBoots()));

			PacketPlayOutHeldItemSlot slot = new PacketPlayOutHeldItemSlot(this.player.getInventory().getHeldItemSlot());

			for(Player worldPlayer : this.player.getWorld().getPlayers()) {
				CraftPlayer craftOnline = (CraftPlayer) worldPlayer;
				PlayerConnection connection = craftOnline.getHandle().playerConnection;

				if(worldPlayer.getUniqueId().equals(this.player.getUniqueId())) {
					connection.sendPacket(removeInfo);
					connection.sendPacket(addInfo);
					connection.sendPacket(respawn);
					connection.sendPacket(pos);
					connection.sendPacket(slot);

					craftOnline.updateScaledHealth();
					craftOnline.getHandle().triggerHealthUpdate();
					craftOnline.updateInventory();

					Tasks.run(() -> craftOnline.getHandle().updateAbilities());
					continue;
				}

				connection.sendPacket(removeEntity);
				connection.sendPacket(removeInfo);

				if(worldPlayer.canSee(this.player)) {
					connection.sendPacket(addInfo);
					connection.sendPacket(addNamed);
					connection.sendPacket(itemhand);
					connection.sendPacket(helmet);
					connection.sendPacket(chestplate);
					connection.sendPacket(leggings);
					connection.sendPacket(boots);
				}
			}

			this.player.updateInventory();
			this.player.setGameMode(this.player.getGameMode());
			this.player.getInventory().setHeldItemSlot(this.player.getInventory().getHeldItemSlot());
			this.player.setFoodLevel(this.player.getFoodLevel());
			this.player.setSaturation(this.player.getSaturation());
			this.player.setMaxHealth(this.player.getMaxHealth());
			this.player.setHealth(this.player.getHealth());
			this.player.setExp(this.player.getExp());
			this.player.setTotalExperience(this.player.getTotalExperience());
			this.player.setWalkSpeed(this.player.getWalkSpeed());
			this.player.hidePlayer(this.player);
			this.player.showPlayer(this.player);
			if (FrozenNametagHandler.isInitiated()) {
				this.player.setMetadata("qLibNametag-LoggedIn", new FixedMetadataValue(qLib.getInstance(), true));
				//FrozenNametagHandler.initiatePlayer(this.player);
				FrozenNametagHandler.reloadPlayer(this.player);
				FrozenNametagHandler.reloadOthersFor(this.player);
			}
		});
	}
}