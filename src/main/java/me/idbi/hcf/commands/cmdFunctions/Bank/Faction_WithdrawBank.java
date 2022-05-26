package me.idbi.hcf.commands.cmdFunctions.Bank;

import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.tools.SQL_Connection;
import me.idbi.hcf.tools.playertools;
import me.idbi.hcf.tools.rankManager;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Faction_WithdrawBank {
    boolean transaction;

    public Faction_WithdrawBank withdrawFaction(Main.Faction faction, int amount) {
        try {
            int SumSumAmount = faction.balance - amount;

            faction.balance = SumSumAmount;

            transaction = true;
        } catch (Exception ex) {
            transaction = false;
        }

        return this;
    }

    public boolean transactionSuccessfully() {
        return transaction;
    }

    public static void asd(String[] args, Player p) {
        if(!playertools.hasPermission(p, rankManager.Permissions.WITHDRAW)){
            // ToDo: permission handle
            return;
        }
        if(args[1].matches("^[0-9]+$")) {
            try {
                Main.Faction faction =  Main.faction_cache.get(Integer.parseInt(playertools.getMetadata(p, "factionid")));
                if(Integer.parseInt(args[1]) <= 0) {
                    p.sendMessage(Messages.FACTION_BANK_NOT_ENOUGH.queue());
                    return;
                }
                if(faction.balance < Integer.parseInt(args[1])) {
                    p.sendMessage(Messages.FACTION_BANK_NOT_ENOUGH.queue());
                    return;
                }
                Faction_WithdrawBank withdraw =
                        new Faction_WithdrawBank().withdrawFaction(
                                faction,
                                Integer.parseInt(args[1])
                        );

                if(withdraw.transactionSuccessfully()) {
                    p.sendMessage(Messages.FACTION_BANK_WITHDRAW.queue().replace("%amount%", args[1]));
                    Scoreboards.refresh(p);
                }
            } catch (NumberFormatException ex) {
                p.sendMessage(Messages.FACTION_BANK_NUMBER_ERROR.queue());
            }
        } else {
            p.sendMessage(Messages.FACTION_BANK_NUMBER_ERROR.queue());
        }
    }
}
