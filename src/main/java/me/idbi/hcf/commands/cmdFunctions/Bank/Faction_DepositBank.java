package me.idbi.hcf.commands.cmdFunctions.Bank;

import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.tools.playertools;
import org.bukkit.entity.Player;

public class Faction_DepositBank {

    boolean transaction;

    public Faction_DepositBank depositFaction(Main.Faction faction, int amount) {
        try {
            int SumSumAmount = faction.balance + amount;

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
        // ToDo: permission check

        if(args[1].matches("^[0-9]+$")) {
            try {
                Main.Faction faction =  Main.faction_cache.get(Integer.parseInt(playertools.getMetadata(p, "factionid")));
                if(Integer.parseInt(args[1]) <= 0) {
                    p.sendMessage(Messages.FACTION_BANK_NOT_ENOUGH.queue());
                    return;
                }

                Faction_DepositBank deposit =
                        new Faction_DepositBank().depositFaction(
                                faction,
                                Integer.parseInt(args[1])
                        );

                if(deposit.transactionSuccessfully()) {
                    p.sendMessage(Messages.FACTION_BANK_DEPOSIT.queue().replace("%amount%", args[1]));
                }
            } catch (NumberFormatException ex) {
                p.sendMessage(Messages.FACTION_BANK_NUMBER_ERROR.queue());
            }
        } else {
            p.sendMessage(Messages.FACTION_BANK_NUMBER_ERROR.queue());
        }
    }
}

