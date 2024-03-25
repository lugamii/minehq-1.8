package dev.lugami.ubungee.whitelist;

import dev.lugami.ubungee.uBungee;
import lombok.Getter;
import dev.lugami.bridge.global.GlobalAPI;
import dev.lugami.bridge.global.ranks.Rank;
import dev.lugami.bridge.global.util.OtherUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WhitelistUUID implements WhitelistType {

    @Getter private List<UUID> uuidList = new ArrayList<>();

    public WhitelistUUID(){
        uBungee.getInstance().getConfig().getStringList("whitelist.uuids").forEach(wl -> {
            if(OtherUtils.isUUID(wl)){
                uuidList.add(UUID.fromString(wl));
            }
        });
    }

    @Override
    public boolean isAllowed(UUID uuid) {
        Rank r = GlobalAPI.getPlayerRank(uuid, true);
        if(r.getPriority() >= 80){
            return true;
        }
        if(inList(uuid)){
            return true;
        }
        return false;
    }

    public boolean inList(UUID uuid){
        return uuidList.contains(uuid);
    }

    @Override
    public String getKickMessage() {
        return WhitelistType.getBaseMessage();
    }

    @Override
    public String name() {
        return "uuid";
    }
}
