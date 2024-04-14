package org.gamcio.mobmagnet.utility;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.gamcio.mobmagnet.MobMagnet;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static org.gamcio.mobmagnet.MobMagnet.*;

public class SoulItem implements Listener {

    public static NamespacedKey soulKey = new NamespacedKey(MobMagnet.getProvidingPlugin(MobMagnet.class), "year");
    public static NamespacedKey soulAttributes = new NamespacedKey(MobMagnet.getProvidingPlugin(MobMagnet.class), "attributes");
    public static NamespacedKey soulMob = new NamespacedKey(MobMagnet.getProvidingPlugin(MobMagnet.class), "mob");

    public static ItemStack createSoulItem(int random, EntityType entityType){
        ArrayList<String> lore = new ArrayList<String>();
        ItemStack soulItem = new ItemStack(Material.PLAYER_HEAD, 1);
        ItemMeta soulMeta = soulItem.getItemMeta();

        if (random == 0) random = soulRandomYear();

        assert soulMeta != null;
        soulMeta.setDisplayName(ChatColor.WHITE + entityType.name().toLowerCase() + langData.get("item_soul_name"));

        lore.add(SoulItem.getSoulColor(random) + langData.get("item_tier") + " " + getTier(random));
        lore.add(ChatColor.WHITE + "" + random + " " + langData.get("item_year"));
        lore.add("");
        lore.add(ChatColor.DARK_BLUE + langData.get("item_atributtes"));
        lore.add(ChatColor.BLUE + langData.get(mobAttributes.get(entityType.name().toLowerCase()).toString()) + " " + Objects.requireNonNull(getAttribute(random, soulMeta, entityType)).getPersistentDataContainer().get(soulAttributes, PersistentDataType.DOUBLE));
        soulMeta.setLore(lore);

        SkullMeta skullMeta = (SkullMeta) soulMeta;
        skullMeta.setOwnerProfile(getProfile(SoulItem.getLink(random)));

        soulMeta.getPersistentDataContainer().set(soulKey, PersistentDataType.INTEGER, random);

        soulMeta = getAttribute(random, soulMeta, entityType);
        soulMeta.getPersistentDataContainer().set(soulMob, PersistentDataType.STRING, entityType.name().toLowerCase());

        soulItem.setItemMeta(soulMeta);
        soulItem.setItemMeta(skullMeta);

        return soulItem;
    }

    public static String getTier(int number){
        String string = "";

        if(number < 100 ){
            string = "I";
        }else if(number < 1000 ){
            string = "II";
        }else if(number < 10000 ){
            string = "III";
        }else if(number < 100000){
            string = "IV";
        }else if(number < 200000 ){
            string = "V";
        }else if(number < 1000000 ){
            string = "VI";
        }else if(number > 999999 ){
            string = "VII";
        }

        return string;
    }

    public static int soulRandomYear(){
        int year = 0;
        int randomNumber = new Random().nextInt(10000) + 1;

        if(randomNumber < 1000) year = new Random().nextInt(1, 99) + 1; // 10 - 100 = 10%
        else if(randomNumber < 5000) year = new Random().nextInt(100, 999) + 1; // 100 - 1000 = 50%
        else if(randomNumber < 8989) year = new Random().nextInt(1000, 9999) + 1; // 1000 - 10 000 = 28.89%
        else if(randomNumber < 9889) year = new Random().nextInt(10000, 99999) + 1; // 10 000 - 100 000 = 10%
        else if(randomNumber < 9989) year = new Random().nextInt(100000, 199999) + 1; // 100 000 - 200 000 = 1%
        else if(randomNumber < 9999) year = new Random().nextInt(200000, 999999) + 1; // 200 000 - 1 000 000 = 0.1%
        else if(randomNumber > 9999) year = new Random().nextInt(999999, 2000000) + 1; // + 1 000 000 = 0.01%

        return year;

    }
    public static ChatColor getSoulColor(int number){

        ChatColor color = ChatColor.GRAY;

        if(number < 100 ){
            color = ChatColor.WHITE;
        }else if(number < 1000 ){
            color = ChatColor.YELLOW;
        }else if(number < 10000 ){
            color = ChatColor.DARK_PURPLE;
        }else if(number < 100000){
            color = ChatColor.BLACK;
        }else if(number < 200000 ){
            color = ChatColor.RED;
        }else if(number < 1000000 ){
            color = ChatColor.DARK_RED;
        }else if(number > 999999 ){
            color = ChatColor.GOLD;
        }

        return color;
    }
    public static String getLink(int number){

        String link = "https://textures.minecraft.net/texture/";

        if(number < 100 ){
            link += "e8cde67003722db818299bcbec883289d18dc9332de80983400a87f20544392a";
        } else if(number < 1000 ){
            link += "2c7667dbe26f607b699c9303e74f89510dac7543e0c92cef99b334549c57c48a";
        } else if(number < 10000 ){
            link += "cad8cc982786fb4d40b0b6e64a41f0d9736f9c26affb898f4a7faea88ccf8997";
        } else if(number < 100000 ){
            link += "369ec722d9cb6ed9bae93c266bd098f0a306c5e8c03849c27cdcf18c60d6cb3e";
        } else if(number < 200000 ){
            link += "c20ef06dd60499766ac8ce15d2bea41d2813fe55718864b52dc41cbaae1ea913";
        } else if(number < 1000000 ){
            link += "aace6bb3aa4ccac031168202f6d4532597bcac6351059abd9d10b28610493aeb";
        } else if(number > 999999 ){
            link += "c390c71cc947aabc05758f3c4994a31fb5821fd516f11b0d9e19e9c471a646f7";
        }

        return link;
    }
    public static Color getParticlesColor(int number){

        Color color = Color.GRAY;

        if(number < 100 ){
            color = Color.WHITE;
        }else if(number < 1000 ){
            color = Color.YELLOW;
        }else if(number < 10000 ){
            color = Color.fromRGB(135, 31,120);
        }else if(number < 100000 ){
            color = Color.BLACK;
        }else if(number < 200000 ){
            color = Color.RED;
        }else if(number < 1000000 ){
            color = Color.ORANGE;
        }else if(number > 1000000 ){
            color = Color.fromRGB(255,215,0);
        }

        return color;
    }
    public static ItemMeta getAttribute(int number, ItemMeta itemMeta, EntityType entityType){
        double data = 1;
        double maxValue = 1.0;
        float value = 1f;
        float minValue = 1f;

        if(attributesData.containsKey(entityType.name().toLowerCase())){
            maxValue = attributesData.get(entityType.name().toLowerCase());
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

    @EventHandler
    public void rightClickItem(PlayerInteractEvent event){
        Player player = (Player) event.getPlayer();

        if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            if(event.getItem() != null){
                if(event.getItem().getItemMeta().getPersistentDataContainer().has(soulKey)){
                    event.setCancelled(true);
                }
            }
        }
    }
    private static final UUID RANDOM_UUID = UUID.fromString("a18541ac-f7df-4b1e-9b98-e882cdad39b8"); // We reuse the same "random" UUID all the time
    public static PlayerProfile getProfile(String url) {
        PlayerProfile profile = Bukkit.createPlayerProfile(RANDOM_UUID); // Get a new player profile
        PlayerTextures textures = profile.getTextures();
        URL urlObject;
        try {
            urlObject = new URL(url); // The URL to the skin, for example: https://textures.minecraft.net/texture/18813764b2abc94ec3c3bc67b9147c21be850cdf996679703157f4555997ea63a
        } catch (MalformedURLException exception) {
            throw new RuntimeException("Invalid URL", exception);
        }
        textures.setSkin(urlObject); // Set the skin of the player profile to the URL
        profile.setTextures(textures); // Set the textures back to the profile
        return profile;
    }
}
