package me.idbi.hcf.Commands.FactionCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.*;
import me.idbi.hcf.Tools.Objects.Faction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static me.idbi.hcf.Commands.FactionCommands.FactionCreateCommand.con;

public class FactionHomeCommand extends SubCommand implements Listener {
    public static ArrayList<Player> teleportPlayers = new ArrayList<>();
    @Override
    public String getName() {
        return "home";
    }

    @Override
    public boolean isCommand(String argument) {
        return argument.equalsIgnoreCase(getName());
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getSyntax() {
        return "/faction home";
    }

    @Override
    public String getPermission() {
        return "factions.commands." + getName();
    }

    @Override
    public boolean hasPermission(Player p) {
        return p.hasPermission(getName());
    }

    @Override
    public void perform(Player p, String[] args) {
        Faction faction = Playertools.getPlayerFaction(p);
        if (faction == null) {
            p.sendMessage(Messages.not_in_faction.language(p).queue());
            return;
        }
        if (!Playertools.hasPermission(p, FactionRankManager.Permissions.MANAGE_RANKS)) {
            p.sendMessage(Messages.no_permission.language(p).queue());
            return;
        }
        HashMap<String, Integer> map = new HashMap<String, Integer>() {{
            put("X", p.getLocation().getBlockX());
            put("Y", p.getLocation().getBlockY());
            put("Z", p.getLocation().getBlockZ());
            put("YAW", (int) p.getLocation().getYaw());
            put("PITCH", (int) p.getLocation().getPitch());
        }};
        HCF_Claiming.Faction_Claim claim = HCF_Claiming.sendClaimByXZ(p.getLocation().getBlockX(), p.getLocation().getBlockZ());
        if (claim != null) {
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
        } else {
            p.sendMessage(Messages.faction_dont_have_claim.language(p).queue());
        }
    }
    private static void delayer(Player p, Location loc) {
        new BukkitRunnable() {
            int delay = Config.TeleportHome.asInt() * 20;

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
    public static void teleportToHome(Player p) {
        Faction faction = Playertools.getPlayerFaction(p);
        if(faction == null){
            p.sendMessage(Messages.not_in_faction.language(p).queue());
            return;
        }
        HashMap<String, Object> json = SQL_Connection.dbPoll(con, "SELECT * FROM factions WHERE ID = '?' AND home IS NOT NULL", String.valueOf(faction.id));
        if (json.size() > 0) {
            Map<String, Object> map = JsonUtils.jsonToMap(new JSONObject(json.get("home").toString()));
            Location loc = new Location(
                    Bukkit.getWorld(Config.WorldName.asStr()),
                    Integer.parseInt(map.get("X").toString()),
                    Integer.parseInt(map.get("Y").toString()),
                    Integer.parseInt(map.get("Z").toString()),
                    Integer.parseInt(map.get("YAW").toString()),
                    Integer.parseInt(map.get("PITCH").toString()));

            p.sendMessage(Messages.teleport_cancel.language(p).queue().replace("%time%", Config.TeleportHome.asStr()));
            teleportPlayers.add(p);
            delayer(p, loc);
        } else {
            p.sendMessage(Messages.doesnt_home.language(p).queue());
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        teleportPlayers.remove(e.getPlayer());
    }
}
