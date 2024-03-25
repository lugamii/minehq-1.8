package dev.lugami.bridge.global.packet.types;

import dev.lugami.bridge.BridgeGlobal;
import dev.lugami.bridge.global.packet.Packet;
import dev.lugami.bridge.global.util.SystemType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import dev.lugami.bridge.bukkit.listener.GeneralListener;
import dev.lugami.bridge.global.punishment.Punishment;

@AllArgsConstructor @NoArgsConstructor
public class PunishmentPacket implements Packet {

    private Punishment punishment;

    @Override
    public void onReceive() {
        if(BridgeGlobal.getSystemType() == SystemType.BUKKIT) GeneralListener.handlePunishment(punishment, punishment.isPardoned());
    }

}
