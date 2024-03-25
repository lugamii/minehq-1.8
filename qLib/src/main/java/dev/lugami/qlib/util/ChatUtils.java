package dev.lugami.qlib.util;

import lombok.NonNull;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import org.bukkit.ChatColor;

/**
 * @author ImHacking
 * @date 5/22/2022
 */
public class ChatUtils {
    private static final char AMPERSAND = '&';

    private ChatUtils() {
        throw new AssertionError("Utility classes cannot be instantiated."); // seal
    }

    @NonNull
    public static String colorize(@NonNull String s) {
        return ChatColor.translateAlternateColorCodes(AMPERSAND, s);
    }

    @NonNull
    public static IChatBaseComponent colorizeBaseComponent(@NonNull String s) {
        return IChatBaseComponent.ChatSerializer.a(colorize(s));
    }

    @NonNull
    public static TextComponent colorizeTextComponent(@NonNull String s) {
        return new TextComponent(colorize(s));
    }
    @NonNull
    public static String fromBaseComponent(@NonNull IChatBaseComponent cbc) {
        return colorize(cbc.getText());
    }
}
