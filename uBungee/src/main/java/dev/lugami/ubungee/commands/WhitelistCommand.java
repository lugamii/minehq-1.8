package dev.lugami.ubungee.commands;

import dev.lugami.ubungee.uBungee;
import dev.lugami.ubungee.whitelist.WhitelistUUID;
import org.apache.commons.lang3.StringUtils;
import lombok.Getter;
import dev.lugami.bridge.global.GlobalAPI;
import dev.lugami.bridge.global.profile.Profile;
import dev.lugami.bridge.global.ranks.Rank;
import dev.lugami.bridge.global.util.MojangUtils;
import dev.lugami.ubungee.whitelist.WhitelistRank;
import dev.lugami.ubungee.whitelist.WhitelistType;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WhitelistCommand extends Command {

    @Getter
    private static final List<UUID> uuidList = new ArrayList<>();

    public WhitelistCommand() {
        super("whitelist");
    }

    @Override
    public void execute(CommandSender s, String[] args) {
        if (s instanceof ProxiedPlayer) {
            Profile profile = GlobalAPI.getProfile(((ProxiedPlayer) s).getUniqueId());
            if (!uBungee.getInstance().hasPermission(profile, "ubungee.whitelist")) {
                s.sendMessage(ChatColor.RED + "No permission.");
                return;
            }
        }
        if (args.length < 1) {
            s.sendMessage(ChatColor.BLUE.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 35));
            s.sendMessage(ChatColor.RED + "/whitelist toggle");
            s.sendMessage(ChatColor.RED + "/whitelist type <type>");
            s.sendMessage(ChatColor.RED + "/whitelist add <rank>");
            s.sendMessage(ChatColor.RED + "/whitelist add <name>");
            s.sendMessage(ChatColor.RED + "/whitelist remove <name>");
            s.sendMessage(ChatColor.RED + "/whitelist about");
            s.sendMessage(ChatColor.RED + "/whitelist on");
            s.sendMessage(ChatColor.RED + "/whitelist off");
            s.sendMessage(ChatColor.BLUE.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 35));
            return;
        }
        try {
            switch (args[0].toLowerCase()) {
                case "toggle": {
                    boolean b = uuidList.contains(((ProxiedPlayer) s).getUniqueId());
                    if (b)
                        uuidList.remove(((ProxiedPlayer) s).getUniqueId());
                    else
                        uuidList.add(((ProxiedPlayer) s).getUniqueId());

                    s.sendMessage(!b ? ChatColor.GREEN + "You will no longer see whitelist join messages" : ChatColor.RED + "You can now see whitelist join messages");
                    return;
                }
                case "type": {
                    if(args.length < 2) {
                        s.sendMessage(ChatColor.RED + "Usage: /whitelist type <type>");
                        return;
                    }
                    WhitelistType whitelistType = WhitelistType.getType(args[1]);
                    if (whitelistType == null) {
                        s.sendMessage(ChatColor.RED + "The whitelist type \"" + args[1] + "\" does not exist.");
                        return;
                    }
                    uBungee.getInstance().setWhitelist(whitelistType);
                    s.sendMessage(ChatColor.GREEN + "Set whitelist type to \"" + whitelistType.name() + "\".");
                    return;
                }
                case "add": {
                    switch (uBungee.getInstance().getWhitelist().name()) {
                        case "rank": {
                            if(args.length < 2) {
                                s.sendMessage(ChatColor.RED + "Usage: /whitelist add <rank>");
                                return;
                            }
                            Rank rank = GlobalAPI.getRank(args[1]);
                            if (rank == null) {
                                s.sendMessage(ChatColor.RED + "The rank \"" + args[1] + "\" does not exist.");
                                return;
                            }
                            uBungee.getInstance().setWhitelist(new WhitelistRank(rank));
                            s.sendMessage(ChatColor.GREEN + "The rank " + rank.getColor() + rank.getDisplayName() + ChatColor.GREEN + " is now the lowest required rank to join the server.");

                            return;
                        }

                        case "uuid": {
                            if(args.length < 2) {
                                s.sendMessage(ChatColor.RED + "Usage: /whitelist add <name>");
                                return;
                            }
                            try {
                                String uuid = MojangUtils.fetchUUID(args[1]).toString();
                                List<String> uuidList = uBungee.getInstance().getConfig().getStringList("whitelist.uuids");
                                uuidList.add(uuid);
                                uBungee.getInstance().getConfig().set("whitelist.uuids", uuidList);
                                s.sendMessage(ChatColor.GREEN + "Added " + ChatColor.WHITE + args[1] + ChatColor.GREEN + " to the whitelist!");
                            } catch (Exception e) {
                                s.sendMessage(ChatColor.RED + "The player \"" + args[1] + "\" does not exist.");
                            }
                            return;
                        }

                        default: {
                            s.sendMessage(ChatColor.RED + "Usage: /whitelist add <name>");
                            break;
                        }

                    }
                    return;
                }

                case "remove": {
                    if(args.length < 2) {
                        s.sendMessage(ChatColor.RED + "Usage: /whitelist remove <name>");
                        return;
                    }
                    try {
                        String uuid = MojangUtils.fetchUUID(args[1]).toString();
                        List<String> uuidList = uBungee.getInstance().getConfig().getStringList("whitelist.uuids");
                        if (uuidList.contains(uuid)) {
                            uuidList.remove(uuid);
                            uBungee.getInstance().getConfig().set("whitelist.uuids", uuidList);
                            s.sendMessage(ChatColor.RED + "Removed " + ChatColor.WHITE + args[1] + ChatColor.RED + " from the whitelist!");
                        } else {
                            s.sendMessage(ChatColor.RED + "The player \"" + args[1] + "\" is not whitelisted.");
                        }
                    } catch (Exception e) {
                        s.sendMessage(ChatColor.RED + "The player \"" + args[1] + "\" does not exist.");
                    }
                    return;
                }
                case "about": {
                    s.sendMessage("§7§m-----------------------------------------------------");
                    s.sendMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "Whitelist Status:");
                    s.sendMessage("§7§m-----------------------------------------------------");
                    WhitelistType wt = WhitelistType.getType(uBungee.getInstance().getConfig().getString("whitelist.type"));
                    s.sendMessage(ChatColor.GOLD + "Enabled: " + ChatColor.WHITE + (wt != null));
                    if (wt != null) {
                        s.sendMessage(ChatColor.GOLD + "Type: " + ChatColor.WHITE + wt.name());
                        if (wt instanceof WhitelistRank) {
                            s.sendMessage(ChatColor.GOLD + "  - " + ChatColor.WHITE + ((WhitelistRank) wt).getMinRank().getColor() + ((WhitelistRank) wt).getMinRank().getDisplayName());
                        }
                        if (wt instanceof WhitelistUUID) {
                            WhitelistUUID wlu = (WhitelistUUID) wt;
                            new Thread(() -> {
                                wlu.getUuidList().forEach(u -> {
                                    try {
                                        s.sendMessage(ChatColor.GOLD + "  - " + ChatColor.WHITE + MojangUtils.fetchName(u));
                                    } catch (Exception e) {
                                        s.sendMessage(ChatColor.GOLD + "  - " + ChatColor.WHITE + "null");
                                    }
                                });
                            }).start();

                        }
                    }
                    return;
                }
                case "on": {
                    uBungee.getInstance().setWhitelist(WhitelistType.getType("uuid"));
                    s.sendMessage(ChatColor.RED + "The network is now whitelisted.");
                    return;
                }
                case "off": {
                    uBungee.getInstance().setWhitelist(null);
                    s.sendMessage(ChatColor.GREEN + "The network is now unwhitelisted.");
                    return;
                }
                default: {
                    s.sendMessage(ChatColor.BLUE.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 35));
                    s.sendMessage(ChatColor.RED + "/whitelist toggle");
                    s.sendMessage(ChatColor.RED + "/whitelist type <type>");
                    s.sendMessage(ChatColor.RED + "/whitelist add <rank>");
                    s.sendMessage(ChatColor.RED + "/whitelist add <name>");
                    s.sendMessage(ChatColor.RED + "/whitelist remove <name>");
                    s.sendMessage(ChatColor.RED + "/whitelist about");
                    s.sendMessage(ChatColor.RED + "/whitelist on");
                    s.sendMessage(ChatColor.RED + "/whitelist off");
                    s.sendMessage(ChatColor.BLUE.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 35));
                    return;
                }
            }
                } catch(Exception e){
            s.sendMessage(ChatColor.BLUE.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 35));
            s.sendMessage(ChatColor.RED + "/whitelist toggle");
            s.sendMessage(ChatColor.RED + "/whitelist type <type>");
            s.sendMessage(ChatColor.RED + "/whitelist add <rank>");
            s.sendMessage(ChatColor.RED + "/whitelist add <name>");
            s.sendMessage(ChatColor.RED + "/whitelist remove <name>");
            s.sendMessage(ChatColor.RED + "/whitelist about");
            s.sendMessage(ChatColor.RED + "/whitelist on");
            s.sendMessage(ChatColor.RED + "/whitelist off");
            s.sendMessage(ChatColor.BLUE.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 35));
                }
            }

        }