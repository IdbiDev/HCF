package me.idbi.hcf.commands.cmdFunctions.Bank;

import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.tools.Faction_Rank_Manager;
import me.idbi.hcf.tools.factionhistorys.HistoryEntrys;
import me.idbi.hcf.tools.playertools;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.util.Date;

public class Faction_DepositBank {
    public static final Connection con = Main.getConnection("Faction.cmd");
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
                Faction_DepositBank deposit =
                        new Faction_DepositBank().depositFaction(
                                faction,
                                p,
                                Integer.parseInt(args[1])
                        );

                if (deposit.transactionSuccessfully()) {
                    p.sendMessage(Messages.FACTION_BANK_DEPOSIT.queue().replace("%amount%", args[1]));
                    Scoreboards.refresh(p);
                    if(faction.balanceHistory.size()+1>50){
                        faction.balanceHistory.remove(faction.balanceHistory.size() - 1);
                    }
                    faction.balanceHistory.add(0,
                            new HistoryEntrys.BalanceEntry(Integer.parseInt(args[1]),p.getName(),new Date().getTime())
                    );

                }
            } catch (NumberFormatException ex) {
                p.sendMessage(Messages.FACTION_BANK_NUMBER_ERROR.queue());
                ex.printStackTrace();
            }
        } else {
            p.sendMessage(Messages.FACTION_BANK_NUMBER_ERROR.queue());
        }
    }

    public Faction_DepositBank depositFaction(Main.Faction faction, Player p, int amount) {
        try {
            int SQLAmount = Integer.parseInt(playertools.getMetadata(p, "money"));
            if (amount > SQLAmount) {
                transaction = false;
                p.sendMessage(Messages.NOT_ENOUGH_MONEY.queue());
                return this;
            }

            faction.balance = faction.balance + amount;

            playertools.setMetadata(p, "money", Integer.parseInt(playertools.getMetadata(p, "money")) - amount);

            transaction = true;
        } catch (Exception ex) {
            transaction = false;
            ex.printStackTrace();
        }
        return this;
    }

    public boolean transactionSuccessfully() {
        return transaction;
    }
}

