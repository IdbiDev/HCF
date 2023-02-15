package me.idbi.hcf.commands.cmdFunctions;

import me.idbi.hcf.CustomFiles.Comments.Messages;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.Main;
import me.idbi.hcf.tools.*;
import me.idbi.hcf.tools.Objects.Faction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONObject;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Faction_Home implements Listener {

    public static ArrayList<Player> teleportPlayers;
    private static final Connection con = Main.getConnection("faction");

    public static void setHome(Player p) {
        Faction faction = playertools.getPlayerFaction(p);
        if(faction == null){
            p.sendMessage(Messages.not_in_faction.language(p).queue());
            return;
        }
        if (!playertools.hasPermission(p, Faction_Rank_Manager.Permissions.MANAGE_RANKS)) {
            p.sendMessage(Messages.no_permission.language(p).queue());
            return;
        }
        HashMap<String,Integer> map = new HashMap<String,Integer>() {{
            put("X", p.getLocation().getBlockX());
            put("Y", p.getLocation().getBlockY());
            put("Z", p.getLocation().getBlockZ());
            put("YAW", (int) p.getLocation().getYaw());
            put("PITCH", (int) p.getLocation().getPitch());
        }};
        HCF_Claiming.Faction_Claim claim = HCF_Claiming.sendClaimByXZ(p.getLocation().getBlockX(),p.getLocation().getBlockZ());
        if(claim != null) {
            if (claim.faction.id == faction.id) {
                faction.setHomeLocation(p.getLocation());

                JSONObject object = new JSONObject(map);

                // SQL_Connection.dbExecute(con, "UPDATE factions SET home = '?' WHERE ID='?'", object.toString(), String.valueOf(faction.id));

                p.sendMessage(Messages.sethome_message.language(p).queue());

                for (Player member : faction.getMembers()) {
                    member.sendMessage(Messages.sethome_update_faction
                            .language(member)
                            .setPlayer(p)
                            .setCoords(
                                    p.getLocation().getBlockX(),
                                    p.getLocation().getBlockY(),
                                    p.getLocation().getBlockZ())
                            .queue());
                }
            }
        }else{
            p.sendMessage(Messages.faction_dont_have_claim.language(p).queue());
        }
    }

    public static void teleportToHome(Player p) {
        Faction faction = playertools.getPlayerFaction(p);
        if(faction == null){
            p.sendMessage(Messages.not_in_faction.language(p).queue());
            return;
        }
        HashMap<String, Object> json = SQL_Connection.dbPoll(con, "SELECT * FROM factions WHERE ID = '?' AND home IS NOT NULL", String.valueOf(faction.id));
        if (json.size() > 0) {
            Map<String, Object> map = JsonUtils.jsonToMap(new JSONObject(json.get("home").toString()));
            Location loc = new Location(
                    Bukkit.getWorld(Config.world_name.asStr()),
                    Integer.parseInt(map.get("X").toString()),
                    Integer.parseInt(map.get("Y").toString()),
                    Integer.parseInt(map.get("Z").toString()),
                    Integer.parseInt(map.get("YAW").toString()),
                    Integer.parseInt(map.get("PITCH").toString()));

            p.sendMessage(Messages.teleport_cancel.language(p).queue().replace("%time%", Config.home_teleport.asStr()));
            teleportPlayers.add(p);
            delayer(p, loc);
        } else {
            p.sendMessage(Messages.doesnt_home.language(p).queue());
        }
    }

    private static void delayer(Player p, Location loc) {
        new BukkitRunnable() {
            int delay = Config.home_teleport.asInt() * 20;

            @Override
            public void run() {
                if (!teleportPlayers.contains(p)) {
                    p.sendMessage(Messages.teleport_cancel.language(p).queue());
                    cancel();
                    return;
                }

                if (delay <= 0) {
                    p.sendMessage(Messages.successfully_teleport.language(p).queue());
                    p.teleport(loc.add(0.5, 0, 0.5));
                    cancel();
                    return;
                }
                delay -= 2;
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0L, 2L);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        teleportPlayers.remove(e.getPlayer());
    }
}
