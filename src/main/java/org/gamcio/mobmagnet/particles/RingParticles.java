package org.gamcio.mobmagnet.particles;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.gamcio.mobmagnet.MobMagnet;
import org.gamcio.mobmagnet.utility.SoulItem;

import java.util.ArrayList;
import java.util.List;

import static org.gamcio.mobmagnet.SoulItemInventory.taskID;
import static org.gamcio.mobmagnet.utility.SoulItem.soulKey;

public class RingParticles {

    public static void ringCreate(Player player, ItemStack[] items){
        List<Color> color = new ArrayList<Color>();

        int itemsNumber = 0;

        for(ItemStack item : items){
            if(item != null) {
                if (item.getItemMeta().getPersistentDataContainer().has(soulKey)) {
                    itemsNumber++;
                    color.add(SoulItem.getParticlesColor(item.getItemMeta().getPersistentDataContainer().get(soulKey, PersistentDataType.INTEGER)));
                }
            }
        }

        if(taskID.containsKey(player.getUniqueId())){
            Bukkit.getScheduler().cancelTask(taskID.get(player.getUniqueId()));

            taskID.remove(player.getUniqueId());
        }

        int finalItemsNumber = itemsNumber;

        int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(MobMagnet.getProvidingPlugin(MobMagnet.class), () -> summonCircle(player.getLocation(), player, finalItemsNumber, color),0, 1);

        taskID.put(player.getUniqueId(), task);
    }
    private static void summonCircle(Location location, Player p, int howManyCircle, List<Color> color) {
        for (int i = 0; i < howManyCircle; i++) {
            float height = i * 0.05F;
            float size = i * 0.2F + 0.4F;

            for (float d = 0; d <= 90; d += 0.5F) {
                if(i < 4) d += 0.5F;

                Location particleLoc = new Location(location.getWorld(), location.getX(), location.getY() + height, location.getZ());
                particleLoc.setX(location.getX() + Math.cos(d) * size);
                particleLoc.setZ(location.getZ() + Math.sin(d) * size);

                //p.spawnParticle(Particle.REDSTONE, particleLoc, 1, new Particle.DustOptions(color.get(i), 0.7F));
                p.getWorld().spawnParticle(Particle.REDSTONE, particleLoc, 1, new Particle.DustOptions(color.get(i), 0.4F));
            }
        }
    }
}
