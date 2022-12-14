package me.iantapply.listeners;

import me.iantapply.PackMC;
import me.iantapply.states.ResourcePackState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class SendResourcePackOnJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws IOException, NoSuchAlgorithmException {
        if (PackMC.getWorldData().contains(event.getPlayer().getWorld().getName())) {
            String resourcePackURL = ResourcePackState.getResourcePackUrlForWorld(event.getPlayer().getWorld().getName());
            //byte[] binarySha1Hash = ResourcePackState.getbinarySha1Hash(event.getPlayer().getWorld().getName());

            event.getPlayer().setResourcePack(resourcePackURL);
        } else {
            return;
        }
    }
}
