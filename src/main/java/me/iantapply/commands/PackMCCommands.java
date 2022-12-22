package me.iantapply.commands;

import me.iantapply.PackMC;
import me.iantapply.states.ResourcePackState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class PackMCCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Check if the length of the command is greater than 1 (aka "/packmc")
        if (args.length > 0) {

            // If the command is "setpack" move forward
            if (args[0].equals("setpack")) {
                // If the sender has sufficient permissions
                if (sender.hasPermission("packmc.setpack")) {
                    // If there are 3 arguments in the command...
                    if (args.length == 4) {

                        // Check if the world they specified exists
                        if (Bukkit.getWorld(args[1]) == null) {
                            sender.sendMessage(ChatColor.RED + "There is no world with that name! Please specify a world that is on this server.");
                        }

                        // If it exists, try to set the resource pack for that world in the world-data.yml file
                        try {
                            // Set the pack and send a message to the player
                            ResourcePackState.setResourcePack(args[1], args[2], Boolean.valueOf(args[3]));
                            sender.sendMessage(ChatColor.BLUE + "Successfully set the resource pack for the specified world!\n" + ChatColor.GREEN + "The resource pack will not apply until the server is restarted.");
                        } catch (IOException e) {
                            // If there's an exception, catch it and send a message to the player
                            sender.sendMessage(ChatColor.RED + "An error has occurred while setting the pack! Please refer to the logs for more info!");
                            throw new RuntimeException(e);
                        } catch (NoSuchAlgorithmException e) {
                            throw new RuntimeException(e);
                        }
                    // Send a not enough arguments message to the player
                    } else {
                        sender.sendMessage(ChatColor.RED + "You should use /packmc setpack <world name> <resource pack ID> <force pack?>");
                    }
                // Send a message to the player to say they don't have permission
                } else {
                    sender.sendMessage(ChatColor.DARK_RED + "You don't have access to this command");
                }

            // If the command is "status"...
            } else if (args[0].equals("status")) {
                // If they have sufficient permissions move forward
                if (sender.hasPermission("packmc.status")) {
                    sender.sendMessage("this worked");

                // Send a message to them if they don't have permission
                } else {
                    sender.sendMessage(ChatColor.DARK_RED + "You don't have access to this command");
                }

            // If the command is "reload-config"...
            } else if (args[0].equals("reload-config")) {
                // If they have sufficient permissions go ahead and execute the command
                if (sender.hasPermission("packmc.reload-config")) {
                    // Reload config and send a message to the player
                    PackMC.getPlugin(PackMC.class).reloadConfig();
                    sender.sendMessage(ChatColor.GREEN + "Config should have been reloaded!");
                // If they don't have permission send a message to them
                } else {
                    sender.sendMessage(ChatColor.DARK_RED + "You don't have access to this command");
                }

            // If they haven't specified the command just return the process
            } else {
                return false;
            }
            return true;
        // If the length of the command is less than 1 just return the process
        } else {
            return false;
        }
    }
}
