package me.idbi.hcf.Commands.FactionCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Claiming;
import me.idbi.hcf.Tools.Database.MongoDB.MongoDBDriver;
import me.idbi.hcf.Tools.Database.MySQL.SQL_Async;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.entity.Player;

import static com.mongodb.client.model.Filters.eq;
import static me.idbi.hcf.Tools.Playertools.con;


public class FactionUnclaimCommand extends SubCommand {

    @Override
    public String getName() {
        return "unclaim";
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
        return "/faction " + getName();
    }

    @Override
    public int getCooldown() {
        return 2;
    }

    @Override
    public void perform(Player p, String[] args) {
        addCooldown(p);
        HCFPlayer player = HCFPlayer.getPlayer(p);
        if(player.inFaction()) {
            if(player.getRank().isLeader()) {
                if(!player.getFaction().getClaims().isEmpty()) {
                    double backmoney = Claiming.calculateMoneyFromClaim(player.getFaction())  * Config.UnClaimPriceMultiplier.asDouble();
                    player.addMoney(Math.toIntExact(Math.round(backmoney)));
                    player.getFaction().clearClaims(); // done
                    if(Main.isUsingMongoDB()){
                        MongoDBDriver.Delete(MongoDBDriver.MongoCollections.CLAIMS,eq("factionid",player.getFaction().getId()));
                    }else {
                        SQL_Async.dbExecuteAsync(con,"DELETE FROM claims WHERE factionid='?'", String.valueOf(player.getFaction().getId()));
                    }
                    player.sendMessage(Messages.success_unclaim);

                }else {
                    p.sendMessage(Messages.faction_not_have_claim.language(p).queue());
                }
            }else {
                p.sendMessage(Messages.no_permission.language(p).queue());
            }
        }else {
            p.sendMessage(Messages.not_in_faction.language(p).queue());
        }
    }
}
