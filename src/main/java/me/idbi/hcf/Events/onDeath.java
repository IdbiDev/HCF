package me.idbi.hcf.Events;

import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.BanHandler;
import me.idbi.hcf.Tools.HCF_Timer;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Playertools;
import me.idbi.hcf.Tools.SQL_Connection;
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

import static me.idbi.hcf.Main.DTRREGENTIME;


public class onDeath implements Listener {
    private final Connection con = Main.getConnection();

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        //  Victim meghalt damager által! [Weapon]
        //  Példa: BauBence meghalt Macska0121 által! [POTION.POISON]

        //Some constants

        Player damager = e.getEntity().getKiller();
        Player victim = e.getEntity().getPlayer();
        Faction faction = Playertools.getPlayerFaction(victim);
        if (damager != null) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(Messages.kill_message_broadcast.language(player).setVictimWithKills(victim, damager).queue());
            }
            if (faction != null) {
                for (Player member : faction.getMembers()) {
                    member.sendMessage(Messages.kill_message_faction.language(member).setDeath(victim, damager).queue());
                }
            }
            HCFPlayer hcfDamager = HCFPlayer.getPlayer(damager);
            HCFPlayer hcfVictim = HCFPlayer.getPlayer(victim);

            hcfDamager.addKills();
            hcfVictim.addDeaths();

            //SQL_Connection.dbExecute(con,"UPDATE members SET kills=kills+1 WHERE uuid='?'",damager.getUniqueId().toString());
            //SQL_Connection.dbExecute(con,"UPDATE members SET deaths=deaths+1 WHERE uuid='?'",victim.getUniqueId().toString());
        } else {
            //SQL_Connection.dbExecute(con,"UPDATE members SET deaths=deaths+1 WHERE uuid='?'",victim.getUniqueId().toString());

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(Messages.kill_message_broadcast_without_victim.language(player).setDeathWithoutKiller(victim).queue());
            }
        }

        HCF_Timer.removePVPTag(victim);
        if (!Main.deathban) {
            victim.kickPlayer(Messages.no_deathban_kick.language(victim).queue());
            SQL_Connection.dbExecute(con, "INSERT INTO deathbans SET uuid='?',time='?'", victim.getUniqueId().toString(), "0");

        } else {
            BanHandler.banPlayerInHCF(victim);
        }


        if (faction != null) {
            if (!Main.DTRREGEN.containsKey(faction.id)) {
                Main.DTRREGEN.put(faction.id, System.currentTimeMillis() + DTRREGENTIME);
                faction.DTR -= Main.deathDTR;
            }
        }

        if (damager != null)
            victim.getWorld().dropItemNaturally(victim.getLocation(), onSignPlace.deathSign(damager, victim));
    }

    @EventHandler
    public void onVillagerDeath(EntityDeathEvent e) {
        if (!(e.getEntity() instanceof Villager)) return;
        if (Main.savedPlayers.containsKey(e.getEntity())) {
            ArrayList<ItemStack> stacks = Main.savedItems.get(e.getEntity());
            String uuid = e.getEntity().getMetadata("player.UUID").get(0).asString();

            if (e.getEntity().getKiller() != null)
                e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(),
                        onSignPlace.deathSign(
                                e.getEntity().getKiller().getName(),
                                Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName()
                        )
                );

            e.setDroppedExp(0);
            e.getDrops().addAll(stacks);
            if (Main.deathban)
                SQL_Connection.dbExecute(con, "INSERT INTO deathbans SET uuid='?',time='?'", uuid, String.valueOf(System.currentTimeMillis() + (Main.deathbanTime * 60000L)));
            else
                SQL_Connection.dbExecute(con, "INSERT INTO deathbans SET uuid='?',time='?'", uuid, "0");

            Main.savedPlayers.remove(e.getEntity());
            Main.savedItems.remove(e.getEntity());
        }
    }
}
