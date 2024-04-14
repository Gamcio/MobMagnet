package org.gamcio.mobmagnet.Event;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.gamcio.mobmagnet.SoulItemInventory;
import org.gamcio.mobmagnet.particles.RingParticles;

import java.util.Objects;

import static org.bukkit.Bukkit.getLogger;
import static org.gamcio.mobmagnet.MobMagnet.*;
import static org.gamcio.mobmagnet.SoulItemInventory.*;
import static org.gamcio.mobmagnet.commands.GiveSoulItem.getEntityByName;
import static org.gamcio.mobmagnet.utility.SoulItem.*;
import static org.gamcio.mobmagnet.utility.SoulItem.soulAttributes;

public class InventoryEvent implements Listener {

    public static Sound addItem_sound;
    public static Sound removeItem_sound;
    public static Sound buyItem_sound;
    public static Sound cantBuyItem_sound;


    @EventHandler
    public void openSoulInventory(InventoryOpenEvent event){
        if(!event.getView().getTitle().equals(ChatColor.GOLD + langData.get("Soul_inventory"))) return;

        Player player = (Player) event.getPlayer();

        player.playSound(player.getLocation(), Sound.BLOCK_BARREL_OPEN,3.0F, 0.5F);

        for(int i = 0; i < soulSlot.length; i++){
            if(event.getInventory().getItem(soulSlot[i]) == null){

            }else if(Objects.requireNonNull(Objects.requireNonNull(event.getInventory().getItem(soulSlot[i])).getItemMeta()).getDisplayName().equals(ChatColor.WHITE +"You can put where soul")){
                ItemStack item = event.getInventory().getItem(soulSlot[i]);
                event.getInventory().removeItem(item);
                return;
            }
        }
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();

        boolean buy = buySlot(event);
        if(buy) {
            player.playSound(player.getLocation(), buyItem_sound,3.0F, 0.5F);
            event.setCancelled(true);
            return;
        }

        boolean itemIsRemove = removeItem(event);
        if(itemIsRemove){
            player.playSound(player.getLocation(), removeItem_sound,3.0F, 0.5F);
            event.setCancelled(true);
            return;
        }

        boolean itemIsAdd = addItem(event);
        if(itemIsAdd){
            player.playSound(player.getLocation(), addItem_sound, 3.0F, 0.5F);
            event.setCancelled(true);
            return;
        }

        //player.playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 3.0F, 0.5F);
    }

    @EventHandler
    public void onGUIClose(InventoryCloseEvent event){
        if(!event.getView().getTitle().contains(ChatColor.GOLD + langData.get("Soul_inventory"))) return;

        Player player = (Player) event.getPlayer();

        player.playSound(player.getLocation(), Sound.BLOCK_BARREL_CLOSE,3.0F, 0.5F);

        RingParticles.ringCreate(player, event.getInventory().getContents());
    }

    @EventHandler
    public void onJoinServer(PlayerJoinEvent event){
        Player player = event.getPlayer();

        if(!soulSlot_item.containsKey(player.getUniqueId().toString())){

            boolean[] emptySlot = new boolean[] { true, true, true, true, true, true, true, true, true };
            boolean[] isUnlock = new boolean[] { false, false, false, false, false, false, false, false, false };
            ItemStack[] itemStacks = new ItemStack[] { null, null, null, null, null, null, null, null, null };

            for(int i = 0; i < 9; i++){
                if(soulSlot_prize[i] == 0){
                    isUnlock[i] = true;
                }
            }

            soulSlot_item.put(player.getUniqueId().toString(), itemStacks);
            soulSlot_isEmpty.put(player.getUniqueId().toString(), emptySlot);
            soulSlot_slotUnlock.put(player.getUniqueId().toString(), isUnlock);

        }

        ItemStack[] itemStacks = soulSlot_item.get(player.getUniqueId().toString());

        RingParticles.ringCreate(player, itemStacks);
    }

    public boolean buySlot(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();

        if(!event.getView().getTitle().equals(ChatColor.GOLD + langData.get("Soul_inventory"))) return false;

        if(event.getCurrentItem() == null) {
            event.setCancelled(true);
            return false;
        }

        if(!event.getCurrentItem().hasItemMeta()) {
            event.setCancelled(true);
            return false;
        }

        if(!Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getPersistentDataContainer().has(inventory_slot)) {
            event.setCancelled(true);
            return false;
        }


        if(Objects.requireNonNull(Objects.requireNonNull(event.getCurrentItem()).getItemMeta()).getPersistentDataContainer().has(inventory_slot)){
            int slotNumber = event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(inventory_slot, PersistentDataType.INTEGER);
            boolean[] slotIsUnlock = soulSlot_slotUnlock.get(player.getUniqueId().toString());
            double prize = soulSlot_prize[slotNumber];
            String prizeType = soulSlot_prizeType[slotNumber];

            int number = slotNumber - 1;
            if(number < 0) number = 0;

            if(slotIsUnlock[number]){
                if(!slotIsUnlock[slotNumber]){

                    boolean slotUnlock = unLockSlot(player, prizeType, prize);

                    if(slotUnlock){
                        slotIsUnlock[slotNumber] = true;
                        soulSlot_slotUnlock.put(player.getUniqueId().toString(), slotIsUnlock);

                        player.closeInventory();
                        Inventory inv = SoulInventory(player);
                        player.openInventory(inv);

                        return true;
                    }else{
                        player.playSound(player.getLocation(), cantBuyItem_sound, 3.0F, 0.5F);
                    }
                }
            }else{
                player.playSound(player.getLocation(), cantBuyItem_sound, 3.0F, 0.5F);
            }
        }

        return false;
    }

    private boolean unLockSlot(Player player, String prizeType, double prize) {

        if(prizeType.equalsIgnoreCase("LVL")){
            int level = player.getLevel();
            if(level >= prize){
                player.setLevel(level - (int) prize);
                return true;
            }else{
                return false;
            }
        }

        if(prizeType.equalsIgnoreCase("ECO") && econ != null){
            if(econ.has(player, prize)){
                econ.withdrawPlayer(player, prize);
                return true;
            }else{
                return false;
            }
        }else if(econ == null){
            System.out.println(langData.get("plugin_prefix") + " Not found economy plugin");
        }

        return false;
    }

    public boolean removeItem(InventoryClickEvent event){
        if(!event.getView().getTitle().equals(ChatColor.GOLD + langData.get("Soul_inventory"))) return false;

        if(event.getCurrentItem() == null) {
            event.setCancelled(true);
            return false;
        }

        if(!event.getCurrentItem().hasItemMeta()) {
            event.setCancelled(true);
            return false;
        }

        if(Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getPersistentDataContainer().has(inventory_slot)) {
            event.setCancelled(true);
            return false;
        }

        Player player = (Player) event.getWhoClicked();

        for(int i = 0; i < event.getInventory().getContents().length; i++){
            ItemStack[] inventory = event.getInventory().getContents();
            if(inventory[i] != null)
                if(inventory[i].equals(event.getCurrentItem())){

                    // player want remove

                    ItemStack itemRemove = event.getCurrentItem();

                    if(playerInvIsFull(player)){
                        player.getInventory().addItem(itemRemove);
                    }else{
                        player.getWorld().dropItem(player.getLocation(), itemRemove);
                    }

                    boolean[] emptySlot = soulSlot_isEmpty.get(player.getUniqueId().toString());
                    ItemStack[] items = soulSlot_item.get(player.getUniqueId().toString());
                    items[i] = null;
                    emptySlot[i] = true;
                    soulSlot_isEmpty.put(player.getUniqueId().toString(), emptySlot);
                    soulSlot_item.put(player.getUniqueId().toString(), items);

                    String entity = itemRemove.getItemMeta().getPersistentDataContainer().get(soulMob, PersistentDataType.STRING);
                    assert entity != null;

                    removeAttribute(mobAttributes.get(entity), itemRemove.getItemMeta().getPersistentDataContainer().get(soulAttributes, PersistentDataType.DOUBLE), player);

                    event.getInventory().removeItem(itemRemove);

                    UpdateInventory(event.getInventory(), player);

                    return true;
                }
        }
        return false;
    }
    public boolean addItem(InventoryClickEvent event){
        if(!event.getView().getTitle().equals(ChatColor.GOLD + langData.get("Soul_inventory"))) return false;

        ItemStack itemClicked = event.getCurrentItem();

        if(event.getCurrentItem() == null) {
            event.setCancelled(true);
            return false;
        }

        if(!itemClicked.hasItemMeta()) {
            event.setCancelled(true);
            return false;
        }

        if(Objects.requireNonNull(itemClicked.getItemMeta()).getPersistentDataContainer().has(inventory_slot)) {
            event.setCancelled(true);
            return false;
        }

        Player player = (Player) event.getWhoClicked();

        for(ItemStack item : event.getInventory().getContents()){
            if(item != null)
                if(item.equals(event.getCurrentItem())){
                    event.setCancelled(true);
                    return false;
                }
        }

        for(int i = 0; i < 9; i++){
            if(soulSlot_isEmpty.get(player.getUniqueId().toString())[i]){
                if(soulSlot_slotUnlock.get(player.getUniqueId().toString())[i]){
                    if(event.getCurrentItem().getItemMeta().getPersistentDataContainer().has(soulKey)) {

                        ItemStack[] soulItems = returnSoulItems(event.getInventory().getContents());
                        int maxNumber = 0;
                        for(ItemStack item : soulItems){
                            if(maxNumber < item.getItemMeta().getPersistentDataContainer().get(soulKey, PersistentDataType.INTEGER)){
                                maxNumber = item.getItemMeta().getPersistentDataContainer().get(soulKey, PersistentDataType.INTEGER);
                            }
                        }

                        // want player add

                        ItemStack itemAdd = event.getCurrentItem();

                        int itemNumber = event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(soulKey, PersistentDataType.INTEGER);

                        if(itemNumber >= maxNumber) {

                            event.getInventory().setItem(soulSlot_id[i], itemAdd);

                            boolean[] emptySlot = soulSlot_isEmpty.get(player.getUniqueId().toString());
                            ItemStack[] itemStacks = soulSlot_item.get(player.getUniqueId().toString());
                            itemStacks[i] = itemAdd;
                            emptySlot[i] = false;

                            soulSlot_item.put(player.getUniqueId().toString(), itemStacks);
                            soulSlot_isEmpty.put(player.getUniqueId().toString(), emptySlot);


                            String entity = itemAdd.getItemMeta().getPersistentDataContainer().get(soulMob, PersistentDataType.STRING);
                            assert entity != null;

                            addAttribute(mobAttributes.get(entity), itemAdd.getItemMeta().getPersistentDataContainer().get(soulAttributes, PersistentDataType.DOUBLE), player);

                            player.getInventory().remove(itemAdd);

                            UpdateInventory(event.getInventory(), player);

                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

}
