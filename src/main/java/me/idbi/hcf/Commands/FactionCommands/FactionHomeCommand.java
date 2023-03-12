package me.idbi.hcf.Commands.FactionCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.HCF_Timer;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Playertools;
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
        if(HCF_Timer.getHomeTime(p) != 0) {
            p.sendMessage(Messages.already_home_teleporting.language(p).queue());
            return;
        }
        teleportToHome(p);
        addCooldown(p);
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
