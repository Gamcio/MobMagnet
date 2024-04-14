package org.gamcio.mobmagnet.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.gamcio.mobmagnet.SoulItemInventory;

import java.util.Objects;

import static org.gamcio.mobmagnet.MobMagnet.econ;
import static org.gamcio.mobmagnet.MobMagnet.langData;

public class MenuCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        Player p = null;
        Inventory inventory = null;

        if(sender instanceof Player){
            p = (Player) sender;
            if(!p.hasPermission("mobmagnet.menu")) return false;

            if(args.length > 0){
                return false;
            }
        }else{
            if(args.length > 0){
                return false;
            }
        }

        if(p != null){
            inventory = SoulItemInventory.SoulInventory(p);
        }else{
            System.out.println(langData.get("plugin_prefix") + langData.get("you_arent_player"));
            return false;
        }

        p.openInventory(inventory);
        return true;
    }
}
