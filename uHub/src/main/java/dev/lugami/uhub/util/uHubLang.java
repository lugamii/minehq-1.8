package dev.lugami.uhub.util;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;

public final class uHubLang {
    
    public static final String LEFT_ARROW = ChatColor.BLUE + ChatColor.BOLD.toString() + "» ";
    public static final String LEFT_ARROW_NAKED = "»";
    public static final String RIGHT_ARROW = " " + ChatColor.BLUE + ChatColor.BOLD.toString() + "»";
    public static final String RIGHT_ARROW_NAKED = "»";
    public static final String LONG_LINE = ChatColor.STRIKETHROUGH + StringUtils.repeat("-", 53);

    private uHubLang() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}