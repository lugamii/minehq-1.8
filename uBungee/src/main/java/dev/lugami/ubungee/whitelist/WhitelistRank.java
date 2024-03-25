package dev.lugami.ubungee.whitelist;

import dev.lugami.ubungee.uBungee;
import lombok.AllArgsConstructor;
import lombok.Getter;
import dev.lugami.bridge.global.GlobalAPI;
import dev.lugami.bridge.global.ranks.Rank;

import java.util.UUID;

@AllArgsConstructor
public class WhitelistRank implements WhitelistType {

    @Getter private Rank minRank;

    public WhitelistRank(){
        if(uBungee.getInstance().getConfig().getString("whitelist.rank") == null){
            this.minRank = GlobalAPI.getRank("Default");
            return;
        }
        this.minRank = GlobalAPI.getRank(uBungee.getInstance().getConfig().getString("whitelist.rank"));
    }

    @Override
    public boolean isAllowed(UUID uuid) {
        GlobalAPI.getProfileOrCreateNew(uuid);
        Rank r = GlobalAPI.getPlayerRank(uuid, true);
        if(GlobalAPI.getProfile(uuid) == null || r.getPriority() < getMinRank().getPriority()){
            return false;
        }
        return true;
    }

    @Override
    public String getKickMessage() {
        return WhitelistType.getBaseMessage() +
                "\n\nThe server is for " + getMinRank().getColor() + getMinRank().getDisplayName() + " §crank and higher only";
    }

    @Override
    public String name() {
        return "rank";
    }

}