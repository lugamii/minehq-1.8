package dev.lugami.potpvp.kit;

import dev.lugami.potpvp.util.C;
import dev.lugami.qlib.util.ItemUtils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import lombok.experimental.UtilityClass;

import static dev.lugami.potpvp.PotPvPLang.LEFT_ARROW;
import static dev.lugami.potpvp.PotPvPLang.RIGHT_ARROW;
import static org.bukkit.ChatColor.*;

@UtilityClass
public final class KitItems {

    public static final ItemStack OPEN_EDITOR_ITEM = new ItemStack(Material.BOOK);

    static {
        ItemUtils.setDisplayName(OPEN_EDITOR_ITEM, C.C("&eKit Editor"));
    }

}