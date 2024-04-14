package org.gamcio.mobmagnet.addons;

import io.lumine.mythic.core.skills.stats.Stats;
import io.lumine.mythic.lib.api.event.MMOPlayerDataEvent;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.api.stat.StatMap;
import io.lumine.mythic.lib.api.stat.modifier.StatModifier;
import io.lumine.mythic.lib.player.modifier.ModifierType;
import io.lumine.mythic.lib.player.skill.PassiveSkill;
import io.lumine.mythic.lib.player.skill.PassiveSkillMap;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.handler.SkillHandler;
import io.lumine.mythic.lib.skill.result.SkillResult;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class MythicLib implements Listener {

    public static boolean MythicLibEnable = false;
    public static Map<String, String> mythicLib_MobAttributes = new HashMap<String, String>();

    public static void Add_Attribute(String entityName, double value, Player player){
        //if(!MythicLibEnable) return;

        MMOPlayerData playerData = MMOPlayerData.get(player.getUniqueId());
        StatMap statMap = playerData.getStatMap();


        Bukkit.broadcastMessage(String.valueOf(statMap.getStat("strength")));

    }
    public static void Remove_Attribute(String entityName, double value, Player player){
        if(!MythicLibEnable) return;

    }

}
