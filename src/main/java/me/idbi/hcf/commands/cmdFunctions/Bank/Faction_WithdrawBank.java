package me.idbi.hcf.commands.cmdFunctions.Bank;

import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.tools.Faction_Rank_Manager;
import me.idbi.hcf.tools.factionhistorys.HistoryEntrys;
import me.idbi.hcf.tools.playertools;
import org.bukkit.entity.Player;

import java.util.Date;


public class Faction_WithdrawBank {
    boolean transaction;

    public static void asd(String[] args, Player p) {
        if (!playertools.hasPermission(p, Faction_Rank_Manager.Permissions.MANAGE_MONEY)) {
            // ToDo: permission handle
            p.sendMessage(Messages.NO_PERMISSION.queue());
            return;
        }
        if (args[1].matches("^[0-9]+$")) {
            try {
                Main.Faction faction = Main.faction_cache.get(Integer.parseInt(playertools.getMetadata(p, "factionid")));
                if (Integer.parseInt(args[1]) <= 0) {
                    p.sendMessage(Messages.FACTION_BANK_NOT_ENOUGH.queue());
                    return;
                }
                if (faction.balance <= 0) {
                    p.sendMessage(Messages.FACTION_BANK_NOT_ENOUGH.queue());
                    return;
                }

                if (faction.balance < Integer.parseInt(args[1])) {
                    p.sendMessage(Messages.FACTION_BANK_NOT_ENOUGH.queue());
                    return;
                }
                Faction_WithdrawBank withdraw =
                        new Faction_WithdrawBank().withdrawFaction(
                                faction,
                                p,
                                Integer.parseInt(args[1])
                        );

                if (withdraw.transactionSuccessfully()) {
                    p.sendMessage(Messages.FACTION_BANK_WITHDRAW.queue().replace("%amount%", args[1]));
                    Scoreboards.refresh(p);
                    faction.balanceHistory.add(0,
                            new HistoryEntrys.BalanceEntry(-Integer.parseInt(args[1]),p.getName(),new Date().getTime())
                    );
                }
            } catch (NumberFormatException ex) {
                p.sendMessage(Messages.FACTION_BANK_NUMBER_ERROR.queue());
            }
        } else {
            p.sendMessage(Messages.FACTION_BANK_NUMBER_ERROR.queue());
        }
    }

    public Faction_WithdrawBank withdrawFaction(Main.Faction faction, Player p, int amount) {
        try {
            int SumSumAmount = faction.balance - amount;

            faction.balance = SumSumAmount;

            int playerMoney = Integer.parseInt(playertools.getMetadata(p, "money"));
            playertools.setMetadata(p, "money", playerMoney + amount);

            transaction = true;
        } catch (Exception ex) {
            transaction = false;
        }

        return this;
    }

    public boolean transactionSuccessfully() {
        return transaction;
    }
}
