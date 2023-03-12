package me.idbi.hcf.Commands;

import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.SQL_Connection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.idbi.hcf.Commands.FactionCommands.FactionCreateCommand.con;

public class ReviveCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // /revive <player>
        if (sender instanceof Player p) {
            if (p.hasPermission("factions.command.revive")) {
                HCFPlayer player = HCFPlayer.getPlayer(p);
                try {
                    HCFPlayer targetPlayer = HCFPlayer.getPlayer(args[0]);
                    if (targetPlayer != null) {
                        if (targetPlayer.isDeathBanned()) {
                            Main.deathWaitClear.add(targetPlayer.uuid);
                            targetPlayer.setDeathBanned(false);
                            targetPlayer.setDeathTime(0);
                            SQL_Connection.dbExecute(con, "DELETE FROM deathbans WHERE uuid='?'", targetPlayer.uuid.toString());
                            //Todo: cooldown

                        } else {
                            //Todo: Not deathbanned
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
        return false;
    }
}
