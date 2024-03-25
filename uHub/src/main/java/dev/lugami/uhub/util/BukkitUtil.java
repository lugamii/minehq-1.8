package dev.lugami.uhub.util;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;

public class BukkitUtil {

	/**
	 * Registers all listeners from the given package with the given plugin.
	 *
	 * @param plugin      The plugin responsible for these listeners. This is here because the .getClassesInPackage
	 *                    method requires it (for no real reason)
	 * @param packageName The package to load listeners from. Example: ""
	 */
	public static void loadListenersFromPackage(Plugin plugin, String packageName) {
		for (Class<?> clazz : ClassUtil.getClassesInPackage(plugin.getClass(), packageName)) {
			if (isListener(clazz)) {
				try {
					plugin.getServer().getPluginManager().registerEvents((Listener) clazz.newInstance(), plugin);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Check if the given class implements the {@link Listener} interface.
	 *
	 * @param clazz The class to check
	 *
	 * @return If the class implements the {@link Listener} interface
	 */
	public static boolean isListener(Class<?> clazz) {
		for (Class<?> interfaze : clazz.getInterfaces()) {
			if (interfaze == Listener.class) {
				return true;
			}
		}

		return false;
	}

	public static String getName(PotionEffectType potionEffectType) {
		if (potionEffectType.getName().equalsIgnoreCase("fire_resistance")) {
			return "Fire Resistance";
		} else if (potionEffectType.getName().equalsIgnoreCase("speed")) {
			return "Speed";
		} else if (potionEffectType.getName().equalsIgnoreCase("weakness")) {
			return "Weakness";
		} else if (potionEffectType.getName().equalsIgnoreCase("slowness")) {
			return "Slowness";
		} else {
			return "Unknown";
		}
	}

	public static Player getDamager(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			return (Player) event.getDamager();
		} else if (event.getDamager() instanceof Projectile) {
			if (((Projectile) event.getDamager()).getShooter() instanceof Player) {
				return (Player) ((Projectile) event.getDamager()).getShooter();
			}
		}

		return null;
	}

}
