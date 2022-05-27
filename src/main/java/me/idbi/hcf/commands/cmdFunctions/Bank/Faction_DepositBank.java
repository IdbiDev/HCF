package me.idbi.hcf.commands.cmdFunctions.Bank;

import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.tools.playertools;
import me.idbi.hcf.tools.rankManager;
import org.bukkit.entity.Player;

import java.sql.Connection;

public class Faction_DepositBank {
    public static final Connection con = Main.getConnection("Faction.cmd");
    boolean transaction;

    public Faction_DepositBank depositFaction(Main.Faction faction, Player p, int amount) {
        try {
            int SQLAmount = Integer.parseInt(playertools.getMetadata(p,"money"));
            if(amount > SQLAmount) {
                transaction = false;
                p.sendMessage(Messages.FACTION_BANK_PLAYER_NOT_ENOUGH.queue());
                return this;
            }

            int SumSumAmount = faction.balance + amount;

            faction.balance = SumSumAmount;

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
                Faction_DepositBank deposit =
                        new Faction_DepositBank().depositFaction(
                                faction,
                                p,
                                Integer.parseInt(args[1])
                        );

                if(deposit.transactionSuccessfully()) {
                    p.sendMessage(Messages.FACTION_BANK_DEPOSIT.queue().replace("%amount%", args[1]));
                    Scoreboards.refresh(p);
                }
            } catch (NumberFormatException ex) {
                p.sendMessage(Messages.FACTION_BANK_NUMBER_ERROR.queue());
                ex.printStackTrace();
            }
        } else {
            p.sendMessage(Messages.FACTION_BANK_NUMBER_ERROR.queue());
        }
    }
}

