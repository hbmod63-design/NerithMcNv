package com.nerith.mc.commands;

import com.nerith.mc.NerithMC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class NightVisionCommand implements CommandExecutor {

    private final NerithMC plugin;

    public NightVisionCommand(NerithMC plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            plugin.sendMessage(sender, "players-only");
            return true;
        }

        if (!player.hasPermission("nerith.nightvision")) {
            plugin.sendMessage(sender, "no-permission");
            return true;
        }

        boolean nowEnabled = plugin.getNightVisionManager().toggle(player);
        plugin.sendMessage(sender, nowEnabled ? "nightvision-on" : "nightvision-off");
        return true;
    }
}
