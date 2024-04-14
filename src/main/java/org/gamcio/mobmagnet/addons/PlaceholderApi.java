package org.gamcio.mobmagnet.addons;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.gamcio.mobmagnet.MobMagnet;
import org.jetbrains.annotations.NotNull;

import static org.gamcio.mobmagnet.SoulItemInventory.soulSlot_isEmpty;
import static org.gamcio.mobmagnet.SoulItemInventory.soulSlot_slotUnlock;

public class PlaceholderApi extends PlaceholderExpansion {

    private final MobMagnet plugin;

    public PlaceholderApi(MobMagnet plugin) {
        this.plugin = plugin;
    }

    @Override
    @NotNull
    public String getAuthor() {
        return "Gamcio";
    }

    @Override
    @NotNull
    public String getIdentifier() {
        return "MobMagnet";
    }

    @Override
    @NotNull
    public String getVersion() {
        return "1.5.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if(player == null) return null;

        if (params.equalsIgnoreCase("unlockSlot")) {
            if(!soulSlot_slotUnlock.containsKey(player.getUniqueId().toString())) return null;

            boolean[] slot = soulSlot_slotUnlock.get(player.getUniqueId().toString());

            int number = 0;

            for(boolean check : slot){
                if(check) number++;
            }

            String string = String.valueOf(number);

            return string;
        }

        if (params.equalsIgnoreCase("activeSlot")) {
            if(!soulSlot_isEmpty.containsKey(player.getUniqueId().toString())) return null;

            boolean[] slot = soulSlot_isEmpty.get(player.getUniqueId().toString());

            int number = 0;

            for(boolean check : slot){
                if(!check) number++;
            }

            String string = String.valueOf(number);

            return string;
        }

        return null; //
    }
}
