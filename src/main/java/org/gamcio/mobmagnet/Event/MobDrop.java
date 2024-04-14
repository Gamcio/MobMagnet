package org.gamcio.mobmagnet.Event;

import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.gamcio.mobmagnet.utility.SoulItem;

import java.util.Random;

import static org.gamcio.mobmagnet.MobMagnet.mobAttributes;
import static org.gamcio.mobmagnet.addons.MythicLib.Add_Attribute;
import static org.gamcio.mobmagnet.addons.MythicMob.MythicMobEnable;

public class MobDrop implements Listener {

    public static int DropChance = 0;

    @EventHandler
    public void onMobDie(EntityDeathEvent event){
        if(!mobHaveDrop(event.getEntity().getName())) {
            return;
        }
        Add_Attribute(null, 0.0, event.getEntity().getKiller());
        if(event.getEntity().getKiller() == null) return;

        int random = new Random().nextInt(100) + 1;

        ItemStack itemSoul = SoulItem.createSoulItem(0, event.getEntityType());
        if(random <= DropChance)
            event.getDrops().add(itemSoul);
    }

    private boolean mobHaveDrop(String mobName){
        return mobAttributes.containsKey(mobName.toLowerCase());
    }

}
