package me.idbi.hcf.events;

import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.tools.HCF_Timer;
import me.idbi.hcf.tools.SQL_Connection;
import me.idbi.hcf.tools.playertools;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static me.idbi.hcf.tools.playertools.HasMetaData;

public class onPlayerLeft implements Listener {
    private final Connection con = Main.getConnection("events.Quit");
    private final Main m = Main.getPlugin(Main.class);

    @EventHandler
    public void onPlayerLeft(PlayerQuitEvent e) {
        e.setQuitMessage("");
        // Save deaths,kills money etc
        if (HasMetaData(e.getPlayer(), "faction")) {
            SQL_Connection.dbExecute(con, "UPDATE members SET name = '?',money='?',kills='?',deaths='?',online=0 WHERE uuid='?'", e.getPlayer().getName(), playertools.getMetadata(e.getPlayer(), "money"), playertools.getMetadata(e.getPlayer(), "kills"), playertools.getMetadata(e.getPlayer(), "deaths"), e.getPlayer().getUniqueId().toString());
            System.out.println(SQL_Connection.dbExecute(con, "UPDATE members SET online='?' WHERE uuid='?'", "0", e.getPlayer().getUniqueId().toString()));
            // Koba moment
            Main.Faction f = Main.faction_cache.get(Integer.parseInt(playertools.getMetadata(e.getPlayer(), "factionid")));
            if(f != null){
                f.BroadcastFaction(Messages.LEAVE_FACTION_BC.repPlayer(e.getPlayer()).queue());
            }
            //Main.cacheRanks.remove(e.getPlayer());
        }
        if (Boolean.parseBoolean(playertools.getMetadata(e.getPlayer(), "freeze"))) {
            Bukkit.getBanList(BanList.Type.NAME).addBan(e.getPlayer().getName(),
                            m.getConfig().getString("Freeze.Reason"),
                            new Date(System.currentTimeMillis() + m.getConfig().getInt("Freeze.BanTimeSeconds") * 1000L),
                            null)
                    .save();
        }
        Main.player_cache.remove(e.getPlayer());
        setCombatLogger(e.getPlayer());
    }


    public void setCombatLogger(Player p) {
        if (!HCF_Timer.timers.containsKey(p)) {
            return;
        }
        long combatTimer = HCF_Timer.getCombatTime(p);
        ArrayList<ItemStack> items = new ArrayList<>();
        items.addAll(Arrays.asList(p.getInventory().getContents()));
        items.addAll(Arrays.asList(p.getInventory().getArmorContents()));

        LivingEntity entity = (LivingEntity) p.getWorld().spawnEntity(p.getLocation(), EntityType.VILLAGER);
        entity.setCustomName(p.getDisplayName());
        entity.setCanPickupItems(false);
        entity.setCustomNameVisible(true);
        Main.saved_items.put(entity, items);
        Main.saved_players.put(entity, combatTimer);
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, Integer.MAX_VALUE, false, false));
        entity.setMetadata("player.UUID", new FixedMetadataValue(Main.getPlugin(Main.class), p.getUniqueId().toString()));

    }
   /* public void CombatLogger(Player p, long combatTime){
        long millis = System.currentTimeMillis()/1000;
        long diff = combatTime - millis;

        if (diff > 0){
            HardcoreFactions.timers.put(p.getDisplayName()+".combatLogged",millis+diff);
        }

        ArrayList<ItemStack> items = new ArrayList<>();
        items.addAll(Arrays.asList(p.getInventory().getContents()));
        items.addAll(Arrays.asList(p.getInventory().getArmorContents()));


        HardcoreFactions.items.put(p.getDisplayName(),items);

        LivingEntity entity = (LivingEntity) p.getWorld().spawnEntity(p.getLocation(), EntityType.VILLAGER);
        entity.setCustomName(p.getDisplayName());
        entity.setCanPickupItems(false);
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,Integer.MAX_VALUE, Integer.MAX_VALUE, false,false));
        HardcoreFactions.loggerEntityId.put(p.getDisplayName(),entity.getUniqueId());
        HardcoreFactions.loggerEntityType.put(p.getDisplayName(),entity.getType());
        HardcoreFactions.loggerWorld.put(p.getDisplayName(),entity.getWorld());
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
  else if (HardcoreFactions.items.containsKey(e.getEntity().getCustomName())){
            String name = e.getEntity().getCustomName();
            String faction = Return.returnPlayerString("Players."+name+".Faction");

            Player killer = e.getEntity().getKiller();
            String killerFaction = Return.returnPlayerString("Players."+killer.getDisplayName()+".Faction");
            if (!faction.equals(killerFaction)){
                int kills = Return.returnPlayerInt("Players."+killer.getDisplayName()+".Kills")+1;
                Set.setPlayersInt("Players."+killer.getDisplayName()+".Kills",kills);
                int money = Return.returnPlayerInt("Players."+killer.getDisplayName()+".Money");
                Set.setPlayersInt("Players."+killer.getDisplayName()+".Money",money+100);
            }

            if (!faction.equals("N"))
            {
                int dtr = Return.returnPlayerInt("Factions."+faction+".DTR");
                if (dtr > 0){
                    dtr -=1;
                    Set.setPlayersInt("Factions."+faction+".DTR",dtr);
                    long sec = System.currentTimeMillis()/1000;
                    //Set.setPlayerLong("Factions."+faction+".DTRregen",mins+delay);
                    HardcoreFactions.timers.put(faction+".DTRregen",sec+delay);
                    int maxDTR = Return.returnPlayerInt("Factions."+faction+".maxDTR");
                    HardcoreFactions.factionAlert(faction,ChatColor.DARK_RED+name+" just died! DTR: "+dtr+"/"+maxDTR);
                }

            }
            HardcoreFactions.timers.remove(name+".combatLogged");

            if (HardcoreFactions.loggerEntityId.containsKey(name)){
                World w = HardcoreFactions.loggerWorld.get(name);
                EntityType en = HardcoreFactions.loggerEntityType.get(name);
                UUID id = HardcoreFactions.loggerEntityId.get(name);
                for (Entity entity : w.getEntities()){
                    if (entity.getType().equals(en)){
                        if (entity.getUniqueId() == id){
                            entity.remove();
                            break;
                        }
                    }
                }
                Entity entity = e.getEntity();
                e.getDrops().clear();
                ArrayList<ItemStack> items = HardcoreFactions.items.get(entity.getCustomName());
                e.getDrops().addAll(items);
                HardcoreFactions.deadCombatLoggers.add(entity.getCustomName());

                HardcoreFactions.loggerEntityId.remove(name);
                HardcoreFactions.loggerEntityType.remove(name);
                HardcoreFactions.loggerWorld.remove(name);
            }
        }
    }*/
}
