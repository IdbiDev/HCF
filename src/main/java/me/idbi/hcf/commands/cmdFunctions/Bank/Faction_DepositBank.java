package me.idbi.hcf.commands.cmdFunctions.Bank;

import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.tools.SQL_Connection;
import me.idbi.hcf.tools.playertools;
import org.bukkit.entity.Player;

public class Faction_DepositBank {
    public static final Connection con = Main.getConnection("Faction.cmd");
    boolean transaction;

    public Faction_DepositBank depositFaction(Main.Faction faction, int amount) {
        try {
            int SumSumAmount = faction.balance + amount;

            faction.balance = SumSumAmount;

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

    public static void asd(String[] args, Player p) {
        if(!playertools.hasPermission(p, rankManager.Permissions.DEPOSIT)){
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
                System.out.println("ADEPOSITING");
                Faction_DepositBank deposit =
                        new Faction_DepositBank().depositFaction(
                                faction,
                                Integer.parseInt(args[1])
                        );

                if(deposit.transactionSuccessfully()) {
                    p.sendMessage(Messages.FACTION_BANK_DEPOSIT.queue().replace("%amount%", args[1]));
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

