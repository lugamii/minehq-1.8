package dev.lugami.potpvp.rematch.listener;

import dev.lugami.potpvp.PotPvPSI;
import dev.lugami.potpvp.match.event.MatchTerminateEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class RematchGeneralListener implements Listener {

    @EventHandler
    public void onMatchTerminate(MatchTerminateEvent event) {
        PotPvPSI.getInstance().getRematchHandler().registerRematches(event.getMatch());
    }

}