package org.gamcio.mobmagnet.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.gamcio.mobmagnet.utility.SoulItem;

import static org.gamcio.mobmagnet.MobMagnet.langData;

public class GiveSoulItem implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player p = (Player) commandSender;
        EntityType entityType = null;
        int random = 0;

        if(!p.hasPermission("mobmagnet.getsoul")) return false;

        if(strings.length == 1)
            if(getEntityByName(strings[0]) != null){
                entityType = getEntityByName(strings[0]);
            }else{
                random = Integer.parseInt(strings[0]);
            }

        if(strings.length == 2){
            if(getEntityByName(strings[1]) != null){
                entityType = getEntityByName(strings[1]);
            }else{
                Bukkit.broadcastMessage(ChatColor.RED + langData.get("plugin_prefix") + " " + langData.get("incorrect_entity_name"));
            }

            random = Integer.parseInt(strings[0]);
        }
        if(strings.length > 2) {
            return false;
        }

        ItemStack soulItem = SoulItem.createSoulItem(random, entityType);
        p.getInventory().addItem(soulItem);
        return true;
    }

    public static EntityType getEntityByName(String name) {
        for (EntityType type : EntityType.values()) {
            if(type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }
}
