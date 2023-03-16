package me.idbi.hcf.Events;

import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.HCF_Timer;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Objects.StatTrak;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.Bukkit;
import org.bukkit.World;
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
        Player damager = e.getEntity().getKiller();
        Player victim = e.getEntity().getPlayer();
        Faction faction = Playertools.getPlayerFaction(victim);
        HCFPlayer hcfVictim = HCFPlayer.getPlayer(victim);
        hcfVictim.addDeaths();
        e.setDeathMessage("");
        if (faction != null) {
                Main.DTRREGEN.put(faction.getId(), System.currentTimeMillis() + DTRREGENTIME);
                if(victim.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
                    faction.DTR -= Config.OverworldDeathDTR.asDouble();
                }else if(victim.getWorld().getEnvironment().equals(World.Environment.NETHER)) {
                    faction.DTR -= Config.NetherDeathDTR.asDouble();
                }else {
                    faction.DTR -= Config.EndDeathDTR.asDouble();
                }
        }
        if (damager != null) {
            HCFPlayer hcfDamager = HCFPlayer.getPlayer(damager);
            hcfDamager.addKills();
            for (Player player : Bukkit.getOnlinePlayers())
                player.sendMessage(Messages.kill_message_broadcast.language(player).setVictimWithKills(victim, damager).queue());
            if (faction != null) {
                for (Player member : faction.getMembers()) {
                    member.sendMessage(Messages.kill_message_faction.language(member).setDeath(victim, damager).setDTR(faction.DTR).queue());
                }
            }
            StatTrak.addStatTrak(damager, victim);
            victim.getWorld().dropItemNaturally(victim.getLocation(), onSignPlace.deathSign(damager, victim));
        } else {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(Messages.kill_message_broadcast_without_killer.language(player).setDeathWithoutKiller(victim).queue());
            }
        }
        hcfVictim.banHCFPlayer();
        HCF_Timer.removePVPTag(victim);
    }

    @EventHandler
    public void onVillagerDeath(EntityDeathEvent e) {
        if (!(e.getEntity() instanceof Villager)) return;
        if (Main.savedPlayers.containsKey(e.getEntity())) {
            ArrayList<ItemStack> stacks = Main.savedItems.get(e.getEntity());
            String uuid = e.getEntity().getMetadata("player.UUID").get(0).asString();
            HCFPlayer hcf = HCFPlayer.getPlayer(UUID.fromString(uuid));
            if (e.getEntity().getKiller() != null)
                e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(),
                        onSignPlace.deathSign(
                                e.getEntity().getKiller().getName(),
                                Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName()
                        )
                );

            e.setDroppedExp(0);
            e.getDrops().addAll(stacks);
            hcf.banHCFPlayer();
            Main.savedPlayers.remove(e.getEntity());
            Main.savedItems.remove(e.getEntity());
        }
    }
}
