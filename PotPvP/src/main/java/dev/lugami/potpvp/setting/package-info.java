/**
 * Handles accessing, saving, updating, and presentation of player settings.
 *
 * This includes the /settings command, a settings menu, persistence, etc.
 * Clients using the settings API should only concern themselves with {@link dev.lugami.potpvp.setting.event.SettingUpdateEvent},
 * {@link dev.lugami.potpvp.setting.SettingHandler#getSetting(java.util.UUID, dev.lugami.potpvp.setting.Setting)} and
 * {@link dev.lugami.potpvp.setting.SettingHandler#updateSetting(org.bukkit.entity.Player, dev.lugami.potpvp.setting.Setting, boolean)},
 */
package dev.lugami.potpvp.setting;