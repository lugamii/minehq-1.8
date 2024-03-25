package dev.lugami.hqueue.data;

import dev.lugami.bridge.BridgeGlobal;
import dev.lugami.bridge.global.profile.Profile;

import javax.validation.constraints.NotNull;
import java.beans.ConstructorProperties;
import java.util.UUID;

public class QueuePlayer implements Comparable {

    private UUID uuid;
    private long joinedQueue;

    public int compareTo(@NotNull Object object) {
        int result = 0;
        if (object instanceof QueuePlayer) {
            Profile otherProfile;
            QueuePlayer otherPlayer = (QueuePlayer)object;
            int playerPriority = 0;
            int otherPriority = 0;
            Profile playerProfile = BridgeGlobal.getProfileHandler().getProfileByUUID(this.uuid);
            if (playerProfile != null) {
                playerPriority = (playerProfile).getCurrentGrant().getRank().getPriority();
            }
            if ((otherProfile = BridgeGlobal.getProfileHandler().getProfileByUUID(otherPlayer.getUuid())) != null) {
                otherPriority = (otherProfile).getCurrentGrant().getRank().getPriority();
            }
            if ((result = playerPriority > otherPriority ? 0 : 1) == 1) {
                if (this.getJoinedQueue() < otherPlayer.getJoinedQueue()) {
                    return -1;
                }
                return 1;
            }
        }
        return result;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public long getJoinedQueue() {
        return this.joinedQueue;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setJoinedQueue(long joinedQueue) {
        this.joinedQueue = joinedQueue;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof QueuePlayer)) {
            return false;
        }
        QueuePlayer other = (QueuePlayer)o;
        if (!other.canEqual(this)) {
            return false;
        }
        UUID this$uuid = this.getUuid();
        UUID other$uuid = other.getUuid();
        if (this$uuid == null ? other$uuid != null : !((Object)this$uuid).equals(other$uuid)) {
            return false;
        }
        return this.getJoinedQueue() == other.getJoinedQueue();
    }

    protected boolean canEqual(Object other) {
        return other instanceof QueuePlayer;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        UUID $uuid = this.getUuid();
        result = result * 59 + ($uuid == null ? 43 : ((Object)$uuid).hashCode());
        long $joinedQueue = this.getJoinedQueue();
        result = result * 59 + (int)($joinedQueue >>> 32 ^ $joinedQueue);
        return result;
    }

    public String toString() {
        return "QueuePlayer(uuid=" + this.getUuid() + ", joinedQueue=" + this.getJoinedQueue() + ")";
    }

    @ConstructorProperties(value={"uuid", "joinedQueue"})
    public QueuePlayer(UUID uuid, long joinedQueue) {
        this.uuid = uuid;
        this.joinedQueue = joinedQueue;
    }

    public QueuePlayer() {
    }
}

