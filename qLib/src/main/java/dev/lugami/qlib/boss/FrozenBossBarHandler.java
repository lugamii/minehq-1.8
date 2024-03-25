package dev.lugami.qlib.boss;

import com.comphenix.protocol.ProtocolLibrary;
import com.google.common.base.Preconditions;
import gnu.trove.map.hash.TObjectIntHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import lombok.Data;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R3.DataWatcher;
import dev.lugami.qlib.util.EntityUtils;
import dev.lugami.qlib.qLib;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import dev.lugami.qlib.util.PlayerUtils;

import java.beans.ConstructorProperties;
import java.lang.reflect.Field;
import java.util.*;

public class FrozenBossBarHandler {
    private static boolean initiated = false;
    private static final Map<UUID, BarData> displaying = new HashMap<>();
    private static final Map<UUID, Integer> lastUpdatedPosition = new HashMap<>();
    private static TObjectIntHashMap classToIdMap = null;

    public void init() {
        Preconditions.checkState(!initiated);
        try {
            final Field dataWatcherClassToIdField = DataWatcher.class.getDeclaredField("classToId");

            dataWatcherClassToIdField.setAccessible(true);

            this.classToIdMap = (TObjectIntHashMap)dataWatcherClassToIdField.get(null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        initiated = true;
        Bukkit.getScheduler().runTaskTimer(qLib.getInstance(), () -> {
            for (UUID uuid : this.displaying.keySet()) {

                final Player player = qLib.getInstance().getServer().getPlayer(uuid);

                if (player == null) {
                    continue;
                }

                final int updateTicks = PlayerUtils.getProtocol(player) >= 47 ? 60 : 3;

                if (this.lastUpdatedPosition.containsKey(player.getUniqueId()) && MinecraftServer.currentTick -
                        lastUpdatedPosition.get(player.getUniqueId()) < updateTicks) {
                    return;
                }

                this.updatePosition(player);

                this.lastUpdatedPosition.put(player.getUniqueId(),MinecraftServer.currentTick);
            }
        }, 1L, 1L);
        Bukkit.getPluginManager().registerEvents(new Listener(){

            @EventHandler
            public void onPlayerQuit(PlayerQuitEvent event) {
                removeBossBar(event.getPlayer());
            }

            @EventHandler
            public void onPlayerTeleport(PlayerTeleportEvent event) {
                Player player = event.getPlayer();
                if (!displaying.containsKey(player.getUniqueId())) {
                    return;
                }
                BarData data = displaying.get(player.getUniqueId());
                String message = data.message;
                float health = data.health;
                removeBossBar(player);
                setBossBar(player, message, health);
            }
        }, qLib.getInstance());
    }

    public void setBossBar(Player player, String message, float health) {
        try {
            if (message == null) {
                removeBossBar(player);
                return;
            }
            Preconditions.checkArgument(health >= 0.0f && health <= 1.0f, "Health must be between 0 and 1");
            if (message.length() > 64) {
                message = message.substring(0, 64);
            }
            message = ChatColor.translateAlternateColorCodes('&', message);
            if (!displaying.containsKey(player.getUniqueId())) {
                sendSpawnPacket(player, message, health);
            } else {
                sendUpdatePacket(player, message, health);
            }
            displaying.get(player.getUniqueId()).message = message;
            displaying.get(player.getUniqueId()).health = health;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeBossBar(Player player) {
        if (!displaying.containsKey(player.getUniqueId())) {
            return;
        }
        int entityId = displaying.get(player.getUniqueId()).entityId;
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(entityId));
        displaying.remove(player.getUniqueId());
        lastUpdatedPosition.remove(player.getUniqueId());
    }

    private void sendSpawnPacket(Player bukkitPlayer, String message, float health) throws Exception {
        final EntityPlayer player = ((CraftPlayer)bukkitPlayer).getHandle();
        final int version = ProtocolLibrary.getProtocolManager().getProtocolVersion(bukkitPlayer);

        this.displaying.put(bukkitPlayer.getUniqueId(), new BarData(EntityUtils.getFakeEntityId(), message, health));

        final BarData stored = this.displaying.get(bukkitPlayer.getUniqueId());

        final PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving();
        packet.id = (stored.getEntityId()); //this.spawnPacketAField.set(packet,stored.getEntityId());

        final DataWatcher watcher = new DataWatcher((Entity)null);

        if (version < 47) {
            packet.type = ((byte)EntityType.ENDER_DRAGON.getTypeId());

            watcher.a(6, health * 200.0F);
            packet.x = ((int)(player.locX * 32.0D));
            packet.y = (-6400);
            packet.z = ((int)(player.locZ * 32.0D));

        } else {
            packet.type = ((byte)EntityType.WITHER.getTypeId());

            watcher.a(6, health * 300.0F);
            watcher.a(20, 880);

            final double pitch = Math.toRadians(player.pitch);
            final double yaw = Math.toRadians(player.yaw);
            packet.x = ((int)((player.locX - Math.sin(yaw) * Math.cos(pitch) * 32.0D) * 32.0D));
            packet.y = ((int)((player.locY - Math.sin(pitch) * 32.0D) * 32.0D));
            packet.z = ( (int)((player.locZ + Math.sin(yaw) * Math.cos(pitch) * 32.0D) * 32.0D));
        }

        watcher.a(version < 47 ? 10 : 2, message);
        packet.l = (watcher);

        player.playerConnection.sendPacket(packet);
    }

    private void sendUpdatePacket(Player bukkitPlayer, String message, float health) {

        final EntityPlayer player = ((CraftPlayer)bukkitPlayer).getHandle();
        final int version = PlayerUtils.getProtocol(bukkitPlayer);
        final BarData stored = this.displaying.get(bukkitPlayer.getUniqueId());
        final PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata();
        packet.a = (stored.getEntityId());

        final List<DataWatcher.WatchableObject> objects = new ArrayList<>();

        if (health != stored.getHealth()) {

            if (version != 47) {
                objects.add(createWatchableObject(6, health * 200.0F));
            } else {
                objects.add(createWatchableObject(6, health * 300.0F));
            }

        }

        if (!message.equals(stored.getMessage())) {
            objects.add(createWatchableObject(version != 47 ? 10 : 2, message));
        }
        packet.b = objects;
        player.playerConnection.sendPacket(packet);
    }

    private static DataWatcher.WatchableObject createWatchableObject(int id, Object object) {
        return new DataWatcher.WatchableObject(classToIdMap.get(object.getClass()), id, object);
    }

    private void updatePosition(Player bukkitPlayer) {

        if (!this.displaying.containsKey(bukkitPlayer.getUniqueId())) {
            return;
        }

        final EntityPlayer player = ((CraftPlayer)bukkitPlayer).getHandle();
        final int version = PlayerUtils.getProtocol(bukkitPlayer);

        int x;
        int y;
        int z;

        if (version != 47) {
            x = (int)(player.locX * 32.0D);
            y = -6400;
            z = (int)(player.locZ * 32.0D);
        } else {
            final double pitch = Math.toRadians((double)player.pitch);
            final double yaw = Math.toRadians((double)player.yaw);
            x = (int)((player.locX - Math.sin(yaw) * Math.cos(pitch) * 32.0D) * 32.0D);
            y = (int)((player.locY - Math.sin(pitch) * 32.0D) * 32.0D);
            z = (int)((player.locZ + Math.cos(yaw) * Math.cos(pitch) * 32.0D) * 32.0D);
        }

        player.playerConnection.sendPacket(new PacketPlayOutEntityTeleport(
                this.displaying.get(bukkitPlayer.getUniqueId()).getEntityId(),x,y,z,(byte)0,(byte)0, true));
    }

    @Data
    private static class BarData {
        private final int entityId;
        private String message;
        private float health;

        @ConstructorProperties(value={"entityId", "message", "health"})
        public BarData(int entityId, String message, float health) {
            this.entityId = entityId;
            this.message = message;
            this.health = health;
        }
    }

}

