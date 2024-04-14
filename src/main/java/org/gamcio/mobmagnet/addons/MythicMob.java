package org.gamcio.mobmagnet.addons;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.gamcio.mobmagnet.utility.SoulItem;

import java.util.ArrayList;
import java.util.Objects;

import static org.gamcio.mobmagnet.MobMagnet.*;
import static org.gamcio.mobmagnet.MobMagnet.attributesData;
import static org.gamcio.mobmagnet.utility.SoulItem.*;

public class MythicMob implements Listener {
    public static boolean MythicMobEnable = false;

    @EventHandler
    public void onMobDie_Mythic(MythicMobDeathEvent event){
        if(!MythicMobEnable) return;

        if(mobAttributes.containsKey(event.getMob().getMobType())){

            ItemStack item = createSoulItem_MythicMob(0, event.getMob().getMobType());

            event.getDrops().add(item);
        }
    }

    public static ItemStack createSoulItem_MythicMob(int random, String entityName){
        ArrayList<String> lore = new ArrayList<String>();
        ItemStack soulItem = new ItemStack(Material.PLAYER_HEAD, 1);
        ItemMeta soulMeta = soulItem.getItemMeta();

        if (random == 0) random = soulRandomYear();

        assert soulMeta != null;
        soulMeta.setDisplayName(ChatColor.WHITE + entityName + langData.get("item_soul_name"));

        lore.add(SoulItem.getSoulColor(random) + langData.get("item_tier") + " " + getTier(random));
        lore.add(ChatColor.WHITE + "" + random + " " + langData.get("item_year"));
        lore.add("");
        lore.add(ChatColor.DARK_BLUE + langData.get("item_atributtes"));
        Bukkit.broadcastMessage(mobAttributes.get(entityName).toString());
        lore.add(ChatColor.BLUE + langData.get(mobAttributes.get(entityName).toString()) + " " + Objects.requireNonNull(getAttribute_MythicMob(random, soulMeta, entityName)).getPersistentDataContainer().get(soulAttributes, PersistentDataType.DOUBLE));
        soulMeta.setLore(lore);

        SkullMeta skullMeta = (SkullMeta) soulMeta;
        skullMeta.setOwnerProfile(getProfile(SoulItem.getLink(random)));

        soulMeta.getPersistentDataContainer().set(soulKey, PersistentDataType.INTEGER, random);

        soulMeta = getAttribute_MythicMob(random, soulMeta, entityName);
        soulMeta.getPersistentDataContainer().set(soulMob, PersistentDataType.STRING, entityName);

        soulItem.setItemMeta(soulMeta);
        soulItem.setItemMeta(skullMeta);

        return soulItem;
    }
    private static ItemMeta getAttribute_MythicMob(int number, ItemMeta itemMeta, String entityType){
        double data = 1;
        double maxValue = 1.0;
        float value = 1f;
        float minValue = 1f;

        if(attributesData.containsKey(entityType)){
            maxValue = attributesData.get(entityType);
        }


        value = (float) (maxValue / 7F);

        if(number < 100 ){ // Max 1
            value *= 1;
            minValue = value / 2f;
            data = ((value / 100) * number) + minValue;
        }else if(number < 1000 ){
            value *= 2;
            minValue = value / 2f;
            data = ((value / 1000) * number) + minValue;
        }else if(number < 10000 ){
            value *= 3;
            minValue = value / 2f;
            data = ((value / 10000) * number) + minValue;
        }else if(number < 100000 ){
            value *= 4;
            minValue = value / 2f;
            data = ((value / 100000) * number) + minValue;
        }else if(number < 200000 ){
            value *= 5;
            minValue = value / 2f;
            data = ((value / 200000) * number) + minValue;
        }else if(number < 1000000 ){
            value *= 6;
            minValue = value / 2f;
            data = ((value / 1000000f) * number) + minValue;
        }else if(number > 999999 ){
            value *= 7;
            minValue = value / 2f;
            float correctValue = (value / 2000000f) * number;
            if(correctValue >= minValue - (0.3 * minValue)){
                data = correctValue;
            }else{
                data = ((value / 2000000f) * number) + minValue;
            }

        }

        data *= 100;
        data = Math.round(data);
        data  /= 100;

        itemMeta.getPersistentDataContainer().set(soulAttributes, PersistentDataType.DOUBLE, data);

        return itemMeta;
    }

}
