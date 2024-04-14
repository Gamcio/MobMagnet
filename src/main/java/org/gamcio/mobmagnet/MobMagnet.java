package org.gamcio.mobmagnet;

import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.gamcio.mobmagnet.Event.InventoryEvent;
import org.gamcio.mobmagnet.Event.MobDrop;
import org.gamcio.mobmagnet.addons.MythicMob;
import org.gamcio.mobmagnet.addons.PlaceholderApi;
import org.gamcio.mobmagnet.commands.GiveSoulItem;
import org.gamcio.mobmagnet.commands.MenuCommand;
import org.gamcio.mobmagnet.utility.LoadingConfig;
import org.gamcio.mobmagnet.utility.SoulItem;

import java.util.*;

public final class MobMagnet extends JavaPlugin {
    public static Map<String, Attribute> mobAttributes = new HashMap<String, Attribute>();
    public static Map<String, Double> attributesData = new HashMap<String, Double>();
    public static Map<String, String> langData = new HashMap<String, String>();

    public static Economy econ = null;

    @Override
    public void onEnable() {
        LoadingConfig.loadFile();

        Metrics metrics = new Metrics(this, 21577);

        getServer().getPluginManager().registerEvents(new InventoryEvent(), this);
        getServer().getPluginManager().registerEvents(new MythicMob(), this);
        getServer().getPluginManager().registerEvents(new SoulItem(), this);
        getServer().getPluginManager().registerEvents(new MobDrop(), this);

        getCommand("soulmenu").setExecutor(new MenuCommand());
        getCommand("getsoul").setExecutor(new GiveSoulItem());

        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
        }

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) { //
            new PlaceholderApi(this).register(); //
        }else{
            getLogger().severe(String.format("[%s] - Not found PlaceholderAPI!", getDescription().getName()));
        }
    }
    @Override
    public void onDisable() {
        LoadingConfig.saveFile();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) {
            econ = economyProvider.getProvider();
        }

        return (econ != null);
    }

}
