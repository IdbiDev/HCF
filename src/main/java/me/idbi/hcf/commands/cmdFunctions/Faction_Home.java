package me.idbi.hcf.commands.cmdFunctions;

import me.idbi.hcf.CustomFiles.ConfigLibrary;
import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.tools.JsonUtils;
import me.idbi.hcf.tools.SQL_Connection;
import me.idbi.hcf.tools.playertools;
import me.idbi.hcf.tools.rankManager;
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
        if (!playertools.hasPermission(p, rankManager.Permissions.SETHOME)) {
            p.sendMessage(Messages.NO_PERMISSION.queue());
            //Todo: Message on you dont have permission
            return;
        }
        Main.Faction faction = Main.faction_cache.get(Integer.valueOf(playertools.getMetadata(p, "factionid")));
        if(faction == null){
            p.sendMessage(Messages.ERROR_WHILE_EXECUTING.queue());
            return;
        }
        HashMap map = new HashMap() {{
            put("X", p.getLocation().getBlockX());
            put("Y", p.getLocation().getBlockY());
            put("Z", p.getLocation().getBlockZ());
            put("YAW", (int) p.getLocation().getYaw());
            put("PITCH", (int) p.getLocation().getPitch());
        }};

        faction.setHomeLocation(p.getLocation());

        JSONObject object = new JSONObject(map);

        SQL_Connection.dbExecute(con, "UPDATE factions SET home = '?' WHERE ID='?'", object.toString(), String.valueOf(faction.factionid));
        p.sendMessage(Messages.SETHOME_MESSAGE.queue());
    }

    public static void teleportToHome(Player p) {
        Main.Faction faction = Main.faction_cache.get(Integer.valueOf(playertools.getMetadata(p, "factionid")));
        HashMap<String, Object> json = SQL_Connection.dbPoll(con, "SELECT * FROM factions WHERE ID = '?' AND home IS NOT NULL", String.valueOf(faction.factionid));
        if (json.size() > 0) {
            Map<String, Object> map = JsonUtils.jsonToMap(new JSONObject(json.get("home").toString()));
            Location loc = new Location(
                    Bukkit.getWorld(ConfigLibrary.World_name.getValue()),
                    Integer.parseInt(map.get("X").toString()),
                    Integer.parseInt(map.get("Y").toString()),
                    Integer.parseInt(map.get("Z").toString()),
                    Integer.parseInt(map.get("YAW").toString()),
                    Integer.parseInt(map.get("PITCH").toString()));

            p.sendMessage(Messages.TELEPORT_TO_HOME.queue().replace("%time%", ConfigLibrary.teleport_delay_in_seconds.getValue()));
            teleportPlayers.add(p);
            delayer(p, loc);
        } else {
            p.sendMessage(Messages.DOESNT_HOME.queue());
        }
    }

    private static void delayer(Player p, Location loc) {
        new BukkitRunnable() {
            int delay = Integer.parseInt(ConfigLibrary.teleport_delay_in_seconds.getValue()) * 20;

            @Override
            public void run() {
                if (!teleportPlayers.contains(p)) {
                    p.sendMessage(Messages.TELEPORT_CANCEL.queue());
                    cancel();
                    return;
                }

                if (delay <= 0) {
                    p.sendMessage(Messages.SUCCESSFULLY_TELEPORT.queue());
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
