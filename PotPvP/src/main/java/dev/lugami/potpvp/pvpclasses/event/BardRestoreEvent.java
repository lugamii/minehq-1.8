package dev.lugami.potpvp.pvpclasses.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import dev.lugami.potpvp.pvpclasses.PvPClass;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@AllArgsConstructor
public class BardRestoreEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Player player;
    private PvPClass.SavedPotion potions;

    public HandlerList getHandlers() {
        return (handlers);
    }

    public static HandlerList getHandlerList() {
        return (handlers);
    }

}