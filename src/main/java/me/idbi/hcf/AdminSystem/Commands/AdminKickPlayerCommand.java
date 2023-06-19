package me.idbi.hcf.AdminSystem.Commands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.Tools.Database.MongoDB.MongoDBDriver;
import me.idbi.hcf.Tools.Nametag.NameChanger;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Database.MySQL.SQL_Connection;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.util.HashMap;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static me.idbi.hcf.Tools.Playertools.con;
public class AdminKickPlayerCommand extends SubCommand {

    @Override
    public String getName() {
        return "removeplayer";
    }

    @Override
    public boolean isCommand(String argument) {
        return argument.equalsIgnoreCase(getName());
    }

    @Override
    public String getDescription() {
        return "Kicks the selected player from the selected faction.";
    }

    @Override
    public String getSyntax() {
        return "/admin " + getName() + "<player> <faction>";
    }

    @Override
    public String getPermission() {
        return "factions.commands.admin." + getName();
    }

    @Override
    public boolean hasPermission(Player p) {
        return p.hasPermission(getPermission());
    }

    @Override
    public boolean hasCooldown(Player p) {
        if(!SubCommand.commandCooldowns.get(this).containsKey(p)) return false;
        return SubCommand.commandCooldowns.get(this).get(p) > System.currentTimeMillis();
    }

    @Override
    public void addCooldown(Player p) {
        HashMap<Player, Long> hashMap = SubCommand.commandCooldowns.get(this);
        hashMap.put(p, System.currentTimeMillis() + (getCooldown() * 1000L));
        SubCommand.commandCooldowns.put(this, hashMap);
    }

    @Override
    public int getCooldown() {
        return 2;
    }

    @Override
    public void perform(Player p, String[] args) {
        if (!Main.nameToFaction.containsKey(args[2])) {
            p.sendMessage(Messages.not_found_faction.language(p).queue());
            return;
        }
        if (Bukkit.getPlayer(args[1]) == null) {
            p.sendMessage(Messages.not_found_player.language(p).queue());
            return;
        }
        Player target = Bukkit.getPlayer(args[1]);
        HCFPlayer player = HCFPlayer.getPlayer(target);
        player.removeFaction();
        if(Main.isUsingMongoDB()) {
            Bson update = combine(set("faction", 0), set("rank", "None"));
            MongoDBDriver.Update(MongoDBDriver.MongoCollections.MEMBERS, eq("uuid", target.getUniqueId().toString()), update);
        }else {
            SQL_Connection.dbExecute(con, "UPDATE members SET rank = '?', faction = '?' WHERE uuid = '?'", "None", "0", target.getUniqueId().toString());
        }
        //SQL_Connection.dbExecute(con, "UPDATE members SET faction=0 WHERE uuid='?'", target.getUniqueId().toString());
        Scoreboards.refresh(target);
        NameChanger.refresh(target);
    }
}
