package com.nerith.mc;

import com.nerith.mc.commands.NightVisionCommand;
import com.nerith.mc.commands.OffhandCommand;
import com.nerith.mc.listeners.PlayerListener;
import com.nerith.mc.managers.NightVisionManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class NerithMC extends JavaPlugin {

    private NightVisionManager nightVisionManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        nightVisionManager = new NightVisionManager(this);
        nightVisionManager.load();

        getCommand("nightvision").setExecutor(new NightVisionCommand(this));
        getCommand("offhand").setExecutor(new OffhandCommand(this));

        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        // Watchdog: reapplies night vision if another effect (such as milk) removes it
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Player player : getServer().getOnlinePlayers()) {
                nightVisionManager.apply(player);
            }
        }, 100L, 100L);

        getLogger().info("NerithMC enabled!");
    }

    @Override
    public void onDisable() {
        if (nightVisionManager != null) {
            nightVisionManager.save();
        }
        getLogger().info("NerithMC disabled!");
    }

    public NightVisionManager getNightVisionManager() {
        return nightVisionManager;
    }

    /** Sends a message from config.yml with the prefix and & color codes translated */
    public void sendMessage(CommandSender sender, String key) {
        String prefix = getConfig().getString("prefix", "");
        String message = getConfig().getString("messages." + key, key);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message));
    }
}
