/* Decompiler 100ms, total 176ms, lines 1068 */
package com.moonsworth.fallback.player;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.moonsworth.fallback.check.AbstractCheck;
import com.moonsworth.fallback.check.ICheck;
import com.moonsworth.fallback.client.ClientType;
import com.moonsworth.fallback.client.EnumClientType;
import com.moonsworth.fallback.util.BlockPos;
import com.moonsworth.fallback.util.EventTimer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import com.moonsworth.fallback.Fallback;
import com.moonsworth.fallback.check.impl.aimassist.AimAssistA;
import com.moonsworth.fallback.check.impl.aimassist.AimAssistB;
import com.moonsworth.fallback.check.impl.aimassist.AimAssistC;
import com.moonsworth.fallback.check.impl.aimassist.AimAssistD;
import com.moonsworth.fallback.check.impl.aimassist.AimAssistE;
import com.moonsworth.fallback.check.impl.aimassist.AimAssistF;
import com.moonsworth.fallback.check.impl.aimassist.AimAssistG;
import com.moonsworth.fallback.check.impl.aimassist.AimAssistH;
import com.moonsworth.fallback.check.impl.aimassist.cinematic.Cinematic;
import com.moonsworth.fallback.check.impl.aimassist.sensitivity.Sensitivity;
import com.moonsworth.fallback.check.impl.autoclicker.AutoClickerA;
import com.moonsworth.fallback.check.impl.autoclicker.AutoClickerB;
import com.moonsworth.fallback.check.impl.autoclicker.AutoClickerC;
import com.moonsworth.fallback.check.impl.autoclicker.AutoClickerD;
import com.moonsworth.fallback.check.impl.autoclicker.AutoClickerG;
import com.moonsworth.fallback.check.impl.autoclicker.AutoClickerH;
import com.moonsworth.fallback.check.impl.autoclicker.AutoClickerI;
import com.moonsworth.fallback.check.impl.autoclicker.AutoClickerJ;
import com.moonsworth.fallback.check.impl.autoclicker.AutoClickerK;
import com.moonsworth.fallback.check.impl.autoclicker.AutoClickerL;
import com.moonsworth.fallback.check.impl.autoclicker.AutoClickerM;
import com.moonsworth.fallback.check.impl.autoclicker.AutoClickerN;
import com.moonsworth.fallback.check.impl.autoclicker.AutoClickerO;
import com.moonsworth.fallback.check.impl.autoclicker.AutoClickerP;
import com.moonsworth.fallback.check.impl.autoclicker.AutoClickerQ;
import com.moonsworth.fallback.check.impl.badpackets.BadPacketsA;
import com.moonsworth.fallback.check.impl.badpackets.BadPacketsB;
import com.moonsworth.fallback.check.impl.badpackets.BadPacketsC;
import com.moonsworth.fallback.check.impl.badpackets.BadPacketsD;
import com.moonsworth.fallback.check.impl.badpackets.BadPacketsE;
import com.moonsworth.fallback.check.impl.badpackets.BadPacketsF;
import com.moonsworth.fallback.check.impl.badpackets.BadPacketsG;
import com.moonsworth.fallback.check.impl.badpackets.BadPacketsH;
import com.moonsworth.fallback.check.impl.badpackets.BadPacketsI;
import com.moonsworth.fallback.check.impl.badpackets.BadPacketsJ;
import com.moonsworth.fallback.check.impl.badpackets.BadPacketsL;
import com.moonsworth.fallback.check.impl.badpackets.BadPacketsM;
import com.moonsworth.fallback.check.impl.badpackets.BadPacketsN;
import com.moonsworth.fallback.check.impl.fly.FlyA;
import com.moonsworth.fallback.check.impl.fly.FlyB;
import com.moonsworth.fallback.check.impl.fly.FlyC;
import com.moonsworth.fallback.check.impl.fly.FlyD;
import com.moonsworth.fallback.check.impl.fly.FlyE;
import com.moonsworth.fallback.check.impl.invalid.InvalidA;
import com.moonsworth.fallback.check.impl.invalid.InvalidB;
import com.moonsworth.fallback.check.impl.inventory.InventoryA;
import com.moonsworth.fallback.check.impl.inventory.InventoryB;
import com.moonsworth.fallback.check.impl.inventory.InventoryC;
import com.moonsworth.fallback.check.impl.inventory.InventoryD;
import com.moonsworth.fallback.check.impl.inventory.InventoryE;
import com.moonsworth.fallback.check.impl.inventory.InventoryF;
import com.moonsworth.fallback.check.impl.inventory.InventoryG;
import com.moonsworth.fallback.check.impl.killaura.KillAuraA;
import com.moonsworth.fallback.check.impl.killaura.KillAuraC;
import com.moonsworth.fallback.check.impl.killaura.KillAuraD;
import com.moonsworth.fallback.check.impl.killaura.KillAuraE;
import com.moonsworth.fallback.check.impl.killaura.KillAuraF;
import com.moonsworth.fallback.check.impl.killaura.KillAuraG;
import com.moonsworth.fallback.check.impl.killaura.KillAuraI;
import com.moonsworth.fallback.check.impl.killaura.KillAuraJ;
import com.moonsworth.fallback.check.impl.killaura.KillAuraK;
import com.moonsworth.fallback.check.impl.killaura.KillAuraL;
import com.moonsworth.fallback.check.impl.killaura.KillAuraM;
import com.moonsworth.fallback.check.impl.killaura.KillAuraN;
import com.moonsworth.fallback.check.impl.killaura.KillAuraO;
import com.moonsworth.fallback.check.impl.killaura.KillAuraP;
import com.moonsworth.fallback.check.impl.killaura.KillAuraQ;
import com.moonsworth.fallback.check.impl.killaura.KillAuraR;
import com.moonsworth.fallback.check.impl.killaura.KillAuraS;
import com.moonsworth.fallback.check.impl.killaura.KillAuraT;
import com.moonsworth.fallback.check.impl.motion.MotionA;
import com.moonsworth.fallback.check.impl.motion.MotionB;
import com.moonsworth.fallback.check.impl.range.RangeA;
import com.moonsworth.fallback.check.impl.range.RangeB;
import com.moonsworth.fallback.check.impl.scaffold.ScaffoldB;
import com.moonsworth.fallback.check.impl.scaffold.ScaffoldC;
import com.moonsworth.fallback.check.impl.speed.SpeedA;
import com.moonsworth.fallback.check.impl.speed.SpeedB;
import com.moonsworth.fallback.check.impl.speed.SpeedC;
import com.moonsworth.fallback.check.impl.speed.SpeedD;
import com.moonsworth.fallback.check.impl.speed.SpeedE;
import com.moonsworth.fallback.check.impl.speed.SpeedF;
import com.moonsworth.fallback.check.impl.speed.SpeedG;
import com.moonsworth.fallback.check.impl.timer.TimerA;
import com.moonsworth.fallback.check.impl.timer.TimerB;
import com.moonsworth.fallback.check.impl.velocity.VelocityA;
import com.moonsworth.fallback.check.impl.velocity.VelocityB;
import com.moonsworth.fallback.check.impl.velocity.VelocityC;
import com.moonsworth.fallback.util.CustomLocation;
import com.moonsworth.fallback.util.VelocityTracker;

public class PlayerData {
    private static Map<Class<? extends ICheck>, Constructor<? extends ICheck>> CONSTRUCTORS;
    public static Class<? extends ICheck>[] CHECKS;
    private UUID uuid;
    private Set<UUID> playersWatching;
    private Map<String, String> forgeMods;
    private CustomLocation lastMovePacket;
    private ClientType client;
    private UUID lastTarget;
    private Entity lastTargetEntity;
    private String randomBanReason;
    private VelocityTracker velocityTracker;
    public ArrayList<AbstractCheck> flaggedChecks;
    private Set<CustomLocation> teleportLocations;
    private Map<Class<? extends ICheck>, ICheck> checkMap;
    private Map<Integer, Long> keepAliveTimes;
    private Map<ICheck, Double> checkVlMap;
    private Set<BlockPos> fakeBlocks;
    private StringBuilder sniffedPacketBuilder;
    private Map<UUID, List<CustomLocation>> recentPlayerPackets;
    private Map<ICheck, Set<Long>> checkViolationTimes;
    private boolean randomBan;
    private boolean allowTeleport;
    private boolean cinematic;
    private boolean inventoryOpen;
    private boolean setInventoryOpen;
    private boolean attackedSinceVelocity;
    private boolean underBlock;
    private boolean sprinting;
    private boolean inLiquid;
    private boolean instantBreakDigging;
    private boolean fakeDigging;
    private boolean onGround;
    private boolean sniffing;
    private boolean onStairs;
    private boolean onLadder;
    private boolean onCarpet;
    private boolean placing;
    private boolean banning;
    private boolean digging;
    private boolean inWeb;
    private boolean onIce;
    private boolean verifyingSensitivity;
    private double sensitivity;
    private boolean wasUnderBlock;
    private boolean wasOnGround;
    private boolean wasInLiquid;
    private boolean wasInWeb;
    public boolean devalerts;
    public boolean staffalerts;
    private double movementSpeed;
    private double lastGroundY;
    private double LastDistanceY;
    private double randomBanRate;
    private double velocityX;
    private double velocityY;
    private double velocityZ;
    private long lastAttack;
    private long lastDelayedMovePacket;
    private long lastAnimationPacket;
    private long lastAttackPacket;
    private long lastVelocity;
    public long lastFlag;
    public long addedToBanwave;
    private long ping;
    public int violations;
    private int LastAttackTime;
    private int velocityH;
    private int AirTicks;
    public int currentTick;
    private int deathTicks;
    private int GroundTicks;
    private int RespawnTicks;
    private int teleportTicks;
    private int standTicks;
    private int velocityV;
    private int lastCps;
    private int LastTeleportTime;
    private int movementsSinceIce;
    private int movementsSinceUnderBlock;
    public int InvalidKeepAlivesVerbose;
    public boolean hasLooked;
    private EventTimer velocityTimer;
    private EventTimer iceTimer;
    private EventTimer blockAboveTimer;

    public PlayerData(UUID uuid) {
        this.client = EnumClientType.VANILLA;
        this.velocityTracker = new VelocityTracker();
        this.flaggedChecks = new ArrayList();
        this.teleportLocations = Collections.newSetFromMap(new ConcurrentHashMap());
        this.checkMap = new HashMap();
        this.keepAliveTimes = new HashMap();
        this.checkVlMap = new HashMap();
        this.fakeBlocks = new HashSet();
        this.sniffedPacketBuilder = new StringBuilder();
        this.recentPlayerPackets = new HashMap();
        this.checkViolationTimes = new HashMap();
        this.devalerts = false;
        this.staffalerts = false;
        this.uuid = uuid;
        this.playersWatching = new HashSet();
        this.setupEventTimers();
        Fallback.instance.getServer().getScheduler().runTaskAsynchronously(Fallback.instance, () -> {
            CONSTRUCTORS.keySet().stream().map((o) -> {
                return o;
            }).forEach((check) -> {
                Constructor constructor = (Constructor)CONSTRUCTORS.get(check);

                try {
                    this.checkMap.put(check, (ICheck) constructor.newInstance(this));
                } catch (Exception var4) {
                    var4.printStackTrace();
                }

            });
        });
    }

    private void setupEventTimers() {
        this.velocityTimer = new EventTimer(20, this);
        this.iceTimer = new EventTimer(20, this);
        this.blockAboveTimer = new EventTimer(20, this);
    }

    public <T extends ICheck> T getCheck(Class<T> clazz) {
        return (T) this.checkMap.get(clazz);
    }

    public AbstractCheck getCheckByName(String name) {
        Iterator var2 = this.getFlaggedChecks().iterator();

        AbstractCheck check;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            check = (AbstractCheck)var2.next();
        } while(!check.getName().equalsIgnoreCase(name));

        return check;
    }

    public CustomLocation getLastPlayerPacket(UUID playerUUID, int index) {
        List<CustomLocation> customLocations = (List)this.recentPlayerPackets.get(playerUUID);
        return customLocations != null && customLocations.size() > index ? (CustomLocation)customLocations.get(customLocations.size() - index) : null;
    }

    public void updateTimers() {
        if (this.onIce) {
            this.iceTimer.reset();
        }

        if (this.isUnderBlock()) {
            this.blockAboveTimer.reset();
        }

    }

    public boolean isLagging() {
        long now = System.currentTimeMillis();
        return now - this.lastDelayedMovePacket < 220L || this.teleportTicks > 0;
    }

    public void addPlayerPacket(UUID playerUUID, CustomLocation customLocation) {
        List<CustomLocation> customLocations = (List)this.recentPlayerPackets.get(playerUUID);
        if (customLocations == null) {
            customLocations = new ArrayList();
        }

        if (((List)customLocations).size() == 20) {
            ((List)customLocations).remove(0);
        }

        ((List)customLocations).add(customLocation);
        this.recentPlayerPackets.put(playerUUID, customLocations);
    }

    public void addTeleportLocation(CustomLocation teleportLocation) {
        this.teleportLocations.add(teleportLocation);
    }

    public boolean allowTeleport(CustomLocation teleportLocation) {
        Iterator var2 = this.teleportLocations.iterator();

        CustomLocation customLocation;
        double delta;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            customLocation = (CustomLocation)var2.next();
            delta = Math.pow(teleportLocation.getX() - customLocation.getX(), 2.0D) + Math.pow(teleportLocation.getZ() - customLocation.getZ(), 2.0D);
        } while(delta > 0.005D);

        this.teleportLocations.remove(customLocation);
        return true;
    }

    public void setDeathTicks(int deathTicks) {
        this.deathTicks = deathTicks;
    }

    public double getCheckVl(ICheck check) {
        if (!this.checkVlMap.containsKey(check)) {
            this.checkVlMap.put(check, 0.0D);
        }

        return (Double)this.checkVlMap.get(check);
    }

    public void setCheckVl(double vl, ICheck check) {
        if (vl < 0.0D) {
            vl = 0.0D;
        }

        this.checkVlMap.put(check, vl);
    }

    public boolean keepAliveExists(int id) {
        return this.keepAliveTimes.containsKey(id);
    }

    public long getKeepAliveTime(int id) {
        return (Long)this.keepAliveTimes.get(id);
    }

    public void removeKeepAliveTime(int id) {
        this.keepAliveTimes.remove(id);
    }

    public boolean isPlayerWatching(Player player) {
        return this.playersWatching.contains(player.getUniqueId());
    }

    public void togglePlayerWatching(Player player) {
        if (!this.playersWatching.remove(player.getUniqueId())) {
            this.playersWatching.add(player.getUniqueId());
        }

    }

    public void addKeepAliveTime(int id) {
        this.keepAliveTimes.put(id, System.currentTimeMillis());
    }

    public int getViolations(ICheck check, Long time) {
        Set<Long> timestamps = (Set)this.checkViolationTimes.get(check);
        if (timestamps != null) {
            int violations = 0;
            Iterator var5 = timestamps.iterator();

            while(var5.hasNext()) {
                long timestamp = (Long)var5.next();
                if (System.currentTimeMillis() - timestamp <= time) {
                    ++violations;
                }
            }

            return violations;
        } else {
            return 0;
        }
    }

    public int getViolations(ICheck check) {
        Set<Long> logs = (Set)this.checkViolationTimes.get(check);
        return logs.size();
    }

    public void addViolation(ICheck check) {
        Set<Long> timestamps = (Set)this.checkViolationTimes.get(check);
        if (timestamps == null) {
            timestamps = new HashSet();
        }

        ((Set)timestamps).add(System.currentTimeMillis());
        this.checkViolationTimes.put(check, timestamps);
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setPlayersWatching(Set<UUID> playersWatching) {
        this.playersWatching = playersWatching;
    }

    public void setForgeMods(Map<String, String> forgeMods) {
        this.forgeMods = forgeMods;
    }

    public void setLastMovePacket(CustomLocation lastMovePacket) {
        this.lastMovePacket = lastMovePacket;
    }

    public void setClient(ClientType client) {
        this.client = client;
    }

    public void setLastTarget(UUID lastTarget) {
        this.lastTarget = lastTarget;
    }

    public void setLastTargetEntity(Entity lastTargetEntity) {
        this.lastTargetEntity = lastTargetEntity;
    }

    public void setRandomBanReason(String randomBanReason) {
        this.randomBanReason = randomBanReason;
    }

    public void setVelocityTracker(VelocityTracker velocityTracker) {
        this.velocityTracker = velocityTracker;
    }

    public void setFlaggedChecks(ArrayList<AbstractCheck> flaggedChecks) {
        this.flaggedChecks = flaggedChecks;
    }

    public void setTeleportLocations(Set<CustomLocation> teleportLocations) {
        this.teleportLocations = teleportLocations;
    }

    public void setCheckMap(Map<Class<? extends ICheck>, ICheck> checkMap) {
        this.checkMap = checkMap;
    }

    public void setKeepAliveTimes(Map<Integer, Long> keepAliveTimes) {
        this.keepAliveTimes = keepAliveTimes;
    }

    public void setCheckVlMap(Map<ICheck, Double> checkVlMap) {
        this.checkVlMap = checkVlMap;
    }

    public void setFakeBlocks(Set<BlockPos> fakeBlocks) {
        this.fakeBlocks = fakeBlocks;
    }

    public void setSniffedPacketBuilder(StringBuilder sniffedPacketBuilder) {
        this.sniffedPacketBuilder = sniffedPacketBuilder;
    }

    public void setRecentPlayerPackets(Map<UUID, List<CustomLocation>> recentPlayerPackets) {
        this.recentPlayerPackets = recentPlayerPackets;
    }

    public void setCheckViolationTimes(Map<ICheck, Set<Long>> checkViolationTimes) {
        this.checkViolationTimes = checkViolationTimes;
    }

    public void setRandomBan(boolean randomBan) {
        this.randomBan = randomBan;
    }

    public void setAllowTeleport(boolean allowTeleport) {
        this.allowTeleport = allowTeleport;
    }

    public void setCinematic(boolean cinematic) {
        this.cinematic = cinematic;
    }

    public void setInventoryOpen(boolean inventoryOpen) {
        this.inventoryOpen = inventoryOpen;
    }

    public void setSetInventoryOpen(boolean setInventoryOpen) {
        this.setInventoryOpen = setInventoryOpen;
    }

    public void setAttackedSinceVelocity(boolean attackedSinceVelocity) {
        this.attackedSinceVelocity = attackedSinceVelocity;
    }

    public void setUnderBlock(boolean underBlock) {
        this.underBlock = underBlock;
    }

    public void setSprinting(boolean sprinting) {
        this.sprinting = sprinting;
    }

    public void setInLiquid(boolean inLiquid) {
        this.inLiquid = inLiquid;
    }

    public void setInstantBreakDigging(boolean instantBreakDigging) {
        this.instantBreakDigging = instantBreakDigging;
    }

    public void setFakeDigging(boolean fakeDigging) {
        this.fakeDigging = fakeDigging;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public void setSniffing(boolean sniffing) {
        this.sniffing = sniffing;
    }

    public void setOnStairs(boolean onStairs) {
        this.onStairs = onStairs;
    }

    public void setOnLadder(boolean onLadder) {
        this.onLadder = onLadder;
    }

    public void setOnCarpet(boolean onCarpet) {
        this.onCarpet = onCarpet;
    }

    public void setPlacing(boolean placing) {
        this.placing = placing;
    }

    public void setBanning(boolean banning) {
        this.banning = banning;
    }

    public void setDigging(boolean digging) {
        this.digging = digging;
    }

    public void setInWeb(boolean inWeb) {
        this.inWeb = inWeb;
    }

    public void setOnIce(boolean onIce) {
        this.onIce = onIce;
    }

    public void setVerifyingSensitivity(boolean verifyingSensitivity) {
        this.verifyingSensitivity = verifyingSensitivity;
    }

    public void setSensitivity(double sensitivity) {
        this.sensitivity = sensitivity;
    }

    public void setWasUnderBlock(boolean wasUnderBlock) {
        this.wasUnderBlock = wasUnderBlock;
    }

    public void setWasOnGround(boolean wasOnGround) {
        this.wasOnGround = wasOnGround;
    }

    public void setWasInLiquid(boolean wasInLiquid) {
        this.wasInLiquid = wasInLiquid;
    }

    public void setWasInWeb(boolean wasInWeb) {
        this.wasInWeb = wasInWeb;
    }

    public void setDevalerts(boolean devalerts) {
        this.devalerts = devalerts;
    }

    public void setStaffalerts(boolean staffalerts) {
        this.staffalerts = staffalerts;
    }

    public void setMovementSpeed(double movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    public void setLastGroundY(double lastGroundY) {
        this.lastGroundY = lastGroundY;
    }

    public void setLastDistanceY(double LastDistanceY) {
        this.LastDistanceY = LastDistanceY;
    }

    public void setRandomBanRate(double randomBanRate) {
        this.randomBanRate = randomBanRate;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public void setVelocityZ(double velocityZ) {
        this.velocityZ = velocityZ;
    }

    public void setLastAttack(long lastAttack) {
        this.lastAttack = lastAttack;
    }

    public void setLastDelayedMovePacket(long lastDelayedMovePacket) {
        this.lastDelayedMovePacket = lastDelayedMovePacket;
    }

    public void setLastAnimationPacket(long lastAnimationPacket) {
        this.lastAnimationPacket = lastAnimationPacket;
    }

    public void setLastAttackPacket(long lastAttackPacket) {
        this.lastAttackPacket = lastAttackPacket;
    }

    public void setLastVelocity(long lastVelocity) {
        this.lastVelocity = lastVelocity;
    }

    public void setLastFlag(long lastFlag) {
        this.lastFlag = lastFlag;
    }

    public void setAddedToBanwave(long addedToBanwave) {
        this.addedToBanwave = addedToBanwave;
    }

    public void setPing(long ping) {
        this.ping = ping;
    }

    public void setViolations(int violations) {
        this.violations = violations;
    }

    public void setLastAttackTime(int LastAttackTime) {
        this.LastAttackTime = LastAttackTime;
    }

    public void setVelocityH(int velocityH) {
        this.velocityH = velocityH;
    }

    public void setAirTicks(int AirTicks) {
        this.AirTicks = AirTicks;
    }

    public void setCurrentTick(int currentTick) {
        this.currentTick = currentTick;
    }

    public void setGroundTicks(int GroundTicks) {
        this.GroundTicks = GroundTicks;
    }

    public void setRespawnTicks(int RespawnTicks) {
        this.RespawnTicks = RespawnTicks;
    }

    public void setTeleportTicks(int teleportTicks) {
        this.teleportTicks = teleportTicks;
    }

    public void setStandTicks(int standTicks) {
        this.standTicks = standTicks;
    }

    public void setVelocityV(int velocityV) {
        this.velocityV = velocityV;
    }

    public void setLastCps(int lastCps) {
        this.lastCps = lastCps;
    }

    public void setLastTeleportTime(int LastTeleportTime) {
        this.LastTeleportTime = LastTeleportTime;
    }

    public void setMovementsSinceIce(int movementsSinceIce) {
        this.movementsSinceIce = movementsSinceIce;
    }

    public void setMovementsSinceUnderBlock(int movementsSinceUnderBlock) {
        this.movementsSinceUnderBlock = movementsSinceUnderBlock;
    }

    public void setInvalidKeepAlivesVerbose(int InvalidKeepAlivesVerbose) {
        this.InvalidKeepAlivesVerbose = InvalidKeepAlivesVerbose;
    }

    public void setHasLooked(boolean hasLooked) {
        this.hasLooked = hasLooked;
    }

    public void setVelocityTimer(EventTimer velocityTimer) {
        this.velocityTimer = velocityTimer;
    }

    public void setIceTimer(EventTimer iceTimer) {
        this.iceTimer = iceTimer;
    }

    public void setBlockAboveTimer(EventTimer blockAboveTimer) {
        this.blockAboveTimer = blockAboveTimer;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public Set<UUID> getPlayersWatching() {
        return this.playersWatching;
    }

    public Map<String, String> getForgeMods() {
        return this.forgeMods;
    }

    public CustomLocation getLastMovePacket() {
        return this.lastMovePacket;
    }

    public ClientType getClient() {
        return this.client;
    }

    public UUID getLastTarget() {
        return this.lastTarget;
    }

    public Entity getLastTargetEntity() {
        return this.lastTargetEntity;
    }

    public String getRandomBanReason() {
        return this.randomBanReason;
    }

    public VelocityTracker getVelocityTracker() {
        return this.velocityTracker;
    }

    public ArrayList<AbstractCheck> getFlaggedChecks() {
        return this.flaggedChecks;
    }

    public Set<CustomLocation> getTeleportLocations() {
        return this.teleportLocations;
    }

    public Map<Class<? extends ICheck>, ICheck> getCheckMap() {
        return this.checkMap;
    }

    public Map<Integer, Long> getKeepAliveTimes() {
        return this.keepAliveTimes;
    }

    public Map<ICheck, Double> getCheckVlMap() {
        return this.checkVlMap;
    }

    public Set<BlockPos> getFakeBlocks() {
        return this.fakeBlocks;
    }

    public StringBuilder getSniffedPacketBuilder() {
        return this.sniffedPacketBuilder;
    }

    public Map<UUID, List<CustomLocation>> getRecentPlayerPackets() {
        return this.recentPlayerPackets;
    }

    public Map<ICheck, Set<Long>> getCheckViolationTimes() {
        return this.checkViolationTimes;
    }

    public boolean isRandomBan() {
        return this.randomBan;
    }

    public boolean isAllowTeleport() {
        return this.allowTeleport;
    }

    public boolean isCinematic() {
        return this.cinematic;
    }

    public boolean isInventoryOpen() {
        return this.inventoryOpen;
    }

    public boolean isSetInventoryOpen() {
        return this.setInventoryOpen;
    }

    public boolean isAttackedSinceVelocity() {
        return this.attackedSinceVelocity;
    }

    public boolean isUnderBlock() {
        return this.underBlock;
    }

    public boolean isSprinting() {
        return this.sprinting;
    }

    public boolean isInLiquid() {
        return this.inLiquid;
    }

    public boolean isInstantBreakDigging() {
        return this.instantBreakDigging;
    }

    public boolean isFakeDigging() {
        return this.fakeDigging;
    }

    public boolean isOnGround() {
        return this.onGround;
    }

    public boolean isSniffing() {
        return this.sniffing;
    }

    public boolean isOnStairs() {
        return this.onStairs;
    }

    public boolean isOnLadder() {
        return this.onLadder;
    }

    public boolean isOnCarpet() {
        return this.onCarpet;
    }

    public boolean isPlacing() {
        return this.placing;
    }

    public boolean isBanning() {
        return this.banning;
    }

    public boolean isDigging() {
        return this.digging;
    }

    public boolean isInWeb() {
        return this.inWeb;
    }

    public boolean isOnIce() {
        return this.onIce;
    }

    public boolean isVerifyingSensitivity() {
        return this.verifyingSensitivity;
    }

    public double getSensitivity() {
        return this.sensitivity;
    }

    public boolean isWasUnderBlock() {
        return this.wasUnderBlock;
    }

    public boolean isWasOnGround() {
        return this.wasOnGround;
    }

    public boolean isWasInLiquid() {
        return this.wasInLiquid;
    }

    public boolean isWasInWeb() {
        return this.wasInWeb;
    }

    public boolean isDevalerts() {
        return this.devalerts;
    }

    public boolean isStaffalerts() {
        return this.staffalerts;
    }

    public double getMovementSpeed() {
        return this.movementSpeed;
    }

    public double getLastGroundY() {
        return this.lastGroundY;
    }

    public double getLastDistanceY() {
        return this.LastDistanceY;
    }

    public double getRandomBanRate() {
        return this.randomBanRate;
    }

    public double getVelocityX() {
        return this.velocityX;
    }

    public double getVelocityY() {
        return this.velocityY;
    }

    public double getVelocityZ() {
        return this.velocityZ;
    }

    public long getLastAttack() {
        return this.lastAttack;
    }

    public long getLastDelayedMovePacket() {
        return this.lastDelayedMovePacket;
    }

    public long getLastAnimationPacket() {
        return this.lastAnimationPacket;
    }

    public long getLastAttackPacket() {
        return this.lastAttackPacket;
    }

    public long getLastVelocity() {
        return this.lastVelocity;
    }

    public long getLastFlag() {
        return this.lastFlag;
    }

    public long getAddedToBanwave() {
        return this.addedToBanwave;
    }

    public long getPing() {
        return this.ping;
    }

    public int getViolations() {
        return this.violations;
    }

    public int getLastAttackTime() {
        return this.LastAttackTime;
    }

    public int getVelocityH() {
        return this.velocityH;
    }

    public int getAirTicks() {
        return this.AirTicks;
    }

    public int getCurrentTick() {
        return this.currentTick;
    }

    public int getDeathTicks() {
        return this.deathTicks;
    }

    public int getGroundTicks() {
        return this.GroundTicks;
    }

    public int getRespawnTicks() {
        return this.RespawnTicks;
    }

    public int getTeleportTicks() {
        return this.teleportTicks;
    }

    public int getStandTicks() {
        return this.standTicks;
    }

    public int getVelocityV() {
        return this.velocityV;
    }

    public int getLastCps() {
        return this.lastCps;
    }

    public int getLastTeleportTime() {
        return this.LastTeleportTime;
    }

    public int getMovementsSinceIce() {
        return this.movementsSinceIce;
    }

    public int getMovementsSinceUnderBlock() {
        return this.movementsSinceUnderBlock;
    }

    public int getInvalidKeepAlivesVerbose() {
        return this.InvalidKeepAlivesVerbose;
    }

    public boolean isHasLooked() {
        return this.hasLooked;
    }

    public EventTimer getVelocityTimer() {
        return this.velocityTimer;
    }

    public EventTimer getIceTimer() {
        return this.iceTimer;
    }

    public EventTimer getBlockAboveTimer() {
        return this.blockAboveTimer;
    }

    static {
        List<Class<? extends ICheck>> checks = Arrays.asList(Cinematic.class, Sensitivity.class, AimAssistA.class, AimAssistB.class, AimAssistC.class, AimAssistD.class, AimAssistE.class, AimAssistF.class, AimAssistG.class, AimAssistH.class, AutoClickerA.class, AutoClickerB.class, AutoClickerC.class, AutoClickerD.class, AutoClickerG.class, AutoClickerH.class, AutoClickerI.class, AutoClickerJ.class, AutoClickerK.class, AutoClickerK.class, AutoClickerL.class, AutoClickerM.class, AutoClickerN.class, AutoClickerO.class, AutoClickerP.class, AutoClickerQ.class, BadPacketsA.class, BadPacketsB.class, BadPacketsC.class, BadPacketsD.class, BadPacketsE.class, BadPacketsF.class, BadPacketsG.class, BadPacketsH.class, BadPacketsI.class, BadPacketsJ.class, BadPacketsL.class, BadPacketsM.class, BadPacketsN.class, FlyA.class, FlyB.class, FlyC.class, FlyE.class, FlyC.class, FlyD.class, FlyE.class, SpeedA.class, SpeedB.class, SpeedC.class, SpeedD.class, SpeedE.class, SpeedF.class, SpeedG.class, InventoryA.class, InventoryB.class, InventoryC.class, InventoryD.class, InventoryE.class, InventoryF.class, InventoryG.class, KillAuraA.class, KillAuraC.class, KillAuraD.class, KillAuraE.class, KillAuraF.class, KillAuraG.class, KillAuraI.class, KillAuraJ.class, KillAuraK.class, KillAuraL.class, KillAuraM.class, KillAuraN.class, KillAuraO.class, KillAuraP.class, KillAuraQ.class, KillAuraR.class, KillAuraS.class, KillAuraT.class, InvalidA.class, InvalidB.class, MotionA.class, MotionB.class, RangeA.class, RangeB.class, TimerA.class, TimerB.class, VelocityA.class, VelocityB.class, VelocityC.class, ScaffoldB.class, ScaffoldC.class);
        CHECKS = (Class[])checks.toArray(new Class[checks.size()]);
        CONSTRUCTORS = new ConcurrentHashMap();
        Class[] var1 = CHECKS;
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            Class check = var1[var3];

            try {
                CONSTRUCTORS.put(check, check.getConstructor(PlayerData.class));
            } catch (NoSuchMethodException var6) {
                var6.printStackTrace();
            }
        }

    }
}