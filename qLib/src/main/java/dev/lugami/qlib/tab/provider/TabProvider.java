package dev.lugami.qlib.tab.provider;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.mojang.authlib.properties.Property;
import org.bukkit.entity.Player;

import java.util.HashMap;

public interface TabProvider {
    // row, column, string
    default Table<Integer, Integer, String> provide(Player player) {
        return null;
    }
    default String getHeader(Player player) {
        return null;
    }

    default String getFooter(Player player) {
        return null;
    }

    default Table<Integer, Integer, Property> getHeads(Player player) {
        return HashBasedTable.create();
    }
}
