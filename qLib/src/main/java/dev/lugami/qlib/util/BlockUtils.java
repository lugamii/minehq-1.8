package dev.lugami.qlib.util;

import java.util.*;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.*;
import org.bukkit.entity.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.*;
import net.minecraft.server.v1_8_R3.*;
import com.google.common.collect.*;

public class BlockUtils {

    private static final Set<Material> INTERACTABLE = ImmutableSet.of(Material.FENCE_GATE, Material.FURNACE, Material.BURNING_FURNACE, Material.BREWING_STAND, Material.CHEST, Material.HOPPER, Material.DISPENSER, Material.WOODEN_DOOR, Material.STONE_BUTTON, Material.WOOD_BUTTON, Material.TRAPPED_CHEST, Material.TRAP_DOOR, Material.LEVER, Material.DROPPER, Material.ENCHANTMENT_TABLE, Material.BED_BLOCK, Material.ANVIL, Material.BEACON);

    public static boolean isInteractable(Block block) {
        return isInteractable(block.getType());
    }

    public static boolean isInteractable(Material material) {
        return BlockUtils.INTERACTABLE.contains(material);
    }

    public static boolean setBlockFast(World world, int x, int y, int z, int blockId, byte data) {
        net.minecraft.server.v1_8_R3.World w = ((CraftWorld)world).getHandle();
        Chunk chunk = w.getChunkAt(x >> 4, z >> 4);
        return a(chunk, x & 0xF, y, z & 0xF, net.minecraft.server.v1_8_R3.Block.getById(blockId), data);
    }

    private static void queueChunkForUpdate(Player player, int cx, int cz) {
        ((CraftPlayer)player).getHandle().chunkCoordIntPairQueue.add(new ChunkCoordIntPair(cx, cz));
    }

    private static boolean a(Chunk that, int i, int j, int k, net.minecraft.server.v1_8_R3.Block block, int l) {
        try {
            that.a(new BlockPosition(i, j, k), block.getBlockData());
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

}