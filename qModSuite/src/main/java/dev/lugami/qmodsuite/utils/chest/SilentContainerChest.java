package dev.lugami.qmodsuite.utils.chest;

import net.minecraft.server.v1_8_R3.Container;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.IInventory;
import net.minecraft.server.v1_8_R3.InventoryLargeChest;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.PlayerInventory;
import net.minecraft.server.v1_8_R3.Slot;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryDoubleChest;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryView;
import org.bukkit.inventory.Inventory;

public class SilentContainerChest extends Container {
   public IInventory container;
   private int f;
   private CraftInventoryView bukkitEntity = null;
   private PlayerInventory player;

   public CraftInventoryView getBukkitView() {
      if (this.bukkitEntity != null) {
         return this.bukkitEntity;
      } else {
         Object inventory = this.container instanceof PlayerInventory ? new CraftInventoryPlayer((PlayerInventory)this.container) : (this.container instanceof InventoryLargeChest ? new CraftInventoryDoubleChest((InventoryLargeChest)this.container) : new CraftInventory(this.container));
         this.bukkitEntity = new CraftInventoryView(this.player.player.getBukkitEntity(), (Inventory)inventory, this);
         return this.bukkitEntity;
      }
   }

   public SilentContainerChest(IInventory firstInv, IInventory secondInv) {
      this.container = secondInv;
      this.f = secondInv.getSize() / 9;
      int i = (this.f - 4) * 18;
      this.player = (PlayerInventory)firstInv;

      int k;
      int j;
      for(j = 0; j < this.f; ++j) {
         for(k = 0; k < 9; ++k) {
            this.a(new Slot(secondInv, k + j * 9, 8 + k * 18, 18 + j * 18));
         }
      }

      for(j = 0; j < 3; ++j) {
         for(k = 0; k < 9; ++k) {
            this.a(new Slot(firstInv, k + j * 9 + 9, 8 + k * 18, 103 + j * 18 + i));
         }
      }

      for(j = 0; j < 9; ++j) {
         this.a(new Slot(firstInv, j, 8 + j * 18, 161 + i));
      }

   }

   public boolean a(EntityHuman entityhuman) {
      return !this.checkReachable || this.container.a(entityhuman);
   }

   public ItemStack b(EntityHuman entityhuman, int i) {
      ItemStack itemstack = null;
      Slot slot = (Slot)this.c.get(i);
      if (slot != null && slot.hasItem()) {
         ItemStack item = slot.getItem();
         itemstack = item.cloneItemStack();
         if (i < this.f * 9) {
            if (!this.a(item, this.f * 9, this.c.size(), true)) {
               return null;
            }
         } else if (!this.a(item, 0, this.f * 9, false)) {
            return null;
         }

         if (item.count == 0) {
            slot.set((ItemStack)null);
         } else {
            slot.f();
         }
      }

      return itemstack;
   }

   public void b(EntityHuman entityhuman) {
      super.b(entityhuman);
   }
}
