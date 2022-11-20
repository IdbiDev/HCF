package me.idbi.hcf.events;

import me.idbi.hcf.CustomFiles.ConfigLibrary;
import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.tools.DisplayName.displayTeams;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Map;
import java.util.Objects;

public class onPlayerJoin implements Listener {
    private Main m = Main.getPlugin(Main.class);

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        playertools.LoadPlayer(p);

        if(Main.death_wait_clear.contains(e.getPlayer().getUniqueId())) {
            e.getPlayer().getInventory().clear();
            e.getPlayer().getInventory().setArmorContents(null);
            e.getPlayer().setHealth(e.getPlayer().getMaxHealth());
            e.getPlayer().setFoodLevel(20);
            e.getPlayer().setFallDistance(0);


            Main.death_wait_clear.remove(e.getPlayer().getUniqueId());
            String str = ConfigLibrary.Spawn_location.getValue();

            Location spawn = new Location(
                    Bukkit.getWorld(ConfigLibrary.World_name.getValue()),
                    Integer.parseInt(str.split(" ")[0]),
                    Integer.parseInt(str.split(" ")[1]),
                    Integer.parseInt(str.split(" ")[2]),
                    Integer.parseInt(str.split(" ")[3]),
                    Integer.parseInt(str.split(" ")[4])
            );
            e.getPlayer().teleport(spawn);
            //pvpTimer
        }
        e.setJoinMessage("");
        
        if (!Objects.equals(playertools.getMetadata(p, "factionid"), "0")) {
            Main.Faction f = playertools.getPlayerFaction(e.getPlayer());
            if(f != null) {
                f.BroadcastFaction(
                        Messages.JOIN_FACTION_BC.repPlayer(p).queue());
                f.addPrefixPlayer(p);
            }
        }

        // displayTeams.setupPlayer(e.getPlayer());
        // displayTeams.addPlayerToTeam(e.getPlayer());

        Scoreboards.refresh(p);

        for (Map.Entry<LivingEntity, Long> entity : Main.saved_players.entrySet()) {
            if (Bukkit.getPlayer(entity.getKey().getCustomName()) != null) {
                if (Bukkit.getPlayer(entity.getKey().getCustomName()).isOnline()) {
                    entity.getKey().remove();

                    Main.saved_players.remove(entity.getKey());
                    Main.saved_items.remove(entity.getKey());
                }
            }
        }
    }
}
