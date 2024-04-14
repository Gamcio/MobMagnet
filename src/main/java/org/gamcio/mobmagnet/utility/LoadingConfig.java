package org.gamcio.mobmagnet.utility;

import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.gamcio.mobmagnet.MobMagnet;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.gamcio.mobmagnet.Event.MobDrop.DropChance;
import static org.gamcio.mobmagnet.MobMagnet.*;
import static org.gamcio.mobmagnet.SoulItemInventory.*;
import static org.gamcio.mobmagnet.addons.MythicMob.*;
import static org.gamcio.mobmagnet.addons.MythicLib.MythicLibEnable;
import static org.gamcio.mobmagnet.Event.InventoryEvent.*;

public class LoadingConfig {

    private static File playerFile;
    private static YamlConfiguration playerConfig;

    private static File settingsFile;
    private static YamlConfiguration settingsConfig;

    private static File langFile;
    private static YamlConfiguration langConfig;
    private static File mobDataFile;
    private static YamlConfiguration mobDataConfig;
    public static void loadFile(){

        System.out.println("[MobMagnet] LOADING...");

        playerFile = new File(MobMagnet.getProvidingPlugin(MobMagnet.class).getDataFolder(), "player.yml");
        if (!playerFile.exists()) {
            playerFile.getParentFile().mkdirs();
            MobMagnet.getProvidingPlugin(MobMagnet.class).saveResource("player.yml", false);
        }

        playerConfig = new YamlConfiguration();
        try {
            playerConfig.load(playerFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        settingsFile = new File(MobMagnet.getProvidingPlugin(MobMagnet.class).getDataFolder(), "settings.yml");
        if (!settingsFile.exists()) {
            settingsFile.getParentFile().mkdirs();
            MobMagnet.getProvidingPlugin(MobMagnet.class).saveResource("settings.yml", false);
        }

        settingsConfig = new YamlConfiguration();
        try {
            settingsConfig.load(settingsFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        langFile = new File(MobMagnet.getProvidingPlugin(MobMagnet.class).getDataFolder(), "language.yml");
        if (!langFile.exists()) {
            langFile.getParentFile().mkdirs();
            MobMagnet.getProvidingPlugin(MobMagnet.class).saveResource("language.yml", false);
        }

        langConfig = new YamlConfiguration();
        try {
            langConfig.load(langFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        mobDataFile = new File(MobMagnet.getProvidingPlugin(MobMagnet.class).getDataFolder(), "mobData.yml");
        if (!mobDataFile.exists()) {
            mobDataFile.getParentFile().mkdirs();
            MobMagnet.getProvidingPlugin(MobMagnet.class).saveResource("mobData.yml", false);
        }

        mobDataConfig = new YamlConfiguration();
        try {
            mobDataConfig.load(mobDataFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        if(mobDataConfig.getConfigurationSection("mobData") != null)
            Objects.requireNonNull(mobDataConfig.getConfigurationSection("mobData")).getKeys(false).forEach(key -> {
                Attribute content = Attribute.valueOf(Objects.requireNonNull(mobDataConfig.get("mobData." + key + ".type")).toString());
                double content2 = (double) Objects.requireNonNull(mobDataConfig.get("mobData." + key + ".max-value"));
                mobAttributes.put(key, content);
                attributesData.put(key, content2);
            });

        if(settingsConfig.getConfigurationSection("Addons") != null){
            MythicMobEnable = (boolean) settingsConfig.get("Addons.mythicMob");
            //MythicLibEnable = (boolean) settingsConfig.get("Addons.mythicLib");
        }
        if(settingsConfig.getConfigurationSection("item_settings") != null){
            DropChance = (int) settingsConfig.get("item_settings.item_chance_drop");
        }

        if(settingsConfig.getConfigurationSection("slot_settings") != null){
            double[] prize = new double[9];
            String[] pizeType = new String[9];

            for(int i = 0; i < 9; i++){
                prize[i] = (double) settingsConfig.get("slot_settings.slot_" + i + ".prize");
                pizeType[i] = (String) settingsConfig.get("slot_settings.slot_" + i + ".type");
            }

            soulSlot_prize = prize;
            soulSlot_prizeType = pizeType;
        }

        if(settingsConfig.getConfigurationSection("Menu_Settings") != null){
            addItem_sound = Sound.valueOf((String) settingsConfig.get("Menu_Settings.Sound.Add_item"));
            removeItem_sound = Sound.valueOf((String) settingsConfig.get("Menu_Settings.Sound.Remove_item"));
            buyItem_sound = Sound.valueOf((String) settingsConfig.get("Menu_Settings.Sound.Buy_Slot"));
            cantBuyItem_sound = Sound.valueOf((String) settingsConfig.get("Menu_Settings.Sound.Cant_Buy"));

        }

        if(langConfig.getConfigurationSection("language") != null){
            Objects.requireNonNull(langConfig.getConfigurationSection("language")).getKeys(false).forEach(key -> {
                if(langConfig.get("language." + key) == null){
                    System.out.println("[MobMagnet] Not found in language.yml key: " + key);
                    return;
                }
                String content = Objects.requireNonNull(langConfig.get("language." + key)).toString();

                langData.put(key, content);
            });
        }

        if(playerConfig.getConfigurationSection("Player") != null)
            Objects.requireNonNull(playerConfig.getConfigurationSection("Player")).getKeys(false).forEach(key -> {

                boolean[] empty = new boolean[9];
                ItemStack[] items = new ItemStack[9];
                boolean[] unlock = new boolean[9];

                for(int i = 0; i < 9; i++){
                      empty[i] = (boolean) playerConfig.get("Player." + key + ".Slot" + i + ".isEmpty");
                      items[i] = (ItemStack) playerConfig.get("Player." + key + ".Slot" + i + ".item");
                      unlock[i] = (boolean) playerConfig.get("Player." + key + ".Slot" + i + ".slotUnlock");
                }

                soulSlot_isEmpty.put(key, empty);
                soulSlot_item.put(key, items);
                soulSlot_slotUnlock.put(key,unlock);
            });

    }

    public static void saveFile(){
        System.out.println("[MobMagnet] SAVING...");

        for(int i = 0; i < 9; i++){
            for(String name : soulSlot_isEmpty.keySet()){
                playerConfig.set("Player." + name + ".Slot" + i + ".isEmpty", soulSlot_isEmpty.get(name)[i]);
            }
            for(String name : soulSlot_item.keySet()){
                playerConfig.set("Player." + name + ".Slot" + i + ".item", soulSlot_item.get(name)[i]);
            }
            for(String name : soulSlot_slotUnlock.keySet()){
                playerConfig.set("Player." + name + ".Slot" + i + ".slotUnlock", soulSlot_slotUnlock.get(name)[i]);
            }
        }

        try{
            playerConfig.save(playerFile);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static List<Boolean> ArrayToListBool(boolean[] array){
        List<Boolean> list = new ArrayList<Boolean>();

        for (boolean b : array) {
            list.add(b);
        }

        return list;
    }

    public static Attribute stringToAttribute(String name){
        Attribute attribute = null;

        attribute = Attribute.valueOf(name);

        return attribute;
    }


}
