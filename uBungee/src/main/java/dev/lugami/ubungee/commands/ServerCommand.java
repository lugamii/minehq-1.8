package dev.lugami.ubungee.commands;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import java.util.Collections;
import java.util.Map;

import dev.lugami.ubungee.uBungee;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import dev.lugami.bridge.global.GlobalAPI;
import dev.lugami.bridge.global.ranks.Rank;

public class ServerCommand extends Command implements TabExecutor {

    public ServerCommand() {
        super( "server");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if(commandSender instanceof ProxiedPlayer){
            ProxiedPlayer p = (ProxiedPlayer) commandSender;
            Rank r = GlobalAPI.getPlayerRank(p.getUniqueId(), true);
            if(r.isStaff()){
                Map<String, ServerInfo> servers = ProxyServer.getInstance().getServers();
                if ( args.length == 0 )
                {
                    p.sendMessage( new TextComponent(ChatColor.GOLD + "You are currently connected to " + ChatColor.WHITE + p.getServer().getInfo().getName() + ChatColor.GOLD + "."));
                    TextComponent serverList = new TextComponent(ChatColor.GOLD + "Servers: ");
                    boolean first = true;
                    for ( ServerInfo server : servers.values() )
                    {
                        TextComponent serverTextComponent = new TextComponent( first ? server.getName() : "§7,§f " + server.getName() );
                        int count = server.getPlayers().size();
                        serverTextComponent.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT,
                                new ComponentBuilder( count + ( count == 1 ? " player" : " players" ) + "\n" )
                                        .append( "Click to connect to the server" ).italic( true )
                                        .create() ) );
                        serverTextComponent.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/server " + server.getName() ) );
                        serverTextComponent.setColor(ChatColor.WHITE);
                        serverList.addExtra( serverTextComponent );
                        first = false;
                    }
                    p.sendMessage(serverList);
                    p.sendMessage("§6Connect to a server with §e/server <name>");
                } else if (args.length == 1){
                    ServerInfo si = uBungee.getInstance().getProxy().getServerInfo(args[0]);
                    if(si != null){
                        p.sendMessage("§6Sending you to §f" + si.getName() + "§6...");
                        p.connect(si);
                    } else {
                        p.sendMessage("§cNo server by the name \"" + args[0] + "\"§c found.");
                    }
                }
            } else {
                p.sendMessage("§cNo permission.");
            }
        }
    }

    @Override
    public Iterable<String> onTabComplete(final CommandSender sender, final String[] args)
    {
        return ( args.length > 1 ) ? Collections.EMPTY_LIST : Iterables.transform( Iterables.filter( ProxyServer.getInstance().getServers().values(), new Predicate<ServerInfo>()
        {
            private final String lower = ( args.length == 0 ) ? "" : args[0].toLowerCase();

            @Override
            public boolean apply(ServerInfo input)
            {
                return input.getName().toLowerCase().startsWith(lower);
            }
        } ), new Function<ServerInfo, String>()
        {
            @Override
            public String apply(ServerInfo input)
            {
                return input.getName();
            }
        } );
    }
}