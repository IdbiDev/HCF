package me.idbi.hcf.Commands.FactionCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.Connection;

public class FactionTransferCommand extends SubCommand {
    public static Connection con = Main.getConnection("cmd.Transfer");

    @Override
    public String getName() {
        return "transfer";
    }

    @Override
    public boolean isCommand(String argument) {
        return argument.equalsIgnoreCase(getName());
    }

    @Override
    public String getDescription() {
        return "Transfers the faction";
    }

    @Override
    public boolean hasPermission(Player p) {
        return p.hasPermission(getPermission());
    }

    @Override
    public String getPermission() {
        return "factions.command." + getName();
    }

    @Override
    public String getSyntax() {
        return "/faction transfer <player>";
    }

    @Override
    public void perform(Player p, String[] args) {
        Faction playerFaction = Playertools.getPlayerFaction(p);
        if (playerFaction != null) {
            if (p.getUniqueId().toString().equalsIgnoreCase(playerFaction.leader)) {
                OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[1]);
                if (targetPlayer != null) {
                    //Player targetPlayer_Online;
                    HCFPlayer targetHCF = HCFPlayer.getPlayer(targetPlayer.getUniqueId());
                    HCFPlayer playerHCF = HCFPlayer.getPlayer(p.getUniqueId());

                    Faction targetFaction = Playertools.getPlayerFaction(targetPlayer);

                    if (targetPlayer.getUniqueId() == p.getUniqueId()) {
                        p.sendMessage(Messages.cant_kick_yourself.language(p).queue());
                        return;
                    }

                    if (targetFaction != playerFaction) {
                        p.sendMessage(Messages.cant_kick_leader.language(p).queue());
                        return;
                    }

                    playerFaction.leader = targetPlayer.getUniqueId().toString();

                    targetHCF.setRank(playerFaction.getLeaderRank());
                    playerHCF.setRank(playerFaction.getDefaultRank());
                }
            } else {
                p.sendMessage(Messages.no_permission_in_faction.language(p).queue());
            }
        } else {
            // Nem vagy tagja egy factionnak se
            p.sendMessage(Messages.not_in_faction.language(p).queue());
        }
    }
}
