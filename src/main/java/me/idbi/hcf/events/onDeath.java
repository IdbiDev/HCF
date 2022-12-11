package me.idbi.hcf.events;

import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.tools.Banhandler;
import me.idbi.hcf.tools.HCF_Timer;
import me.idbi.hcf.tools.SQL_Connection;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.UUID;

import static me.idbi.hcf.Main.DTR_REGEN_TIME;


public class onDeath implements Listener {
    private final Connection con = Main.getConnection("events.onDeath");

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        //  Victim meghalt damager által! [Weapon]
        //  Példa: BauBence meghalt Macska0121 által! [POTION.POISON]

        //Some constants

        Player damager = e.getEntity().getKiller();
        Player victim = e.getEntity().getPlayer();
        Main.Faction faction = playertools.getPlayerFaction(victim);
        if (damager != null) {
            e.setDeathMessage(Messages.KILL_MESSAGE_BROADCAST.repDeathWithKills(victim,damager).queue());
            if(faction !=null){
                faction.BroadcastFaction(Messages.KILL_MESSAGE_FACTION.repDeath(victim,damager).queue());
            }
            playertools.setMetadata(damager,"kills",Integer.parseInt(playertools.getMetadata(damager,"kills"))+1);
            playertools.setMetadata(victim,"deaths",Integer.parseInt(playertools.getMetadata(victim,"deaths")+1));
            Main.PlayerStatistic cica = Main.playerStatistics.get(damager);
            cica.kills++;
            Main.playerStatistics.put(damager,cica);
            cica = Main.playerStatistics.get(victim);
            cica.deaths++;
            Main.playerStatistics.put(victim,cica);

            SQL_Connection.dbExecute(con,"UPDATE members SET kills=kills+1 WHERE uuid='?'",damager.getUniqueId().toString());
           SQL_Connection.dbExecute(con,"UPDATE members SET deaths=deaths+1 WHERE uuid='?'",victim.getUniqueId().toString());
        } else {
            SQL_Connection.dbExecute(con,"UPDATE members SET deaths=deaths+1 WHERE uuid='?'",victim.getUniqueId().toString());
            e.setDeathMessage(Messages.KILL_MESSAGE_BROADCAST_WITHOUT_VICTIM.repDeathWithoutKiller(victim).queue());
        }

        HCF_Timer.removePVPTag(victim);
        if (!Main.deathban) {
            victim.kickPlayer(Messages.NO_DEATHBAN_KICK.queue());
            SQL_Connection.dbExecute(con, "INSERT INTO deathbans SET uuid='?',time='?'", victim.getUniqueId().toString(), "0");

        } else {
            Banhandler.banPlayerInHCF(victim);
        }


        if (faction !=null) {
            if (!Main.DTR_REGEN.containsKey(faction.id)) {
                Main.DTR_REGEN.put(faction.id, System.currentTimeMillis() + DTR_REGEN_TIME * 1000L);
                if (Main.debug)
                    System.out.println("Death >> " + faction.name);
                faction.DTR -= Main.DEATH_DTR;
            }
        }

        if(damager != null)
            victim.getWorld().dropItemNaturally(victim.getLocation(), onSignPlace.deathSign(damager, victim));
    }

    @EventHandler
    public void onVillagerDeath(EntityDeathEvent e) {
        if (!(e.getEntity() instanceof Villager)) return;
        if (Main.saved_players.containsKey(e.getEntity())) {
            ArrayList<ItemStack> stacks = Main.saved_items.get(e.getEntity());
            String uuid = e.getEntity().getMetadata("player.UUID").get(0).asString();

            if(e.getEntity().getKiller() != null)
                e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(),
                        onSignPlace.deathSign(
                                e.getEntity().getKiller().getName(),
                                Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName()
                        )
                );

            e.setDroppedExp(0);
            e.getDrops().addAll(stacks);
            if (Main.deathban)
                SQL_Connection.dbExecute(con, "INSERT INTO deathbans SET uuid='?',time='?'", uuid, String.valueOf(System.currentTimeMillis() + (Main.death_time * 60000L)));
            else
                SQL_Connection.dbExecute(con, "INSERT INTO deathbans SET uuid='?',time='?'", uuid, "0");

            Main.saved_players.remove(e.getEntity());
            Main.saved_items.remove(e.getEntity());
        }
    }
}
