package dev.lugami.bridge.bukkit.commands.grant.events;

import dev.lugami.bridge.global.grant.Grant;
import dev.lugami.bridge.bukkit.util.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class GrantCreateEvent extends BaseEvent {

    private UUID uuid;
    private Grant grant;

}
