package dev.lugami.potpvp.arena.menu.manageschematics;

import dev.lugami.potpvp.PotPvPSI;
import dev.lugami.potpvp.arena.ArenaHandler;
import dev.lugami.potpvp.arena.ArenaSchematic;
import dev.lugami.potpvp.command.ManageCommand;
import dev.lugami.potpvp.util.menu.MenuBackButton;
import dev.lugami.qlib.menu.Button;
import dev.lugami.qlib.menu.Menu;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public final class ManageSchematicsMenu extends Menu {

    public ManageSchematicsMenu() {
        super("Manage schematics");
        setAutoUpdate(true);
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        ArenaHandler arenaHandler = PotPvPSI.getInstance().getArenaHandler();
        Map<Integer, Button> buttons = new HashMap<>();
        int index = 0;

        buttons.put(index++, new MenuBackButton(p -> new ManageCommand.ManageMenu().openMenu(p)));

        for (ArenaSchematic schematic : arenaHandler.getSchematics()) {
            buttons.put(index++, new ManageSchematicButton(schematic));
        }

        return buttons;
    }

}