package dev.lugami.uhub.util;

import org.apache.commons.lang3.StringEscapeUtils;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class Style {

	public static String API_FAILED =
			ChatColor.RED.toString() + "The API failed to retrieve your information. Try again later.";
	public static String BLUE = ChatColor.BLUE.toString();
	public static String AQUA = ChatColor.AQUA.toString();
	public static String YELLOW = ChatColor.YELLOW.toString();
	public static String RED = ChatColor.RED.toString();
	public static String GRAY = ChatColor.GRAY.toString();
	public static String GOLD = ChatColor.GOLD.toString();
	public static String GREEN = ChatColor.GREEN.toString();
	public static String WHITE = ChatColor.WHITE.toString();
	public static String BLACK = ChatColor.BLACK.toString();
	public static String BOLD = ChatColor.BOLD.toString();
	public static String ITALIC = ChatColor.ITALIC.toString();
	public static String UNDER_LINE = ChatColor.UNDERLINE.toString();
	public static String STRIKE_THROUGH = ChatColor.STRIKETHROUGH.toString();
	public static String RESET = ChatColor.RESET.toString();
	public static String MAGIC = ChatColor.MAGIC.toString();
	public static String DARK_BLUE = ChatColor.DARK_BLUE.toString();
	public static String DARK_AQUA = ChatColor.DARK_AQUA.toString();
	public static String DARK_GRAY = ChatColor.DARK_GRAY.toString();
	public static String DARK_GREEN = ChatColor.DARK_GREEN.toString();
	public static String DARK_PURPLE = ChatColor.DARK_PURPLE.toString();
	public static String DARK_RED = ChatColor.DARK_RED.toString();
	public static String PINK = ChatColor.LIGHT_PURPLE.toString();
	public static String BLANK_LINE = "§a §b §c §d §e §f §0 §r";
	public static String BORDER_LINE_SCOREBOARD = Style.GRAY + Style.STRIKE_THROUGH + "----------------------";
	public static String UNICODE_VERTICAL_BAR = Style.GRAY + StringEscapeUtils.unescapeJava("\u2503");
	public static String UNICODE_CAUTION = StringEscapeUtils.unescapeJava("\u26a0");
	public static String UNICODE_ARROW_LEFT = StringEscapeUtils.unescapeJava("\u25C0");
	public static String UNICODE_ARROW_RIGHT = StringEscapeUtils.unescapeJava("\u25B6");
	public static String UNICODE_ARROWS_LEFT = StringEscapeUtils.unescapeJava("\u00AB");
	public static String UNICODE_ARROWS_RIGHT = StringEscapeUtils.unescapeJava("\u00BB");
	public static String UNICODE_HEART = StringEscapeUtils.unescapeJava("\u2764");
	//private static FontRenderer FONT_RENDERER = new FontRenderer();
	private static String MAX_LENGTH = "11111111111111111111111111111111111111111111111111111";

	private Style() {
		throw new RuntimeException("Cannot instantiate a utility class.");
	}

	public static String strip(String in) {
		return ChatColor.stripColor(translate(in));
	}

	public static String translate(String in) {
		return ChatColor.translateAlternateColorCodes('&', in);
	}

	public static List<String> translateLines(List<String> lines) {
		List<String> toReturn = new ArrayList<>();

		for (String line : lines) {
			toReturn.add(ChatColor.translateAlternateColorCodes('&', line));
		}

		return toReturn;
	}

}
