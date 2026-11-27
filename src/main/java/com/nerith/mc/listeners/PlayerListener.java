package com.nerith.mc.listeners;

import com.nerith.mc.NerithMC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public final class PlayerListener implements Listener {

    private final NerithMC plugin;

    public PlayerListener(NerithMC plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        plugin.getNightVisionManager().apply(event.getPlayer());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        // الموت يمسح التأثيرات، لذلك نعيدها بعد tick واحد من الرسبون
        Bukkit.getScheduler().runTask(plugin, () -> plugin.getNightVisionManager().apply(player));
    }
}
