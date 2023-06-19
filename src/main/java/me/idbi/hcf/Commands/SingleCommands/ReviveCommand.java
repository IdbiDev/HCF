package me.idbi.hcf.Commands.SingleCommands;

import me.idbi.hcf.BukkitCommands.CommandInfo;
import me.idbi.hcf.BukkitCommands.HCFCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Database.MongoDB.MongoDBDriver;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Database.MySQL.SQL_Connection;
import org.bukkit.entity.Player;

import static com.mongodb.client.model.Filters.eq;
import static me.idbi.hcf.Tools.Playertools.con;
@CommandInfo(
        name = "revive",
        description = "Revive a deathbanned player",
        permission = "factions.commands.revive",
        syntax = "/revive <player>")
public class ReviveCommand extends HCFCommand {
    @Override
    public int getCooldown() {
        return 60*60;
    }

    @Override
    public void execute(Player p, String[] args) {
        HCFPlayer player = HCFPlayer.getPlayer(p);
        try {
            HCFPlayer targetPlayer = HCFPlayer.getPlayer(args[0]);
            if (targetPlayer != null) {
                if (targetPlayer.isDeathBanned()) {
                    Main.deathWaitClear.add(targetPlayer.getUUID());
                    targetPlayer.setDeathBanned(false);
                    targetPlayer.setDeathTime(0);
                    if(Main.isUsingMongoDB()){
                        MongoDBDriver.Delete(MongoDBDriver.MongoCollections.DEATHBANS,eq("uuid",targetPlayer.getUUID().toString()));
                    }else {
                        SQL_Connection.dbExecute(con, "DELETE FROM deathbans WHERE uuid='?'", targetPlayer.getUUID().toString());
                    }
                    addCooldown(p);
                } else {
                    player.sendMessage(Messages.not_deathbanned);
                }
            } else {
                p.sendMessage(Messages.not_found_player.language(p).queue());
            }
        } catch (IndexOutOfBoundsException asked) {
            p.sendMessage(Messages.missing_argument.language(p).queue());
            p.sendMessage("§cUsage: /revive <player>");
        } catch (Exception e) {
            e.printStackTrace();
            p.sendMessage(Messages.error_while_executing.language(p).queue());
        }
    }
}
