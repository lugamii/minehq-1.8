// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.handler;

import java.beans.ConstructorProperties;

import com.moonsworth.fallback.Fallback;
import com.moonsworth.fallback.check.ICheck;
import com.moonsworth.fallback.client.EnumClientType;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.event.PlayerAlertEvent;
import com.moonsworth.fallback.event.PlayerBanEvent;
import com.moonsworth.fallback.player.PlayerData;
import com.moonsworth.fallback.util.BlockPos;
import com.moonsworth.fallback.util.CustomLocation;
import dev.lugami.spigot.chunk.CraftFakeMultiBlockChange;
import dev.lugami.spigot.chunk.FakeMultiBlockChange;
import dev.lugami.spigot.handler.PacketHandler;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.entity.Entity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.event.Event;

import java.util.Arrays;
import java.lang.reflect.Field;
import java.util.ArrayList;

import org.bukkit.entity.Player;
import com.moonsworth.fallback.check.checks.PacketCheck;
import org.bukkit.Material;
import java.util.List;

import static net.minecraft.server.v1_8_R3.PacketPlayInBlockDig.EnumPlayerDigType.*;

public class CustomPacketHandler implements PacketHandler
{
    private Fallback plugin;
    private static List<Material> instantBreakTypes;
    
    public void handleReceivedPacket(final PlayerConnection playerConnection, final Packet packet) {
        try {
            final Player player = (Player)playerConnection.getPlayer();
            final PlayerData playerData = this.plugin.getPlayerDataManager().getPlayerData(player);
            if (playerData == null) {
                return;
            }
            if (playerData.isSniffing()) {
                this.handleSniffedPacket(packet, playerData);
            }
            final String simpleName2;
            final String simpleName = simpleName2 = packet.getClass().getSimpleName();
            switch (simpleName2) {
                /**
                case "PacketPlayInCustomPayload": {
                    if (!playerData.getClient().isHacked()) {
                        this.handleCustomPayload((PacketPlayInCustomPayload)packet, playerData, player);
                        break;
                    }
                    break;
                }
                **/
                case "PacketPlayInPosition":
                case "PacketPlayInPositionLook":
                case "PacketPlayInLook":
                case "PacketPlayInFlying": {
                    this.handleFlyPacket((PacketPlayInFlying)packet, playerData);
                    break;
                }
                case "PacketPlayInKeepAlive": {
                    this.handleKeepAlive((PacketPlayInKeepAlive)packet, playerData, player);
                    break;
                }
                case "PacketPlayInUseEntity": {
                    this.handleUseEntity((PacketPlayInUseEntity)packet, playerData, player);
                    break;
                }
                case "PacketPlayInBlockPlace": {
                    playerData.setPlacing(true);
                    break;
                }
                case "PacketPlayInCloseWindow": {
                    playerData.setInventoryOpen(false);
                    break;
                }
                case "PacketPlayInClientCommand": {
                    if (((PacketPlayInClientCommand)packet).a() == PacketPlayInClientCommand.EnumClientCommand.OPEN_INVENTORY_ACHIEVEMENT) {
                        playerData.setInventoryOpen(true);
                        break;
                    }
                    break;
                }
                case "PacketPlayInEntityAction": {
                    final PacketPlayInEntityAction.EnumPlayerAction action = ((PacketPlayInEntityAction)packet).b();
                    if (action == PacketPlayInEntityAction.EnumPlayerAction.START_SPRINTING) {
                        playerData.setSprinting(true);
                        break;
                    }
                    if (action == PacketPlayInEntityAction.EnumPlayerAction.STOP_SPRINTING) {
                        playerData.setSprinting(false);
                        break;
                    }
                    break;
                }
                case "PacketPlayInBlockDig": {
                    final PacketPlayInBlockDig blockDig = (PacketPlayInBlockDig)packet;
                    final PacketPlayInBlockDig.EnumPlayerDigType digType = blockDig.c();
                    final int x = blockDig.a().getX();
                    final int y = blockDig.a().getY();
                    final int z = blockDig.a().getZ();
                    if (playerData.getFakeBlocks().contains(new BlockPos(x, y, z))) {
                        playerData.setFakeDigging(true);
                        playerData.setDigging(false);
                        break;
                    }
                    playerData.setFakeDigging(false);
                    if (digType == START_DESTROY_BLOCK) {
                        final Material type = player.getWorld().isChunkLoaded(x >> 4, z >> 4) ? player.getWorld().getBlockAt(x, y, z).getType() : null;
                        playerData.setInstantBreakDigging(CustomPacketHandler.instantBreakTypes.contains(type));
                        playerData.setDigging(true);
                        break;
                    }
                    if (digType == ABORT_DESTROY_BLOCK || digType == STOP_DESTROY_BLOCK) {
                        playerData.setInstantBreakDigging(false);
                        playerData.setDigging(false);
                        break;
                    }
                    break;
                }
                case "PacketPlayInArmAnimation": {
                    playerData.setLastAnimationPacket(System.currentTimeMillis());
                    break;
                }
            }
            for (final Class<? extends ICheck> checkClass : PlayerData.CHECKS) {
                if (!Fallback.instance.getDisabledChecks().contains(checkClass.getSimpleName().toUpperCase())) {
                    final ICheck check = (ICheck)playerData.getCheck(checkClass);
                    if (check != null && check.getType() == Packet.class) {
                        check.handleCheck((Player)playerConnection.getPlayer(), packet);
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void handleSentPacket(final PlayerConnection playerConnection, final Packet packet) {
        try {
            final Player player = (Player)playerConnection.getPlayer();
            final PlayerData playerData = this.plugin.getPlayerDataManager().getPlayerData(player);
            if (playerData == null) {
                return;
            }
            final String simpleName2;
            final String simpleName = simpleName2 = packet.getClass().getSimpleName();
            switch (simpleName2) {
    //            case "PacketPlayOutRelEntityMoveLook": {}
  //              case "PacketPlayOutEntityLook": {}
//                case "PacketPlayOutRelEntityMove": {}
                case "PacketPlayOutEntityVelocity": {
                    this.handleVelocityOut((PacketPlayOutEntityVelocity)packet, playerData, player);
                    return;
                }
                case "PacketPlayOutCloseWindow": {
                    if (!playerData.keepAliveExists(-1)) {
                        playerConnection.sendPacket(new PacketPlayOutKeepAlive(-1));
                    }
                    return;
                }
                case "PacketPlayOutPosition": {
                    this.handlePositionPacket((PacketPlayOutPosition)packet, playerData);
                    return;
                }
                case "PacketPlayOutBlockChange": {
                    final PacketPlayOutBlockChange blockChange = (PacketPlayOutBlockChange)packet;
                    if (blockChange.block != Blocks.AIR) {
                        playerData.getFakeBlocks().add(new BlockPos(blockChange.getPosition().getX(), blockChange.getPosition().getY(), blockChange.getPosition().getZ()));
                        return;
                    }
                    playerData.getFakeBlocks().remove(new BlockPos(blockChange.getPosition().getX(), blockChange.getPosition().getY(), blockChange.getPosition().getZ()));
                    return;
                }
                case "PacketPlayOutExplosion": {
                    this.handleExplosionPacket((PacketPlayOutExplosion)packet, playerData);
                    return;
                }
                case "PacketPlayOutEntityTeleport": {
                    this.handleTeleportPacket((PacketPlayOutEntityTeleport)packet, playerData, player);
                    return;
                }
                case "PacketPlayOutKeepAlive": {
                    playerData.addKeepAliveTime(((PacketPlayOutKeepAlive)packet).getA());
                    return;
                }
            }
            if (PacketPlayOutEntity.class.isAssignableFrom(packet.getClass())) {
                this.handleEntityPacket((PacketPlayOutEntity)packet, playerData, player);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void handleSniffedPacket(final Packet packet, final PlayerData playerData) {
        try {
            final StringBuilder builder = new StringBuilder();
            builder.append(packet.getClass().getSimpleName());
            builder.append(" (timestamp = ");
            builder.append(System.currentTimeMillis());
            final List<Field> fieldsList = new ArrayList<Field>();
            fieldsList.addAll(Arrays.asList(packet.getClass().getDeclaredFields()));
            fieldsList.addAll(Arrays.asList(packet.getClass().getSuperclass().getDeclaredFields()));
            for (final Field field : fieldsList) {
                if (field.getName().equalsIgnoreCase("timestamp")) {
                    continue;
                }
                field.setAccessible(true);
                builder.append(", ");
                builder.append(field.getName());
                builder.append(" = ");
                builder.append(field.get(packet));
            }
            builder.append(")");
            playerData.getSniffedPacketBuilder().append(builder.toString());
            playerData.getSniffedPacketBuilder().append("\n");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void handleCustomPayload(final PacketPlayInCustomPayload packet, final PlayerData playerData, final Player player) {
        /**
        final String payloadTag = packet.c();
        for (final EnumClientType clientType : EnumClientType.values()) {
            if (clientType.getPayloadTag() != null && clientType.getPayloadTag().equals(payloadTag)) {
                playerData.setClient(clientType);
                break;
            }
        }
        if (payloadTag.equals("REGISTER")) {
            try {
                final String registerType = new String(packet.a());
                if (registerType.contains("CB-Client")) {
                    playerData.setClient(EnumClientType.CHEAT_BREAKER);
                }
                else if (registerType.contains("Lunar-Client")) {
                    playerData.setClient(EnumClientType.Lunar_Client);
                }
                else if (registerType.equalsIgnoreCase("AC-Client")) {
                    playerData.setClient(EnumClientType.AERO_CLIENT);
                }
                else if (registerType.equalsIgnoreCase("CC")) {
                    playerData.setClient(EnumClientType.COSMIC_CLIENT);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        if (playerData.getClient() != null && playerData.getClient().isHacked()) {
            this.plugin.getServer().getPluginManager().callEvent(new PlayerAlertEvent(AlertType.RELEASE, player, playerData.getClient().getName()));
            playerData.setRandomBanRate(500.0);
            playerData.setRandomBanReason(playerData.getClient().getName());
            playerData.setRandomBan(true);
        }
        **/
    }
    
    private void handleFlyPacket(final PacketPlayInFlying packet, final PlayerData playerData) {
        final CustomLocation toLocation = new CustomLocation(packet.a(), packet.b(), packet.c(), packet.d(), packet.e());
        final CustomLocation fromLocation = playerData.getLastMovePacket();
        ++playerData.currentTick;
        if (playerData.getTeleportTicks() > 0) {
            playerData.setTeleportTicks(playerData.getTeleportTicks() - 1);
        }
        if (playerData.getTeleportTicks() < 0) {
            playerData.setTeleportTicks(0);
        }
        if (playerData.getRespawnTicks() > 0) {
            playerData.setRespawnTicks(playerData.getRespawnTicks() - 1);
        }
        if (playerData.getRespawnTicks() < 0) {
            playerData.setRespawnTicks(0);
        }
        if (packet.g()) {
            playerData.setStandTicks(0);
        }
        else {
            playerData.setStandTicks(playerData.getStandTicks() + 1);
        }
        if (fromLocation != null) {
            if (!packet.g()) {
                toLocation.setX(fromLocation.getX());
                toLocation.setY(fromLocation.getY());
                toLocation.setZ(fromLocation.getZ());
            }
            if (!packet.h()) {
                toLocation.setYaw(fromLocation.getYaw());
                toLocation.setPitch(fromLocation.getPitch());
            }
            if (System.currentTimeMillis() - fromLocation.getTimestamp() > 110L) {
                playerData.setLastDelayedMovePacket(System.currentTimeMillis());
            }
        }
        if (playerData.isSetInventoryOpen()) {
            playerData.setInventoryOpen(false);
            playerData.setSetInventoryOpen(false);
        }
        playerData.setLastMovePacket(toLocation);
        playerData.setPlacing(false);
        playerData.setAllowTeleport(false);
        if (packet instanceof PacketPlayInFlying.PacketPlayInPositionLook && playerData.allowTeleport(toLocation)) {
            playerData.setAllowTeleport(true);
            playerData.setTeleportTicks(10);
        }
    }
    
    private void handleKeepAlive(final PacketPlayInKeepAlive packet, final PlayerData playerData, final Player player) {
        final int id = packet.a();
        if (playerData.keepAliveExists(id)) {
            if (id == -1) {
                playerData.setSetInventoryOpen(true);
            }
            else {
                playerData.setPing(System.currentTimeMillis() - playerData.getKeepAliveTime(id));
            }
            playerData.removeKeepAliveTime(id);
        }
        else if (id != 0 && playerData.InvalidKeepAlivesVerbose++ > 0) {
            final PlayerAlertEvent alertEvent = new PlayerAlertEvent(AlertType.RELEASE, player, "Illegal Keep Alive");
            this.plugin.getServer().getPluginManager().callEvent(alertEvent);
        }
    }
    
    private void handleUseEntity(final PacketPlayInUseEntity packet, final PlayerData playerData, final Player player) {
        if (packet.a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
            playerData.setLastAttackPacket(System.currentTimeMillis());
            if (!playerData.isAttackedSinceVelocity()) {
                playerData.setVelocityX(playerData.getVelocityX() * 0.6);
                playerData.setVelocityZ(playerData.getVelocityZ() * 0.6);
                playerData.setAttackedSinceVelocity(true);
            }
            if (!playerData.isBanning() && playerData.isRandomBan() && Math.random() * playerData.getRandomBanRate() < 1.0) {
                playerData.setBanning(true);
                final PlayerBanEvent event = new PlayerBanEvent(player, playerData.getRandomBanReason());
                this.plugin.getServer().getPluginManager().callEvent((Event)event);
            }
            final net.minecraft.server.v1_8_R3.Entity targetEntity = packet.a(((CraftPlayer)player).getHandle().getWorld());
            if (targetEntity instanceof EntityPlayer) {
                final Player target = (Player)targetEntity.getBukkitEntity();
                playerData.setLastTarget(target.getUniqueId());
                playerData.setLastTargetEntity((Entity)target);
            }
        }
    }
    
    private void handleVelocityOut(final PacketPlayOutEntityVelocity packet, final PlayerData playerData, final Player player) {
        if (packet.getA() == player.getEntityId()) {
            final double x = Math.abs(packet.getB() / 8000.0);
            final double y = packet.getC() / 8000.0;
            final double z = Math.abs(packet.getD() / 8000.0);
            if (x > 0.0 || z > 0.0) {
                playerData.setVelocityH((int)(((x + z) / 2.0 + 2.0) * 15.0));
            }
            if (y > 0.0) {
                playerData.setVelocityV((int)(Math.pow(y + 2.0, 2.0) * 5.0));
                if (playerData.isOnGround() && player.getLocation().getY() % 1.0 == 0.0) {
                    playerData.setVelocityX(x);
                    playerData.setVelocityY(y);
                    playerData.setVelocityZ(z);
                    playerData.getVelocityTimer().reset();
                    playerData.setLastVelocity(System.currentTimeMillis());
                    playerData.setAttackedSinceVelocity(false);
                }
            }
        }
    }
    
    private void handleExplosionPacket(final PacketPlayOutExplosion packet, final PlayerData playerData) {
        final float x = Math.abs(packet.getF());
        final float y = packet.getG();
        final float z = Math.abs(packet.getH());
        if (x > 0.0f || z > 0.0f) {
            playerData.setVelocityH((int)(((x + z) / 2.0f + 2.0f) * 15.0f));
        }
        if (y > 0.0f) {
            playerData.setVelocityV((int)(Math.pow(y + 2.0f, 2.0) * 5.0));
        }
    }
    
    private void handleEntityPacket(final PacketPlayOutEntity packet, final PlayerData playerData, final Player player) {
        final net.minecraft.server.v1_8_R3.Entity targetEntity = getEntity(((CraftPlayer)player).getHandle().getWorld(), packet.getA());
        if (targetEntity instanceof EntityPlayer) {
            final Player target = (Player)targetEntity.getBukkitEntity();
            final CustomLocation customLocation = playerData.getLastPlayerPacket(target.getUniqueId(), 1);
            if (customLocation != null) {
                final double x = packet.getB() / 32.0;
                final double y = packet.getC() / 32.0;
                final double z = packet.getD() / 32.0;
                float yaw = packet.getE() * 360.0f / 256.0f;
                float pitch = packet.getF() * 360.0f / 256.0f;
                if (!packet.isG()) {
                    yaw = customLocation.getYaw();
                    pitch = customLocation.getPitch();
                }
                playerData.addPlayerPacket(target.getUniqueId(), new CustomLocation(customLocation.getX() + x, customLocation.getY() + y, customLocation.getZ() + z, yaw, pitch));
            }
        }
    }
    
    private void handleTeleportPacket(final PacketPlayOutEntityTeleport packet, final PlayerData playerData, final Player player) {
        final net.minecraft.server.v1_8_R3.Entity targetEntity = getEntity(((CraftPlayer)player).getHandle().getWorld(), packet.getA());
        if (targetEntity instanceof EntityPlayer) {
            final Player target = (Player)targetEntity.getBukkitEntity();
            final double x = packet.getB() / 32.0;
            final double y = packet.getC() / 32.0;
            final double z = packet.getD() / 32.0;
            final float yaw = packet.getE() * 360.0f / 256.0f;
            final float pitch = packet.getF() * 360.0f / 256.0f;
            playerData.addPlayerPacket(target.getUniqueId(), new CustomLocation(x, y, z, yaw, pitch));
        }
    }

    private net.minecraft.server.v1_8_R3.Entity getEntity(World world, int id) {
        for (net.minecraft.server.v1_8_R3.Entity entity : world.entityList) {
            if (entity.getId() == id) {
                return entity;
            }
        }
        return null;
    }
    
    private void handlePositionPacket(final PacketPlayOutPosition packet, final PlayerData playerData) {
        if (packet.getE() > 90.0f) {
            packet.setE(90.0f);
        }
        else if (packet.getE() < -90.0f) {
            packet.setE(-90.0f);
        }
        else if (packet.getE() == 0.0f) {
            packet.setE(0.492832f);
        }
        playerData.setVelocityY(0.0);
        playerData.setVelocityX(0.0);
        playerData.setVelocityZ(0.0);
        playerData.setAttackedSinceVelocity(false);
        playerData.addTeleportLocation(new CustomLocation(packet.getA(), packet.getB(), packet.getC(), packet.getD(), packet.getE()));
    }
    
    private float getAngle(final double posX, final double posZ, final CustomLocation location) {
        final double x = posX - location.getX();
        final double z = posZ - location.getZ();
        float newYaw = (float)Math.toDegrees(-Math.atan(x / z));
        if (z < 0.0 && x < 0.0) {
            newYaw = (float)(90.0 + Math.toDegrees(Math.atan(z / x)));
        }
        else if (z < 0.0 && x > 0.0) {
            newYaw = (float)(-90.0 + Math.toDegrees(Math.atan(z / x)));
        }
        return newYaw;
    }
    
    @ConstructorProperties({ "plugin" })
    public CustomPacketHandler(final Fallback plugin) {
        this.plugin = plugin;
    }
    
    static {
        CustomPacketHandler.instantBreakTypes = Arrays.asList(Material.LONG_GRASS, Material.WATER_LILY, Material.DEAD_BUSH, Material.YELLOW_FLOWER, Material.RED_ROSE, Material.DOUBLE_PLANT, Material.SUGAR_CANE, Material.SUGAR_CANE_BLOCK);
    }
}
