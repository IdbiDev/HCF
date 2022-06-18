package me.idbi.hcf.tools;

import me.idbi.hcf.Elevator.ElevatorCreate;
import me.idbi.hcf.Elevator.ElevatorInteract;
import me.idbi.hcf.Main;
import me.idbi.hcf.SignShop.CreateShopSign;
import me.idbi.hcf.SignShop.InteractShopSign;
import me.idbi.hcf.commands.cmdFunctions.Faction_Home;
import me.idbi.hcf.events.Enchants.Anvil.AnvilEvent;
import me.idbi.hcf.events.Enchants.EnchantInventory;
import me.idbi.hcf.events.Enchants.TableEvent;
import me.idbi.hcf.events.Enchants.TableInteract;
import me.idbi.hcf.events.*;

import static org.bukkit.Bukkit.getServer;

public class setupEvents {
    public static void SetupEvents() {
        //xd
        getServer().getPluginManager().registerEvents(new onPlayerJoin(), Main.getPlugin(Main.class));

        getServer().getPluginManager().registerEvents(new onPlayerLeft(), Main.getPlugin(Main.class));

        getServer().getPluginManager().registerEvents(new onDamage(), Main.getPlugin(Main.class));

        getServer().getPluginManager().registerEvents(new onBlockBreak(), Main.getPlugin(Main.class));

        getServer().getPluginManager().registerEvents(new onBlockPlace(), Main.getPlugin(Main.class));

        getServer().getPluginManager().registerEvents(new onDeath(), Main.getPlugin(Main.class));

        getServer().getPluginManager().registerEvents(new onPlayerInteract(), Main.getPlugin(Main.class));

        getServer().getPluginManager().registerEvents(new onPlayerMove(), Main.getPlugin(Main.class));

        getServer().getPluginManager().registerEvents(new onPlayerRespawn(), Main.getPlugin(Main.class));

        getServer().getPluginManager().registerEvents(new onPlayerPreJoin(), Main.getPlugin(Main.class));

        getServer().getPluginManager().registerEvents(new onBowShoot(), Main.getPlugin(Main.class));

        getServer().getPluginManager().registerEvents(new Faction_Home(), Main.getPlugin(Main.class));

        getServer().getPluginManager().registerEvents(new InteractShopSign(), Main.getPlugin(Main.class));

        getServer().getPluginManager().registerEvents(new CreateShopSign(), Main.getPlugin(Main.class));

        getServer().getPluginManager().registerEvents(new ElevatorCreate(), Main.getPlugin(Main.class));

        getServer().getPluginManager().registerEvents(new ElevatorInteract(), Main.getPlugin(Main.class));

        getServer().getPluginManager().registerEvents(new onPlayerChat(), Main.getPlugin(Main.class));

        getServer().getPluginManager().registerEvents(new TableEvent(), Main.getPlugin(Main.class));

        getServer().getPluginManager().registerEvents(new onChunkLoaded(), Main.getPlugin(Main.class));

        getServer().getPluginManager().registerEvents(new EnchantInventory(), Main.getPlugin(Main.class));

        getServer().getPluginManager().registerEvents(new TableInteract(), Main.getPlugin(Main.class));

        getServer().getPluginManager().registerEvents(new AnvilEvent(), Main.getPlugin(Main.class));
    }
}
