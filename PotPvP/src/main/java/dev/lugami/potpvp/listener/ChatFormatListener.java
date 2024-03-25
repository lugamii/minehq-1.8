package dev.lugami.potpvp.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Applies rank prefixes, if they exist.
 * Copied from https://github.com/FrozenOrb/qHub/blob/master/src/main/java/net/frozenorb/qhub/listener/BasicListener.java#L103
 */
public final class ChatFormatListener implements Listener {

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

    }

}