package dev.lugami.potpvp.kit.menu.kits;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import dev.lugami.potpvp.PotPvPSI;
import dev.lugami.potpvp.kit.Kit;
import dev.lugami.potpvp.kit.KitHandler;
import dev.lugami.potpvp.kit.menu.editkit.EditKitMenu;
import dev.lugami.potpvp.kittype.KitType;
import dev.lugami.qlib.menu.Button;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;
import java.util.Optional;

final class KitIconButton extends Button {

    private final Optional<Kit> kitOpt;
    private final KitType kitType;
    private final int slot;

    KitIconButton(Optional<Kit> kitOpt, KitType kitType, int slot) {
        this.kitOpt = Preconditions.checkNotNull(kitOpt, "kitOpt");
        this.kitType = Preconditions.checkNotNull(kitType, "kitType");
        this.slot = slot;
    }

    @Override
    public String getName(Player player) {
        return ChatColor.GREEN.toString() + ChatColor.BOLD + (kitOpt.map(Kit::getName).orElse("Create Kit"));
    }

    @Override
    public List<String> getDescription(Player player) {
        return kitOpt.map(kit -> ImmutableList.of(
            "",
            ChatColor.GREEN + "Heals: " + ChatColor.WHITE + kit.countHeals(),
            ChatColor.RED + "Debuffs: " + ChatColor.WHITE + kit.countDebuffs()
        )).orElse(ImmutableList.of());
    }

    @Override
    public Material getMaterial(Player player) {
        return kitOpt.isPresent() ? Material.DIAMOND_SWORD : Material.STONE_SWORD;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        Kit resolvedKit = kitOpt.orElseGet(() -> {
            KitHandler kitHandler = PotPvPSI.getInstance().getKitHandler();
            return kitHandler.saveDefaultKit(player, kitType, this.slot);
        });

        new EditKitMenu(resolvedKit).openMenu(player);
    }

}