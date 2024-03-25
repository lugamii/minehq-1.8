package dev.lugami.potpvp;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import dev.lugami.qlib.qLib;
import dev.lugami.qlib.tab.data.TabList;
import dev.lugami.spigot.chunk.ChunkSnapshot;
import lombok.Getter;
import mkremins.fanciful.FancyMessage;
import dev.lugami.potpvp.arena.ArenaHandler;
import dev.lugami.potpvp.duel.DuelHandler;
import dev.lugami.potpvp.elo.EloHandler;
import dev.lugami.potpvp.follow.FollowHandler;
import dev.lugami.potpvp.kit.KitHandler;
import dev.lugami.potpvp.kittype.KitType;
import dev.lugami.potpvp.kittype.KitTypeJsonAdapter;
import dev.lugami.potpvp.kittype.KitTypeParameterType;
import dev.lugami.potpvp.listener.*;
import dev.lugami.potpvp.lobby.LobbyHandler;
import dev.lugami.potpvp.match.Match;
import dev.lugami.potpvp.match.MatchHandler;
import dev.lugami.potpvp.nametag.PotPvPNametagProvider;
import dev.lugami.potpvp.party.PartyHandler;
import dev.lugami.potpvp.postmatchinv.PostMatchInvHandler;
import dev.lugami.potpvp.pvpclasses.PvPClassHandler;
import dev.lugami.potpvp.queue.QueueHandler;
import dev.lugami.potpvp.rematch.RematchHandler;
import dev.lugami.potpvp.scoreboard.PotPvPScoreboardConfiguration;
import dev.lugami.potpvp.setting.SettingHandler;
import dev.lugami.potpvp.statistics.StatisticsHandler;
import dev.lugami.potpvp.tab.PotPvPLayoutProvider;
import dev.lugami.potpvp.tournament.TournamentHandler;
import dev.lugami.qlib.command.FrozenCommandHandler;
import dev.lugami.qlib.nametag.FrozenNametagHandler;
import dev.lugami.qlib.scoreboard.FrozenScoreboardHandler;
import dev.lugami.qlib.serialization.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public final class PotPvPSI extends JavaPlugin {

    @Getter
    private static PotPvPSI instance;
    @Getter
    private static Gson gson = new GsonBuilder()
            .registerTypeHierarchyAdapter(PotionEffect.class, new PotionEffectAdapter())
            .registerTypeHierarchyAdapter(ItemStack.class, new ItemStackAdapter())
            .registerTypeHierarchyAdapter(Location.class, new LocationAdapter())
            .registerTypeHierarchyAdapter(Vector.class, new VectorAdapter())
            .registerTypeAdapter(BlockVector.class, new BlockVectorAdapter())
            .registerTypeHierarchyAdapter(KitType.class, new KitTypeJsonAdapter()) // custom KitType serializer
            .registerTypeAdapter(ChunkSnapshot.class, new ChunkSnapshotAdapter())
            .serializeNulls()
            .create();

    private MongoClient mongoClient;
    @Getter
    private MongoDatabase mongoDatabase;

    @Getter
    private SettingHandler settingHandler;
    @Getter
    private DuelHandler duelHandler;
    @Getter
    private KitHandler kitHandler;
    @Getter
    private LobbyHandler lobbyHandler;
    private ArenaHandler arenaHandler;
    @Getter
    private MatchHandler matchHandler;
    @Getter
    private PartyHandler partyHandler;
    @Getter
    private QueueHandler queueHandler;
    @Getter
    private RematchHandler rematchHandler;
    @Getter
    private PostMatchInvHandler postMatchInvHandler;
    @Getter
    private FollowHandler followHandler;
    @Getter
    private EloHandler eloHandler;
    @Getter
    private TournamentHandler tournamentHandler;
    @Getter
    private PvPClassHandler pvpClassHandler;

    @Getter
    private ChatColor dominantColor = ChatColor.AQUA;

    @Override
    public void onEnable() {
        //SpigotConfig.onlyCustomTab = true; // because we'll definitely forget
        //this.dominantColor = ChatColor.DARK_PURPLE;
        instance = this;
        saveDefaultConfig();


        setupMongo();

        for (World world : Bukkit.getWorlds()) {
            world.setGameRuleValue("doDaylightCycle", "false");
            world.setGameRuleValue("doMobSpawning", "false");
            world.setTime(6_000L);
        }

        AtomicInteger index = new AtomicInteger(0);
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            FancyMessage message = new FancyMessage("TIP: ").color(ChatColor.GOLD);

            if (index.get() == 0) {
                message.then("Don't like the server? Knockback sucks? ").color(ChatColor.GRAY)
                        .then("[Click Here]").color(ChatColor.GREEN).command("/showmethedoor").tooltip(ChatColor.GREEN + ":)");

                index.set(0);
            } else {
                message.then("Pots too slow? Learn to pot or disconnect!").color(ChatColor.GRAY);

                index.incrementAndGet();
            }

            for (Player player : Bukkit.getOnlinePlayers()) {
                message.send(player);
            }
        }, 5 * 60 * 20L, 5 * 60 * 20L);

        settingHandler = new SettingHandler();
        duelHandler = new DuelHandler();
        kitHandler = new KitHandler();
        lobbyHandler = new LobbyHandler();
        arenaHandler = new ArenaHandler();
        matchHandler = new MatchHandler();
        partyHandler = new PartyHandler();
        queueHandler = new QueueHandler();
        rematchHandler = new RematchHandler();
        postMatchInvHandler = new PostMatchInvHandler();
        followHandler = new FollowHandler();
        eloHandler = new EloHandler();
        pvpClassHandler = new PvPClassHandler();
        tournamentHandler = new TournamentHandler();


        getServer().getPluginManager().registerEvents(new BasicPreventionListener(), this);
        getServer().getPluginManager().registerEvents(new BowHealthListener(), this);
        getServer().getPluginManager().registerEvents(new ChatFormatListener(), this);
        getServer().getPluginManager().registerEvents(new ChatToggleListener(), this);
        getServer().getPluginManager().registerEvents(new NightModeListener(), this);
        getServer().getPluginManager().registerEvents(new PearlCooldownListener(), this);
        getServer().getPluginManager().registerEvents(new RankedMatchQualificationListener(), this);
        getServer().getPluginManager().registerEvents(new TabCompleteListener(), this);
        getServer().getPluginManager().registerEvents(new StatisticsHandler(), this);

        FrozenCommandHandler.registerAll(this);
        FrozenCommandHandler.registerParameterType(KitType.class, new KitTypeParameterType());
        qLib.getInstance().getTabHandler().setTabList(new TabList(this, new PotPvPLayoutProvider()));
        FrozenNametagHandler.registerProvider(new PotPvPNametagProvider());
        FrozenScoreboardHandler.setConfiguration(PotPvPScoreboardConfiguration.create());

    }

    public void onDisable() {
        for (Match match : this.matchHandler.getHostedMatches()) {
            if (match.getKitType().isBuildingAllowed()) match.getArena().restore();
        }

        try {
            arenaHandler.saveSchematics();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String playerName : PvPClassHandler.getEquippedKits().keySet()) {
            PvPClassHandler.getEquippedKits().get(playerName).remove(getServer().getPlayerExact(playerName));
        }

        instance = null;
    }

    private void setupMongo() {
        mongoClient = new MongoClient(
                getConfig().getString("Mongo.Host"),
                getConfig().getInt("Mongo.Port")
        );

        String databaseId = getConfig().getString("Mongo.Database");
        mongoDatabase = mongoClient.getDatabase(databaseId);
    }

    // This is here because chunk snapshots are (still) being deserialized, and serialized sometimes.
    private static class ChunkSnapshotAdapter extends TypeAdapter<ChunkSnapshot> {

        @Override
        public ChunkSnapshot read(JsonReader arg0) throws IOException {
            return null;
        }

        @Override
        public void write(JsonWriter arg0, ChunkSnapshot arg1) throws IOException {

        }

    }

    public ArenaHandler getArenaHandler() {
        return arenaHandler;
    }

    public static PotPvPSI getInstance() {
        return instance;
    }
}