package dev.lugami.qlib.tab;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import dev.lugami.qlib.qLib;
import dev.lugami.qlib.tab.data.TabList;
import dev.lugami.qlib.util.ChatUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author ImHacking
 * @date 4/10/2022
 */
public class TabRunnable extends BukkitRunnable {

    private Table<Integer, Integer, String> oldValues = HashBasedTable.create();

    @Override
    public void run() {

        TabList tabList = qLib.getInstance().getTabHandler().getTabList();

        if (tabList != null) {
            for (Player player : tabList.getPlugin().getServer().getOnlinePlayers()) {
                String header = tabList.getProvider().getHeader(player);
                String footer = tabList.getProvider().getFooter(player);
                sendHeaderFooter(player, header, footer);
                Table<Integer, Integer, String> table = tabList.getProvider().provide(player);

                for (Table.Cell<Integer, Integer, String> cell : table.cellSet()) {
                    tabList.updateSlot(player, cell.getRowKey(), cell.getColumnKey(), cell.getValue());
                }

                for (Table.Cell<Integer, Integer, String> cell : oldValues.cellSet()) {
                    if (table.get(cell.getRowKey(), cell.getColumnKey()) == null || table.get(cell.getRowKey(), cell.getColumnKey()).isEmpty()) {
                        tabList.updateSlot(player, cell.getRowKey(), cell.getColumnKey(), "");
                    }
                }

                oldValues = table;

            }
        }



    }
    private void sendHeaderFooter(Player player, String header, String footer) {
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        packet.header = new BaseComponent[1];
        if(header != null)
            packet.header[0] = ChatUtils.colorizeTextComponent(header);
        packet.footer = new BaseComponent[1];
        if(footer != null)
            packet.footer[0] = ChatUtils.colorizeTextComponent(footer);
        if (header != null && footer != null) {
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
        }
    }

}