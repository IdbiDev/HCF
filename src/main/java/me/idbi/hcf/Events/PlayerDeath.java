package me.idbi.hcf.Events;

import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.InventoryRollback.Rollback;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Objects.StatTrak;
import me.idbi.hcf.Tools.Playertools;
import me.idbi.hcf.Tools.Timers;
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

import static me.idbi.hcf.Main.dtrRegenTime;


public class PlayerDeath implements Listener {
    private final Connection con = Main.getConnection();

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player damager = e.getEntity().getKiller();
        Player victim = e.getEntity().getPlayer();
        Faction faction = Playertools.getPlayerFaction(victim);
        HCFPlayer hcfVictim = HCFPlayer.getPlayer(victim);
        hcfVictim.createRollback(victim.getLastDamageCause().getCause(), Rollback.RollbackLogType.DEATH);
        hcfVictim.addDeaths();
        hcfVictim.takeMoney(hcfVictim.getMoney());

        e.setDeathMessage("");
        if (faction != null) {
                //Main.DTRREGEN.put(faction.getId(), System.currentTimeMillis() + DTRREGENTIME);
            faction.setDTR_TIMEOUT(System.currentTimeMillis() + dtrRegenTime);
                if(victim.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
                    faction.removeDTR(Config.OverworldDeathDTR.asDouble());
                }else if(victim.getWorld().getEnvironment().equals(World.Environment.NETHER)) {
                    faction.removeDTR(Config.NetherDeathDTR.asDouble());
                }else {
                    faction.removeDTR(Config.EndDeathDTR.asDouble());
                }
                faction.removePoints(Config.PointDecreaseOnDeath.asInt());
        }
        if (damager != null) {
            HCFPlayer hcfDamager = HCFPlayer.getPlayer(damager);
            hcfDamager.addMoney(Config.AddMoneyOnKill.asInt());
            hcfDamager.addKills();

            if(Config.PlayerLoseMoney.asBoolean())
                hcfDamager.addMoney(hcfVictim.getMoney());

            for (Player player : Bukkit.getOnlinePlayers())
                player.sendMessage(Messages.kill_message_broadcast.language(player).setVictimWithKills(victim, damager).queue());
            if (faction != null) {
                for (Player member : faction.getOnlineMembers()) {
                    member.sendMessage(Messages.kill_message_faction.language(member).setDeath(victim, damager).setDTR(faction.getDTR()).queue());
                }
            }
            Faction damagerFaction = hcfDamager.getFaction();
            if(damagerFaction != null){
                damagerFaction.addPoints(Config.PointPerKill.asInt());
            }
            StatTrak.addStatTrak(damager, victim);
            victim.getWorld().dropItemNaturally(victim.getLocation(), SignPlace.deathSign(damager, victim));
        } else {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(Messages.kill_message_broadcast_without_killer.language(player).setDeathWithoutKiller(victim).queue());
            }
        }
        hcfVictim.banHCFPlayer();
        Timers.COMBAT_TAG.remove(victim);
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
                        SignPlace.deathSign(
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
