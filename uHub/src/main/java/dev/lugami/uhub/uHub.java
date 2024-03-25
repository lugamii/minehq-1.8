package dev.lugami.uhub;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.lugami.qlib.qLib;
import dev.lugami.qlib.tab.data.TabList;
import dev.lugami.uhub.tab.uHubLayoutProvider;
import dev.lugami.uhub.util.BukkitUtil;
import dev.lugami.uhub.util.Cooldown;
import lombok.Getter;
import dev.lugami.qlib.command.FrozenCommandHandler;
import dev.lugami.qlib.scoreboard.FrozenScoreboardHandler;
import dev.lugami.uhub.scoreboard.ScoreboardAdapter;
import org.apache.http.impl.conn.tsccm.BasicPooledConnAdapter;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class uHub extends JavaPlugin implements PluginMessageListener {

    private static uHub instance;
    public static uHub plugin;
    public static HashMap<String, Integer> online;
    public static double cdtime;
    private static Location SPAWN_LOC;
    private int playerCountBungee;

    public uHub() {
        this.playerCountBungee = 0;
    }

    private Map<UUID, Boolean> hidingPlayers = new HashMap<>();
    private Map<UUID, Cooldown> hideCooldown = new HashMap<>();

    @Override
    public void onEnable() {
        uHub.instance = this;
        FrozenCommandHandler.registerAll(this);
        uHub.SPAWN_LOC = Bukkit.getWorld("world").getSpawnLocation();
        saveDefaultConfig();
        qLib.getInstance().getTabHandler().setTabList(new TabList(this, new uHubLayoutProvider()));
        FrozenScoreboardHandler.setConfiguration(ScoreboardAdapter.create());
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);

        new BukkitRunnable() {
            public void run() {
                uHub.this.broadcastGlobalPlayercountRequest();
            }
        }.runTaskTimer(this, 0L, 20L);

        for (World world : Bukkit.getWorlds()) {
            world.setGameRuleValue("doDaylightCycle", "false");
            world.setGameRuleValue("doMobSpawning", "false");
            world.setTime(6_000L);
        }

        FrozenScoreboardHandler.setConfiguration(ScoreboardAdapter.create());

        BukkitUtil.loadListenersFromPackage(this, "dev.lugami.uhub.listener");

        Bukkit.getServer().getWorld("world").setDifficulty(Difficulty.PEACEFUL);
        Bukkit.getServer().getWorld("world").setDifficulty(Difficulty.PEACEFUL);
    }

    public boolean isHidingPlayers(Player player) {
        return this.hidingPlayers.getOrDefault(player.getUniqueId(), false);
    }

    public boolean isOnHideCooldown(Player player) {
        return this.hideCooldown.containsKey(player.getUniqueId()) && !this.hideCooldown.get(player.getUniqueId()).hasExpired();
    }

    public Cooldown getHideCooldown(Player player) {
        return this.hideCooldown.get(player.getUniqueId());
    }

    public void onDisable() {
        uHub.instance = null;
    }

    static void setFinalStatic(Field field, Object newValue) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & 0xFFFFFFEF);
        field.set(null, newValue);
    }

    public void loadConfig() {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
    }

    public Location getSpawnLocation() {
        return uHub.SPAWN_LOC;
    }

    public static uHub getInstance() {
        return uHub.instance;
    }

    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        if (subchannel.equals("PlayerCount")) {
            String server = in.readUTF();
            int playercount = in.readInt();
            if (server.equalsIgnoreCase("ALL")) {
                this.playerCountBungee = playercount;
            }
        }
    }

    private void broadcastGlobalPlayercountRequest() {
        if (Bukkit.getOnlinePlayers().size() == 0) {
            return;
        }
        Player p = Bukkit.getOnlinePlayers().toArray(new Player[0])[0];
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PlayerCount");
        out.writeUTF("ALL");
        p.sendPluginMessage(this, "BungeeCord", out.toByteArray());
    }

    public int getPlayerCountBungee() {
        return this.playerCountBungee;
    }

    static {
        uHub.online = new HashMap<>();
        uHub.cdtime = 5.0;
    }
}