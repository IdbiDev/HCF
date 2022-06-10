package me.idbi.hcf.events;

import me.idbi.hcf.Main;
import me.idbi.hcf.tools.Banhandler;
import me.idbi.hcf.tools.SQL_Connection;
import me.idbi.hcf.tools.playertools;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.Connection;
import java.util.ArrayList;

import static me.idbi.hcf.Main.death_time;


public class onDeath implements Listener {
    private final Connection con = Main.getConnection("events.onDeath");

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        //  Victim meghalt damager által! [Weapon]
        //  Példa: BauBence meghalt Macska0121 által! [POTION.POISON]

        //Some constants

        Player damager = e.getEntity().getKiller();
        Player victim = e.getEntity().getPlayer();
        if (damager != null) {
            e.setDeathMessage(Main.servername + ChatColor.RED + victim.getDisplayName() + " meghalt" + damager.getDisplayName() + " által! [" + damager.getItemInHand().getType().name() + "]");
        } else {
            e.setDeathMessage(Main.servername + ChatColor.RED + victim.getDisplayName() + " Meghalt");
        }

        if (!Main.deathban) {
            victim.kickPlayer("Meghaltál, de szerencséd van mert nincs deathban :P");
        } else {
            Banhandler.banPlayerInHCF(victim);
        }
        Main.Faction faction = Main.faction_cache.get(Integer.parseInt(playertools.getMetadata(victim, "factionid")));
        faction.BroadcastFaction("[§4-§f] §3" + victim.getDisplayName() + " &4 meghalt!");

        if (!playertools.getMetadata(victim, "factionid").equals("0")) {
            if (!Main.DTR_REGEN.containsKey(Integer.parseInt(playertools.getMetadata(victim, "factionid")))) {
                Main.DTR_REGEN.put(Integer.parseInt(playertools.getMetadata(victim, "factionid")), System.currentTimeMillis() + death_time * 60000);
                if (Main.debug)
                    System.out.println("Death >> " + playertools.getMetadata(victim, "faction"));
            }
            SQL_Connection.dbExecute(con, "UPDATE factions SET DTR=DTR-1 WHERE ID='?'", playertools.getMetadata(victim, "factionid"));
        }
    }

    @EventHandler
    public void onVillagerDeath(EntityDeathEvent e) {
        if (!(e.getEntity() instanceof Villager)) return;
        if (Main.saved_players.containsKey(e.getEntity())) {
            ArrayList<ItemStack> stacks = Main.saved_items.get(e.getEntity());
            String uuid = e.getEntity().getMetadata("player.UUID").get(0).asString();
            e.setDroppedExp(0);
            e.getDrops().addAll(stacks);
            //Banhandler.banPlayerInHCF(player);
            if (Main.deathban) {
                SQL_Connection.dbExecute(con, "INSERT INTO deathbans SET uuid='?',time='?'", uuid, String.valueOf(System.currentTimeMillis() + (Main.death_time * 60000L)));
            }
            Main.saved_players.remove(e.getEntity());
            Main.saved_items.remove(e.getEntity());
        }
    }
}
