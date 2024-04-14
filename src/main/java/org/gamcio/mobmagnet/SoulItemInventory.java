package org.gamcio.mobmagnet;

import org.bukkit.attribute.*;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

import static org.gamcio.mobmagnet.MobMagnet.*;
import static org.gamcio.mobmagnet.commands.GiveSoulItem.getEntityByName;
import static org.gamcio.mobmagnet.utility.SoulItem.*;

public class SoulItemInventory implements Listener {
    public static Map<UUID, Integer> taskID = new HashMap<UUID, Integer>();
    public static int[] soulSlot = new int[]{ 0, 1, 2, 3, 4, 5, 6, 7, 8 };


    public static int[] soulSlot_id = new int[]{ 0, 1, 2, 3, 4, 5, 6, 7, 8 };
    public static double[] soulSlot_prize = new double[]{ 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0 };
    public static String[] soulSlot_prizeType = new String[]{ "LVL", "LVL", "LVL", "LVL", "LVL", "LVL", "LVL", "LVL", "LVL" };

    public static Map<String, boolean[]> soulSlot_isEmpty = new HashMap<String, boolean[]>();
    public static Map<String, ItemStack[]> soulSlot_item = new HashMap<String, ItemStack[]>();
    public static Map<String, boolean[]> soulSlot_slotUnlock = new HashMap<String, boolean[]>();
    public static NamespacedKey inventory_slot = new NamespacedKey(MobMagnet.getProvidingPlugin(MobMagnet.class), "slot");

    public static Inventory SoulInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 9, ChatColor.GOLD + langData.get("Soul_inventory"));
        String uuid = player.getUniqueId().toString();

        ItemStack fill = new ItemStack(Material.GREEN_STAINED_GLASS_PANE, 1);
        ItemMeta fillMeta = fill.getItemMeta();
        fillMeta.setDisplayName(ChatColor.WHITE + langData.get("unlock_slot"));
        fill.setItemMeta(fillMeta);

        ItemStack disable = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
        ItemMeta disableMeta = disable.getItemMeta();
        disableMeta.setDisplayName(ChatColor.RED + langData.get("lock_slot"));
        disable.setItemMeta(disableMeta);

        if(soulSlot_item.containsKey(uuid)){
            for(int i = 0; i < 9; i++){
                if(!soulSlot_isEmpty.get(uuid)[i]){
                    if(soulSlot_item.get(uuid)[i] != null){
                        inventory.setItem(soulSlot_id[i], soulSlot_item.get(uuid)[i]);
                    }
                }
            }
        }

        for(int i = 0; i < 9; i++){
            if(soulSlot_isEmpty.containsKey(uuid)) {
                if (soulSlot_isEmpty.get(uuid)[i]) {
                    if (!soulSlot_slotUnlock.get(uuid)[i]) {
                        ItemMeta meta = disable.getItemMeta();
                        meta.getPersistentDataContainer().set(inventory_slot, PersistentDataType.INTEGER, i);
                        List<String> loreList = new ArrayList<>();
                        loreList.add("");
                        loreList.add(ChatColor.WHITE + langData.get("prize_level") + " " + soulSlot_prize[i] + " " + returnPrizeTypeSymbol(soulSlot_prizeType[i]));
                        ItemStack item = disable;
                        meta.setLore(loreList);
                        item.setItemMeta(meta);

                        inventory.setItem(soulSlot_id[i], item);
                    } else {
                        ItemMeta meta = fill.getItemMeta();
                        meta.getPersistentDataContainer().set(inventory_slot, PersistentDataType.INTEGER, i);
                        ItemStack item = fill;
                        item.setItemMeta(meta);

                        inventory.setItem(soulSlot_id[i], item);
                    }
                }
            }
        }

        return  inventory;
    }

    private static String returnPrizeTypeSymbol(String type){
        String prizeSymbol = "";

        if(type.equalsIgnoreCase("LVL")){
            prizeSymbol = "Lvl";
        }
        if(type.equalsIgnoreCase("ECO") && econ != null){
            if(econ.isEnabled()){
                prizeSymbol = econ.currencyNameSingular();
            }else{
                prizeSymbol = "$";
            }
        }

        return prizeSymbol;
    }

    public static ItemStack[] returnSoulItems(ItemStack[] items){
        ItemStack[] newItems;
        List<ItemStack> itemsList = new ArrayList<ItemStack>();

        for(ItemStack item : items){
            if(item != null){
                if(Objects.requireNonNull(item.getItemMeta()).getPersistentDataContainer().has(soulKey)){
                    itemsList.add(item);
                }
            }
        }

        newItems = new ItemStack[itemsList.size()];
        itemsList.toArray(newItems);

        return newItems;
    }

    public static boolean playerInvIsFull(Player player){
        boolean isEmpty = false;

        if (player.getInventory().firstEmpty() > 0) {
            isEmpty = true;
        }

        return isEmpty;
    }

    public static void removeAttribute(Attribute attributeName, double value, Player player){
        double baseValue;

        baseValue = Objects.requireNonNull(player.getAttribute(attributeName)).getValue();
        Objects.requireNonNull(player.getAttribute(attributeName)).setBaseValue(baseValue - value);
    }
    public static void addAttribute(Attribute attributeName, double value, Player player){
        double baseValue;

        baseValue = Objects.requireNonNull(player.getAttribute(attributeName)).getValue();
        Objects.requireNonNull(player.getAttribute(attributeName)).setBaseValue(baseValue + value);

    }

    public static void UpdateInventory(Inventory inventory, Player player){

        String uuid = player.getUniqueId().toString();

        ItemStack fill = new ItemStack(Material.GREEN_STAINED_GLASS_PANE, 1);
        ItemMeta fillMeta = fill.getItemMeta();
        fillMeta.setDisplayName(ChatColor.WHITE + langData.get("unlock_slot"));
        fill.setItemMeta(fillMeta);

        ItemStack disable = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
        ItemMeta disableMeta = disable.getItemMeta();
        disableMeta.setDisplayName(ChatColor.RED + langData.get("lock_slot"));
        disable.setItemMeta(disableMeta);

        if(soulSlot_item.containsKey(uuid)){
            for(int i = 0; i < 9; i++){
                if(!soulSlot_isEmpty.get(uuid)[i]){
                    if(soulSlot_item.get(uuid)[i] != null){
                        inventory.setItem(soulSlot_id[i], soulSlot_item.get(uuid)[i]);
                    }
                }
            }
        }

        for(int i = 0; i < 9; i++){
            if(soulSlot_isEmpty.containsKey(uuid)) {
                if (soulSlot_isEmpty.get(uuid)[i]) {
                    if (!soulSlot_slotUnlock.get(uuid)[i]) {
                        ItemMeta meta = disable.getItemMeta();
                        meta.getPersistentDataContainer().set(inventory_slot, PersistentDataType.INTEGER, i);
                        List<String> loreList = new ArrayList<>();
                        loreList.add("");
                        loreList.add(ChatColor.WHITE + langData.get("prize_level") + " " + soulSlot_prize[i] + " " + returnPrizeTypeSymbol(soulSlot_prizeType[i]));
                        ItemStack item = disable;
                        meta.setLore(loreList);
                        item.setItemMeta(meta);

                        inventory.setItem(soulSlot_id[i], item);
                    } else {
                        ItemMeta meta = fill.getItemMeta();
                        meta.getPersistentDataContainer().set(inventory_slot, PersistentDataType.INTEGER, i);
                        ItemStack item = fill;
                        item.setItemMeta(meta);

                        inventory.setItem(soulSlot_id[i], item);
                    }
                }
            }
        }
    }

}
