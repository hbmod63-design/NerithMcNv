package com.nerith.mc.managers;

import com.nerith.mc.NerithMC;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public final class NightVisionManager {

    private final NerithMC plugin;
    private final File file;
    private final Set<UUID> enabled = new HashSet<>();

    public NightVisionManager(NerithMC plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "nightvision.yml");
    }

    /** Loads the players who enabled night vision from the file */
    public void load() {
        if (!file.exists()) return;

        YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
        for (String raw : yml.getStringList("players")) {
            try {
                enabled.add(UUID.fromString(raw));
            } catch (IllegalArgumentException ignored) {
                // Ignore invalid values
            }
        }
    }

    /** Saves the players to the file so the setting survives server restarts */
    public void save() {
        plugin.getDataFolder().mkdirs();

        List<String> list = new ArrayList<>();
        for (UUID uuid : enabled) {
            list.add(uuid.toString());
        }

        YamlConfiguration yml = new YamlConfiguration();
        yml.set("players", list);

        try {
            yml.save(file);
        } catch (IOException e) {
            plugin.getLogger().warning("Could not save nightvision.yml: " + e.getMessage());
        }
    }

    /** Toggles the state. Returns true if it is now enabled. */
    public boolean toggle(Player player) {
        if (enabled.remove(player.getUniqueId())) {
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
            save();
            return false;
        }

        enabled.add(player.getUniqueId());
        apply(player);
        save();
        return true;
    }

    public boolean isEnabled(Player player) {
        return enabled.contains(player.getUniqueId());
    }

    /** Applies permanent night vision if the player is enabled and doesn't already have it */
    public void apply(Player player) {
        if (!enabled.contains(player.getUniqueId())) return;

        PotionEffect current = player.getPotionEffect(PotionEffectType.NIGHT_VISION);
        if (current != null && current.getDuration() == PotionEffect.INFINITE_DURATION) return;

        player.addPotionEffect(new PotionEffect(
                PotionEffectType.NIGHT_VISION,
                PotionEffect.INFINITE_DURATION,
                0,
                false, // ambient
                false, // particles
                true   // icon
        ));
    }
}
