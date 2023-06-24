package me.idbi.hcf.Commands.FactionCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Claiming;
import me.idbi.hcf.Tools.Database.MongoDB.MongoDBDriver;
import me.idbi.hcf.Tools.Database.MySQL.SQL_Connection;
import me.idbi.hcf.Tools.FactionRankManager;
import me.idbi.hcf.Tools.Objects.Claim;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Playertools;
import org.bson.conversions.Bson;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.set;
import static me.idbi.hcf.Tools.Playertools.con;

public class FactionSetHomeCommand extends SubCommand implements Listener {

    public static ArrayList<Player> teleportPlayers = new ArrayList<>();
    @Override
    public String getName() {
        return "sethome";
    }

    @Override
    public boolean isCommand(String argument) {
        return argument.equalsIgnoreCase(getName());
    }

    @Override
    public String getDescription() {
        return "Sets the faction home to the current position.";
    }

    @Override
    public String getSyntax() {
        return "/faction sethome";
    }

    @Override
    public int getCooldown() {
        return 2;
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
        //
        Claim claim = Claiming.sendClaimByXZ(p.getWorld(), p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ());
        if (claim != null) {
            if (claim.getFaction().getId() == faction.getId()) {
                if(Main.isUsingMongoDB()){
                    Bson update = set("home",new JSONObject(map).toString());
                    MongoDBDriver.Update(MongoDBDriver.MongoCollections.FACTIONS,eq("ID",faction.getId()),update);
                }else {
                    SQL_Connection.dbExecute(con,"UPDATE factions SET home='?' WHERE ID='?'", new JSONObject(map).toString(), faction.getId() + "");
                }
                int x = p.getLocation().getBlockX();
                int z = p.getLocation().getBlockZ();
                Location secLoc = p.getLocation().clone();
                secLoc.setX(x);
                secLoc.setZ(z);
                faction.setHomeLocation(/*secLoc.add(x >= 0 ? 0.5 : -0.5, 0.0, z >= 0 ? 0.5 : -0.5*/p.getLocation()/*secLoc*/);
                addCooldown(p);

                p.sendMessage(Messages.sethome_message.language(p).queue());

                for (Player member : faction.getOnlineMembers()) {
                    member.sendMessage(Messages.sethome_update_faction
                            .language(member)
                            .setPlayer(p)
                            .setCoords(
                                    secLoc.getBlockX(),
                                    secLoc.getBlockY(),
                                    secLoc.getBlockZ())
                            .queue());
                }
            }
        } else {
            p.sendMessage(Messages.faction_dont_have_claim.language(p).queue());
        }
    }

}
