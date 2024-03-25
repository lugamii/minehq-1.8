package dev.lugami.uhub.selector.server;

import com.google.common.collect.ImmutableList;
import dev.lugami.uhub.util.Style;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import dev.lugami.bridge.BridgeGlobal;
import dev.lugami.bridge.bukkit.commands.punishment.menu.MainPunishmentMenu;
import dev.lugami.bridge.bukkit.commands.punishment.menu.PunishmentMenu;
import dev.lugami.bridge.bukkit.commands.rank.menu.PermissionsMenu;
import dev.lugami.bridge.bukkit.commands.rank.menu.PlayerMenu;
import dev.lugami.bridge.global.ranks.Rank;
import dev.lugami.bridge.global.status.BridgeServer;
import dev.lugami.qlib.menu.Button;
import dev.lugami.qlib.menu.Menu;
import dev.lugami.qlib.menu.buttons.DisplayButton;
import dev.lugami.qlib.util.BungeeUtils;

import java.util.*;

public class UHCSelectorMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return Style.DARK_GRAY + "Select a server to join";
    }

    public UHCSelectorMenu(BridgeServer server) {
        server = server;
        setAutoUpdate(true);
        setPlaceholder(true);

    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttonMap = new HashMap<>();

        buttonMap.put(0, new Button() {
            @Override
            public String getName(Player player) {
                return (BridgeGlobal.getServerHandler().getServer().isOnline() ? ChatColor.GREEN + ChatColor.BOLD.toString() : ChatColor.RED + ChatColor.BOLD.toString()) + "UHC-1";
            }

            @Override
            public List<String> getDescription(Player player) {
                List<String> description = new ArrayList<>();
                description.add("");
                description.add(ChatColor.GRAY + "* " + ChatColor.GOLD + "Scenario Based UHC!");
                description.add(ChatColor.GRAY + "* " + ChatColor.GOLD + "Hardcore Healing!");
                description.add(ChatColor.GRAY + "* " + ChatColor.GOLD + "FFA & Teams Games!");
                description.add("");
                description.add((BridgeGlobal.getServerHandler().getServer().isOnline() ? ChatColor.GRAY + "» " + ChatColor.YELLOW + "Click to join!" + ChatColor.GRAY + " «" : ChatColor.YELLOW + "This server is offline"));

                return description;
            }

            @Override
            public Material getMaterial(Player player) {
                return (BridgeGlobal.getServerHandler().getServer().isOnline() ? Material.GOLDEN_APPLE : Material.REDSTONE_BLOCK);
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType) {
                if (BridgeGlobal.getServerHandler().getServer() == null) {
                    return;
                }

                player.closeInventory();
                BungeeUtils.send(player, getName(player));
            }
        });

        buttonMap.put(1, new Button() {
            @Override
            public String getName(Player player) {
                return (BridgeGlobal.getServerHandler().getServer().isOnline() ? ChatColor.GREEN + ChatColor.BOLD.toString() : ChatColor.RED + ChatColor.BOLD.toString()) + "UHC-2";
            }

            @Override
            public List<String> getDescription(Player player) {
                List<String> description = new ArrayList<>();
                description.add("");
                description.add(ChatColor.GRAY + "* " + ChatColor.GOLD + "Scenario Based UHC!");
                description.add(ChatColor.GRAY + "* " + ChatColor.GOLD + "Hardcore Healing!");
                description.add(ChatColor.GRAY + "* " + ChatColor.GOLD + "FFA & Teams Games!");
                description.add("");
                description.add((BridgeGlobal.getServerHandler().getServer().isOnline() ? ChatColor.GRAY + "» " + ChatColor.YELLOW + "Click to join!" + ChatColor.GRAY + " «" : ChatColor.YELLOW + "This server is offline"));

                return description;
            }

            @Override
            public Material getMaterial(Player player) {
                return (BridgeGlobal.getServerHandler().getServer().isOnline() ? Material.GOLDEN_APPLE : Material.REDSTONE_BLOCK);
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType) {
                if (BridgeGlobal.getServerHandler().getServer() == null) {
                    return;
                }

                player.closeInventory();
                BungeeUtils.send(player, getName(player));
            }
        });

        buttonMap.put(2, new Button() {
            @Override
            public String getName(Player player) {
                return (BridgeGlobal.getServerHandler().getServer().isOnline() ? ChatColor.GREEN + ChatColor.BOLD.toString() : ChatColor.RED + ChatColor.BOLD.toString()) + "UHC-3";
            }

            @Override
            public List<String> getDescription(Player player) {
                List<String> description = new ArrayList<>();
                description.add("");
                description.add(ChatColor.GRAY + "* " + ChatColor.GOLD + "Scenario Based UHC!");
                description.add(ChatColor.GRAY + "* " + ChatColor.GOLD + "Hardcore Healing!");
                description.add(ChatColor.GRAY + "* " + ChatColor.GOLD + "FFA & Teams Games!");
                description.add("");
                description.add((BridgeGlobal.getServerHandler().getServer().isOnline() ? ChatColor.GRAY + "» " + ChatColor.YELLOW + "Click to join!" + ChatColor.GRAY + " «" : ChatColor.YELLOW + "This server is offline"));

                return description;
            }

            @Override
            public Material getMaterial(Player player) {
                return (BridgeGlobal.getServerHandler().getServer().isOnline() ? Material.GOLDEN_APPLE : Material.REDSTONE_BLOCK);
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType) {
                if (BridgeGlobal.getServerHandler().getServer() == null) {
                    return;
                }

                player.closeInventory();
                BungeeUtils.send(player, getName(player));
            }
        });

        buttonMap.put(3, new Button() {
            @Override
            public String getName(Player player) {
                return (BridgeGlobal.getServerHandler().getServer().isOnline() ? ChatColor.GREEN + ChatColor.BOLD.toString() : ChatColor.RED + ChatColor.BOLD.toString()) + "UHC-4";
            }

            @Override
            public List<String> getDescription(Player player) {
                List<String> description = new ArrayList<>();
                description.add("");
                description.add(ChatColor.GRAY + "* " + ChatColor.GOLD + "Scenario Based UHC!");
                description.add(ChatColor.GRAY + "* " + ChatColor.GOLD + "Hardcore Healing!");
                description.add(ChatColor.GRAY + "* " + ChatColor.GOLD + "FFA & Teams Games!");
                description.add("");
                description.add((BridgeGlobal.getServerHandler().getServer().isOnline() ? ChatColor.GRAY + "» " + ChatColor.YELLOW + "Click to join!" + ChatColor.GRAY + " «" : ChatColor.YELLOW + "This server is offline"));

                return description;
            }

            @Override
            public Material getMaterial(Player player) {
                return (BridgeGlobal.getServerHandler().getServer().isOnline() ? Material.GOLDEN_APPLE : Material.REDSTONE_BLOCK);
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType) {
                if (BridgeGlobal.getServerHandler().getServer() == null) {
                    return;
                }

                player.closeInventory();
                BungeeUtils.send(player, getName(player));
            }
        });

        buttonMap.put(4, new Button() {
            @Override
            public String getName(Player player) {
                return (BridgeGlobal.getServerHandler().getServer().isOnline() ? ChatColor.GREEN + ChatColor.BOLD.toString() : ChatColor.RED + ChatColor.BOLD.toString()) + "UHC-5";
            }

            @Override
            public List<String> getDescription(Player player) {
                List<String> description = new ArrayList<>();
                description.add("");
                description.add(ChatColor.GRAY + "* " + ChatColor.GOLD + "Scenario Based UHC!");
                description.add(ChatColor.GRAY + "* " + ChatColor.GOLD + "Hardcore Healing!");
                description.add(ChatColor.GRAY + "* " + ChatColor.GOLD + "FFA & Teams Games!");
                description.add("");
                description.add((BridgeGlobal.getServerHandler().getServer().isOnline() ? ChatColor.GRAY + "» " + ChatColor.YELLOW + "Click to join!" + ChatColor.GRAY + " «" : ChatColor.YELLOW + "This server is offline"));

                return description;
            }

            @Override
            public Material getMaterial(Player player) {
                return (BridgeGlobal.getServerHandler().getServer().isOnline() ? Material.GOLDEN_APPLE : Material.REDSTONE_BLOCK);
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType) {
                if (BridgeGlobal.getServerHandler().getServer() == null) {
                    return;
                }

                player.closeInventory();
                BungeeUtils.send(player, getName(player));
            }
        });

        buttonMap.put(5, new Button() {
            @Override
            public String getName(Player player) {
                return (BridgeGlobal.getServerHandler().getServer().isOnline() ? ChatColor.GREEN + ChatColor.BOLD.toString() : ChatColor.RED + ChatColor.BOLD.toString()) + "UHC-6";
            }

            @Override
            public List<String> getDescription(Player player) {
                List<String> description = new ArrayList<>();
                description.add("");
                description.add(ChatColor.GRAY + "* " + ChatColor.GOLD + "Scenario Based UHC!");
                description.add(ChatColor.GRAY + "* " + ChatColor.GOLD + "Hardcore Healing!");
                description.add(ChatColor.GRAY + "* " + ChatColor.GOLD + "FFA & Teams Games!");
                description.add("");
                description.add((BridgeGlobal.getServerHandler().getServer().isOnline() ? ChatColor.GRAY + "» " + ChatColor.YELLOW + "Click to join!" + ChatColor.GRAY + " «" : ChatColor.YELLOW + "This server is offline"));

                return description;
            }

            @Override
            public Material getMaterial(Player player) {
                return (BridgeGlobal.getServerHandler().getServer().isOnline() ? Material.GOLDEN_APPLE : Material.REDSTONE_BLOCK);
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType) {
                if (BridgeGlobal.getServerHandler().getServer() == null) {
                    return;
                }

                player.closeInventory();
                BungeeUtils.send(player, getName(player));
            }
        });

        buttonMap.put(6, new Button() {
            @Override
            public String getName(Player player) {
                return (BridgeGlobal.getServerHandler().getServer().isOnline() ? ChatColor.GREEN + ChatColor.BOLD.toString() : ChatColor.RED + ChatColor.BOLD.toString()) + "UHC-7";
            }

            @Override
            public List<String> getDescription(Player player) {
                List<String> description = new ArrayList<>();
                description.add("");
                description.add(ChatColor.GRAY + "* " + ChatColor.GOLD + "Scenario Based UHC!");
                description.add(ChatColor.GRAY + "* " + ChatColor.GOLD + "Hardcore Healing!");
                description.add(ChatColor.GRAY + "* " + ChatColor.GOLD + "FFA & Teams Games!");
                description.add("");
                description.add((BridgeGlobal.getServerHandler().getServer().isOnline() ? ChatColor.GRAY + "» " + ChatColor.YELLOW + "Click to join!" + ChatColor.GRAY + " «" : ChatColor.YELLOW + "This server is offline"));

                return description;
            }

            @Override
            public Material getMaterial(Player player) {
                return (BridgeGlobal.getServerHandler().getServer().isOnline() ? Material.GOLDEN_APPLE : Material.REDSTONE_BLOCK);
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType) {
                if (BridgeGlobal.getServerHandler().getServer() == null) {
                    return;
                }

                player.closeInventory();
                BungeeUtils.send(player, getName(player));
            }
        });

        buttonMap.put(7, new Button() {
            @Override
            public String getName(Player player) {
                return (BridgeGlobal.getServerHandler().getServer().isOnline() ? ChatColor.GREEN + ChatColor.BOLD.toString() : ChatColor.RED + ChatColor.BOLD.toString()) + "UHC-8";
            }

            @Override
            public List<String> getDescription(Player player) {
                List<String> description = new ArrayList<>();
                description.add("");
                description.add(ChatColor.GRAY + "* " + ChatColor.GOLD + "Scenario Based UHC!");
                description.add(ChatColor.GRAY + "* " + ChatColor.GOLD + "Hardcore Healing!");
                description.add(ChatColor.GRAY + "* " + ChatColor.GOLD + "FFA & Teams Games!");
                description.add("");
                description.add((BridgeGlobal.getServerHandler().getServer().isOnline() ? ChatColor.GRAY + "» " + ChatColor.YELLOW + "Click to join!" + ChatColor.GRAY + " «" : ChatColor.YELLOW + "This server is offline"));

                return description;
            }

            @Override
            public Material getMaterial(Player player) {
                return (BridgeGlobal.getServerHandler().getServer().isOnline() ? Material.GOLDEN_APPLE : Material.REDSTONE_BLOCK);
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType) {
                if (BridgeGlobal.getServerHandler().getServer() == null) {
                    return;
                }

                player.closeInventory();
                BungeeUtils.send(player, getName(player));
            }
        });

        buttonMap.put(8, new Button() {
            @Override
            public String getName(Player player) {
                return (BridgeGlobal.getServerHandler().getServer().isOnline() ? ChatColor.GREEN + ChatColor.BOLD.toString() : ChatColor.RED + ChatColor.BOLD.toString()) + "UHC-9";
            }

            @Override
            public List<String> getDescription(Player player) {
                List<String> description = new ArrayList<>();
                description.add("");
                description.add(ChatColor.GRAY + "* " + ChatColor.GOLD + "Scenario Based UHC!");
                description.add(ChatColor.GRAY + "* " + ChatColor.GOLD + "Hardcore Healing!");
                description.add(ChatColor.GRAY + "* " + ChatColor.GOLD + "FFA & Teams Games!");
                description.add("");
                description.add((BridgeGlobal.getServerHandler().getServer().isOnline() ? ChatColor.GRAY + "» " + ChatColor.YELLOW + "Click to join!" + ChatColor.GRAY + " «" : ChatColor.YELLOW + "This server is offline"));

                return description;
            }

            @Override
            public Material getMaterial(Player player) {
                return (BridgeGlobal.getServerHandler().getServer().isOnline() ? Material.GOLDEN_APPLE : Material.REDSTONE_BLOCK);
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType) {
                if (BridgeGlobal.getServerHandler().getServer() == null) {
                    return;
                }

                player.closeInventory();
                BungeeUtils.send(player, getName(player));
            }
        });

        buttonMap.put(17, new Button() {
            @Override
            public String getName(Player player) {
                return ChatColor.RED.toString() + ChatColor.BOLD + "Back";
            }

            @Override
            public List<String> getDescription(Player player) {
                return ImmutableList.of(
                        "",
                        ChatColor.RED + "Click here to return to",
                        ChatColor.RED + "the previous menu."
                );
            }

            @Override
            public Material getMaterial(Player player) {
                return Material.REDSTONE;
            }

            public byte getDamageValue(Player player) {
                return 0;
            }

            public void clicked(Player player, int i, ClickType clickType) {
                player.closeInventory();
                new ServerSelectorMenu().openMenu(player);
            }
        });

        return buttonMap;
    }
}

