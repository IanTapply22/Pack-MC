package me.iantapply.listeners;

import me.iantapply.PackMC;
import me.iantapply.states.ResourcePackState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class SendResourcePackOnWorldJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerChangedWorldEvent event) throws IOException, NoSuchAlgorithmException {
        if (PackMC.getWorldData().contains(event.getPlayer().getWorld().getName())) {
            String resourcePackURL = ResourcePackState.getResourcePackUrlForWorld(event.getPlayer().getWorld().getName());
            //byte[] binarySha1Hash = ResourcePackState.getbinarySha1Hash(event.getPlayer().getWorld().getName());

            event.getPlayer().setResourcePack(resourcePackURL);
        } else {
            return;
        }
    }
}
