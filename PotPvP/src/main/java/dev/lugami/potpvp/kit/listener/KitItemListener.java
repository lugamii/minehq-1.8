package dev.lugami.potpvp.kit.listener;

import dev.lugami.potpvp.PotPvPSI;
import dev.lugami.potpvp.kit.KitItems;
import dev.lugami.potpvp.kit.menu.kits.KitsMenu;
import dev.lugami.potpvp.kittype.menu.select.SelectKitTypeMenu;
import dev.lugami.potpvp.lobby.LobbyHandler;
import dev.lugami.potpvp.util.ItemListener;

public final class KitItemListener extends ItemListener {

    public KitItemListener() {
        addHandler(KitItems.OPEN_EDITOR_ITEM, player -> {
            LobbyHandler lobbyHandler = PotPvPSI.getInstance().getLobbyHandler();

            if (lobbyHandler.isInLobby(player)) {
                new SelectKitTypeMenu(kitType -> {
                    new KitsMenu(kitType).openMenu(player);
                }, "Select a kit to edit...").openMenu(player);
            }
        });
    }

}