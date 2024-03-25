package dev.lugami.uhub.util;

import com.google.common.base.Preconditions;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemBuilders {
    private ItemStack stack;
    private ItemMeta meta;

    public ItemBuilders(Material material) {
        this(material, 1);
    }

    public ItemBuilders(Material material, int amount) {
        this(material, amount, (byte)0);
    }

    public ItemBuilders(ItemStack stack) {
        Preconditions.checkNotNull(stack, "ItemStack cannot be null");
        this.stack = stack;
    }

    public ItemBuilders(Material material, int amount, byte data) {
        Preconditions.checkNotNull(material, "Material cannot be null");
        Preconditions.checkArgument(amount > 0, "Amount must be positive");
        this.stack = new ItemStack(material, amount, (short)data);
    }

    public ItemBuilders(Material material, int amount, int data) {
        Preconditions.checkNotNull(material, "Material cannot be null");
        Preconditions.checkArgument(amount > 0, "Amount must be positive");
        this.stack = new ItemStack(material, amount, (short)data);
    }

    public ItemBuilders displayName(String name) {
        if (this.meta == null) {
            this.meta = this.stack.getItemMeta();
        }

        this.meta.setDisplayName(name);
        return this;
    }

    public ItemBuilders loreLine(String line) {
        if (this.meta == null) {
            this.meta = this.stack.getItemMeta();
        }

        boolean hasLore;
        List lore = (hasLore = this.meta.hasLore()) ? this.meta.getLore() : new ArrayList();
        (lore).add(hasLore ? ((List)lore).size() : 0, line);
        this.lore(line);
        return this;
    }

    public ItemBuilders lore(String... lore) {
        if (this.meta == null) {
            this.meta = this.stack.getItemMeta();
        }

        this.meta.setLore(Arrays.asList(lore));
        return this;
    }

    public ItemBuilders lore(List<String> lore) {
        if (this.meta == null) {
            this.meta = this.stack.getItemMeta();
        }

        this.meta.setLore(lore);
        return this;
    }

    public ItemBuilders enchant(Enchantment enchantment, int level) {
        return this.enchant(enchantment, level, true);
    }

    public ItemBuilders enchant(Enchantment enchantment, int level, boolean unsafe) {
        if (unsafe && level >= enchantment.getMaxLevel()) {
            this.stack.addUnsafeEnchantment(enchantment, level);
        } else {
            this.stack.addEnchantment(enchantment, level);
        }

        return this;
    }

    public ItemBuilders data(short data) {
        this.stack.setDurability(data);
        return this;
    }

    public ItemStack build() {
        if (this.meta != null) {
            this.stack.setItemMeta(this.meta);
        }

        return this.stack;
    }
}
