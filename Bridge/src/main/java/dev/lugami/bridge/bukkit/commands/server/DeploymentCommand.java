package dev.lugami.bridge.bukkit.commands.server;

import org.bukkit.entity.Player;
import dev.lugami.bridge.bukkit.commands.server.menu.deployment.DeploymentMenu;
import dev.lugami.qlib.command.Command;
import dev.lugami.qlib.command.Param;

public class DeploymentCommand {

    @Command(names = {"deploy", "deployment"}, permission = "bridge.deployment", description = "Deploy a server", hidden = true)
    public static void deployCmd(Player s, @Param(name = "serverName") String serverName) {
        new DeploymentMenu(serverName).openMenu(s);
    }
}
