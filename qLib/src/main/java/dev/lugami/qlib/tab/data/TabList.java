package dev.lugami.qlib.tab.data;

import com.google.common.collect.Multimap;
import com.google.common.collect.Table;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.HttpAuthenticationService;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.util.UUIDTypeAdapter;
import dev.lugami.qlib.qLib;
import dev.lugami.qlib.tab.provider.TabProvider;
import dev.lugami.spigot.handler.PacketHandler;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class TabList implements Listener, PacketHandler {

	@Getter private JavaPlugin plugin;
	@Getter private TabProvider provider;

	public static Field info_action, info_profile_list;

	public static Field team_name, team_display, team_prefix, team_suffix, team_players, team_mode, team_color, team_nametag;

	public static Field network_channel;

	public static Field propertymap;
	public static Property head = new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTY1OTM2NjA2MDExNCwKICAicHJvZmlsZUlkIiA6ICJhY2NjNTNkY2FkNjY0YzEyOTU3ZGUzYTIwODE5OTZmMyIsCiAgInByb2ZpbGVOYW1lIiA6ICJDb2RlckFsaSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8zNTU3MzgyZDU4MWE5NzZkYTc2NmI0NGNiNDEwNWMzNjQzMWEzNjIwYzE3MjUwMTczYjI2YzY2MmNlNGY4M2NmIgogICAgfQogIH0KfQ==", "lTAFCWBk/YTBheTDwnkYsMj35DvJqy2BFGHcsQJxSN41Jin0wq5Z9ZRbL1lx2Vryijq4SbCcEgrRQvG8iz5vxcX8p4EFOFeQjEz4oaAlXSVGSfscOKeHSUA0xmPTx1JmPOTyuq1KvZoTsYa9ctx3oZMOuhXUdQmX5ZoBo9gdvXIiEfLv6+LoBoqkp9MQ8G8hcxw6FKkQ6sgKaCkRqQZ4lLxrnflm/CMwlcXD99DGs5VTlYAtYBKRl5bcoutEXBebz4Lw093yz7Z2I1jJJAGrRtyExkXkGxXOKL/w+Cnnu0mxjcZvFS2enKf++6FL3KPybZ5JtHmTLyXRVqWZ5OoekZHnLQmto8CaqvSCTDt0uLdMBfwocWJZdIoFy7EecpsjBdhS1UojkT6rkrB8Rf/mPz0xGktOwdOZn9ReYUcilU+p+x3+Q9a6ODlf9km2nK+27TY2pEnneL9LFK9mepR6gXGvkx11VjmtJNp9EPflPiE0WVSSeazPFFu2olyMCzJ1Clyzw2T5wgusSjSnhoQvKExsvjVtATbybN97diamzjzz3I5cgANbXOYFB3Z0Uey5tpcJdtHE9VKYHauaTQSqf8VXJYEDT0oOnLPoTMe11xtsmz/riWkfgGzp+cCoGpW8DyqyB9BVn9jSi9z99YCRiXYDgZyY+vCGwklrSnpECKI=");

	/**
	 * @param plugin
	 */
	public TabList(JavaPlugin plugin, TabProvider provider) {
		this.plugin = plugin;
		this.provider = provider;

		this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);


	}
	static {
		try {
			info_action = PacketPlayOutPlayerInfo.class.getDeclaredField("a");
			info_action.setAccessible(true);
			info_profile_list = PacketPlayOutPlayerInfo.class.getDeclaredField("b");
			info_profile_list.setAccessible(true);

			team_name = PacketPlayOutScoreboardTeam.class.getDeclaredField("a");
			team_name.setAccessible(true);
			team_display = PacketPlayOutScoreboardTeam.class.getDeclaredField("b");
			team_display.setAccessible(true);
			team_prefix = PacketPlayOutScoreboardTeam.class.getDeclaredField("c");
			team_prefix.setAccessible(true);
			team_suffix = PacketPlayOutScoreboardTeam.class.getDeclaredField("d");
			team_suffix.setAccessible(true);
			team_players = PacketPlayOutScoreboardTeam.class.getDeclaredField("g");
			team_players.setAccessible(true);
			team_color = PacketPlayOutScoreboardTeam.class.getDeclaredField("f");
			team_color.setAccessible(true);
			team_mode = PacketPlayOutScoreboardTeam.class.getDeclaredField("h");
			team_mode.setAccessible(true);
			team_nametag = PacketPlayOutScoreboardTeam.class.getDeclaredField("e");
			team_nametag.setAccessible(true);

			network_channel = NetworkManager.class.getDeclaredField("channel");
			network_channel.setAccessible(true);

			propertymap = PropertyMap.class.getDeclaredField("properties");
			propertymap.setAccessible(true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * You can set the default head for the players in the tab.
	 * Warning due to mojang rate limit this can only be accessed once every minute.
	 * so basically if you restart the server make sure it was a minute before the last start.
	 * Will be a good idea if you turn this off for testing plugins or if you need to restart often.
	 *
	 * @param head  the uuid of the players head you want
	 * @return      this
	 */
	public TabList setHead(UUID head) {
		try {
			URL url = HttpAuthenticationService
					.constantURL("https://sessionserver.mojang.com/session/minecraft/profile/"
							+ UUIDTypeAdapter.fromUUID(head) + "?unsigned=false");

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();

			if (connection.getResponseCode() == 429) {
				this.plugin.getLogger().severe("Tab List could not get the players head for the tab please wait 1 minute and restart");
				return this;
			}

			JSONObject obj = (JSONObject) new JSONParser().parse(new BufferedReader(new InputStreamReader(connection.getInputStream())));
			JSONObject props = (JSONObject) ((JSONArray) obj.get("properties")).get(0);
			this.head = new Property("textures", props.get("value").toString(), props.get("signature").toString());
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return this;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	private void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();

		if (qLib.getInstance().getTabHandler() != null) {
			qLib.getInstance().getTabHandler().removePlayer(player.getUniqueId());
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	private void onLogin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		HashMap<Integer, Property> heads = new HashMap<>();

		for (Table.Cell<Integer, Integer, Property> cell : provider.getHeads(player).cellSet()) {
			heads.put(rowColumnToSlot(cell.getRowKey(), cell.getColumnKey()), cell.getValue());
		}

		setupBoard(((CraftPlayer) player).getHandle(), heads);

		try {
			PacketPlayOutScoreboardTeam a = new PacketPlayOutScoreboardTeam();
			team_mode.set(a, 3);
			team_name.set(a, "qLib");
			team_display.set(a, "qLib");
			team_color.set(a, -1);
			team_players.set(a, Arrays.asList(player.getName()));

			for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
				((CraftPlayer) onlinePlayer).getHandle().playerConnection.sendPacket(a);
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	private void setupBoard(EntityPlayer player, HashMap<Integer, Property> heads) {
		PacketPlayOutPlayerInfo player_packet = new PacketPlayOutPlayerInfo();;
		PacketPlayOutScoreboardTeam t;
		GameProfile profile;

		try {
			PacketPlayOutPlayerInfo info = new PacketPlayOutPlayerInfo();
			List<PacketPlayOutPlayerInfo.PlayerInfoData> players_in_packet = (List<PacketPlayOutPlayerInfo.PlayerInfoData>) info_profile_list.get(player_packet);
			info_action.set(player_packet, PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER);
			for (int r = 0; r < 20; r++) {//slot 0-59 for 1.7 players
				for (int c = 0; c < 3l; c++) {
					int slot = rowColumnToSlot(r, c);
					String name = playerName(slot);

					profile = new GameProfile(UUID.randomUUID(), name);

					Property customHead = heads.get(slot);

					Multimap<String, Property> prop = (Multimap<String, Property>) propertymap.get(profile.getProperties());
					if (customHead != null) {
						prop.put("textures", customHead);
					} else if (head != null) {
						prop.put("textures", head);
					}

					players_in_packet.add(info.new PlayerInfoData(profile, 0, WorldSettings.EnumGamemode.SURVIVAL, null));
				}
			}
			for (int i = 60; i < 80; i++) {//slot 60-79 for 1.8 players
				String name = playerName(i);
				profile = new GameProfile(UUID.randomUUID(), name);

				Property customHead = heads.get(i);

				Multimap<String, Property> prop = (Multimap<String, Property>) propertymap.get(profile.getProperties());
				if (customHead != null) {
					prop.put("textures", customHead);
				} else if (head != null) {
					prop.put("textures", head);
				}

				players_in_packet.add(info.new PlayerInfoData(profile, 0, WorldSettings.EnumGamemode.SURVIVAL, null));
			}

			player.playerConnection.sendPacket(player_packet);

			t = new PacketPlayOutScoreboardTeam();
			team_name.set(t, "qLib");
			team_display.set(t, "qLib");
			team_mode.set(t, 0);
			team_color.set(t, -1);
			team_nametag.set(t, "always");
			Collection<String> players = new ArrayList<>();
			for (Player other : Bukkit.getOnlinePlayers()) {
				players.add(other.getName());
			}
			team_players.set(t, players);
			player.playerConnection.sendPacket(t);

			for (int r = 0; r < 20; r++) {
				for (int c = 0; c < 4; c++) {
					String name = playerName(rowColumnToSlot(r, c));
					String teamName = "$" + name;
					t = new PacketPlayOutScoreboardTeam();
					team_name.set(t, teamName);
					team_display.set(t, teamName);
					team_mode.set(t, 0);
					team_color.set(t, -1);
					team_nametag.set(t, "always");
					team_players.set(t, Arrays.asList(name));
					player.playerConnection.sendPacket(t);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.plugin.getLogger().warning("Error setting up player board for " + player.getName());
		}
	}

	private int rowColumnToSlot(int r, int c) {
		return r + c * 20;
	}

	private String playerName(int slot) {
		return ChatColor.BOLD + ChatColor.GREEN.toString() + ChatColor.UNDERLINE +
				ChatColor.YELLOW +
				(slot >= 10 ? ChatColor.COLOR_CHAR + String.valueOf(slot / 10) +
						ChatColor.COLOR_CHAR + slot % 10
						: ChatColor.BLACK.toString() +
						ChatColor.COLOR_CHAR + slot) + ChatColor.RESET;
	}

	public void updateSlot(Player player, int row, int column, String value) {
		if (row > 19) {
			throw new RuntimeException("Row is above 19 " + row);
		}
		if (column > 4) {
			throw new RuntimeException("Column is above 4 " + column);
		}
		String prefix = value;
		String suffix = "";
		if (value.length() > 16) {
			prefix = value.substring(0, 16);
			suffix = ChatColor.getLastColors(prefix) + value.substring(16);
			if (suffix.length() > 16) {
				suffix = suffix.substring(0, 16);
			}
		}
		String teamName = "$" + playerName(rowColumnToSlot(row, column));
		PacketPlayOutScoreboardTeam t = new PacketPlayOutScoreboardTeam();
		try {
			team_name.set(t, teamName);
			team_display.set(t, teamName);
			team_prefix.set(t, prefix);
			team_suffix.set(t, suffix);
			team_mode.set(t, 2);
			team_nametag.set(t, "always");
			team_color.set(t, -1);
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(t);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			this.plugin.getLogger().warning(String.format("Error setting slot %s, %d, %d, %s", player.getName(), row,
					column, value));
		}
	}


	@Override
	public void handleSentPacket(PlayerConnection playerConnection, Packet packet) {
		if (packet instanceof PacketPlayOutScoreboardTeam) {
			PacketPlayOutScoreboardTeam p = (PacketPlayOutScoreboardTeam) packet;
			try {
				int mode = team_mode.getInt(p);
				String teamname = (String) team_name.get(p);
				/*
				 * if the packet is a remove player packet and the team isn't qLib this happens
				 * if a plugin is removing a player from a team and not adding it to another we
				 * need to check this for 1.8 players consistency as players not in a team will
				 * be displayed first on the scoreboard in front of all of our slots we set the
				 * mode of the packet to a add_player packet and change the team name and
				 * display name to our team "qLib" the only time a player would be removed from
				 * qLib is if we actually did it so dont check for qLib also changing it to a
				 * addplayer packet if it was qLib would mean adding a player to a team they
				 * are already in which would crash 1.7 players
				 */
				if (mode == 4 && !teamname.equals("qLib")) {
					team_mode.set(p, 3);
					team_name.set(p, "qLib");
					team_display.set(p, "qLib");
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void handleReceivedPacket(PlayerConnection connection, Packet packet) {
	}

}