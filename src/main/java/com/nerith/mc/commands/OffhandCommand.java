package com.nerith.mc.commands;

import com.nerith.mc.NerithMC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public final class OffhandCommand implements CommandExecutor {

    private final NerithMC plugin;

    public OffhandCommand(NerithMC plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            plugin.sendMessage(sender, "players-only");
            return true;
        }

        if (!player.hasPermission("nerith.offhand")) {
            plugin.sendMessage(sender, "no-permission");
            return true;
        }

        PlayerInventory inv = player.getInventory();
        ItemStack mainHand = inv.getItemInMainHand();

        if (mainHand.getType().isAir()) {
            plugin.sendMessage(sender, "offhand-empty");
            return true;
        }

        ItemStack offHand = inv.getItemInOffHand();
        boolean offHandWasEmpty = offHand.getType().isAir();

        // Swap: the main hand item goes to the offhand, and the offhand item goes to the main hand
        inv.setItemInOffHand(mainHand);
        inv.setItemInMainHand(offHandWasEmpty ? null : offHand);

        plugin.sendMessage(sender, offHandWasEmpty ? "offhand-moved" : "offhand-swapped");
        return true;
    }
}
