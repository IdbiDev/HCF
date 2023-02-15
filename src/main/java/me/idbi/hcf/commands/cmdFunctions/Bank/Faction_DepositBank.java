package me.idbi.hcf.commands.cmdFunctions.Bank;

import me.idbi.hcf.CustomFiles.Comments.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.tools.Faction_Rank_Manager;
import me.idbi.hcf.tools.Objects.Faction;
import me.idbi.hcf.tools.Objects.HCFPlayer;
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
            p.sendMessage(Messages.no_permission.language(p).queue());
            return;
        }

        if (args[1].matches("^[0-9]+$")) {
            try {
                Faction faction = playertools.getPlayerFaction(p);
                if (Integer.parseInt(args[1]) <= 0) {
                    p.sendMessage(Messages.faction_bank_not_enough.language(p).queue());
                    return;
                }
                Faction_DepositBank deposit =
                        new Faction_DepositBank().depositFaction(
                                faction,
                                p,
                                Integer.parseInt(args[1])
                        );

                if (deposit.transactionSuccessfully()) {
                    p.sendMessage(Messages.faction_bank_deposit.language(p).queue().replace("%amount%", args[1]));
                    Scoreboards.refresh(p);
                    if(faction.balanceHistory.size()+1>50){
                        faction.balanceHistory.remove(faction.balanceHistory.size() - 1);
                    }
                    faction.balanceHistory.add(0,
                            new HistoryEntrys.BalanceEntry(Integer.parseInt(args[1]),p.getName(),new Date().getTime())
                    );

                }
            } catch (NumberFormatException ex) {
                p.sendMessage(Messages.faction_bank_withdraw.language(p).queue());
                ex.printStackTrace();
            }
        } else {
            p.sendMessage(Messages.faction_bank_number_error.language(p).queue());
        }
    }

    public Faction_DepositBank depositFaction(Faction faction, Player p, int amount) {
        try {
            HCFPlayer player = HCFPlayer.getPlayer(p);
            int SQLAmount = player.money;
            if (amount > SQLAmount) {
                transaction = false;
                p.sendMessage(Messages.not_enough_slot.language(p).queue());
                return this;
            }

            faction.balance = faction.balance + amount;

            player.takeMoney(amount);

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

