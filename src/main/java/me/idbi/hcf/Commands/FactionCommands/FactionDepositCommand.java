package me.idbi.hcf.Commands.FactionCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.Tools.FactionHistorys.HistoryEntrys;
import me.idbi.hcf.Tools.FactionRankManager;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class FactionDepositCommand extends SubCommand {
    boolean transaction;

    @Override
    public String getName() {
        return "deposit";
    }

    @Override
    public boolean isCommand(String argument) {
        return argument.equalsIgnoreCase(getName()) || argument.equalsIgnoreCase("d");
    }

    @Override
    public String getDescription() {
        return "Deposits to the faction bank.";
    }

    @Override
    public String getSyntax() {
        return "/faction deposit <amount>";
    }

    @Override
    public int getCooldown() {
        return 2;
    }

    @Override
    public void perform(Player p, String[] args) {
        if (!Playertools.hasPermission(p, FactionRankManager.Permissions.MANAGE_MONEY)) {
            // ToDo: permission handle
            p.sendMessage(Messages.no_permission.language(p).queue());
            return;
        }

        if (args[1].matches("^[0-9]+$") || args[1].equalsIgnoreCase("all")) {
            try {
                HCFPlayer hcf = HCFPlayer.getPlayer(p);
                int arg1 = args[1].matches("^[0-9]+$") ? Integer.parseInt(args[1]) : hcf.getMoney();
                Faction faction = Playertools.getPlayerFaction(p);
                if (arg1 <= 0) {
                    p.sendMessage(Messages.faction_bank_not_enough.language(p).queue());
                    return;
                }
                FactionDepositCommand deposit =
                        new FactionDepositCommand().depositFaction(
                                faction,
                                p,
                                arg1
                        );

                if (deposit.transactionSuccessfully()) {
                    p.sendMessage(Messages.faction_bank_deposit.language(p).queue().replace("%amount%", arg1 + ""));
                    Scoreboards.refresh(p);
                    addCooldown(p);
                    if (faction.balanceHistory.size() + 1 > 50) {
                        faction.balanceHistory.remove(faction.balanceHistory.size() - 1);
                    }
                    faction.balanceHistory.add(0,
                            new HistoryEntrys.BalanceEntry(arg1, p.getName(), new Date().getTime())
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

    public FactionDepositCommand depositFaction(Faction faction, Player p, int amount) {
        try {
            HCFPlayer player = HCFPlayer.getPlayer(p);
            int SQLAmount = player.getMoney();
            if (amount > SQLAmount) {
                transaction = false;
                p.sendMessage(Messages.faction_bank_not_enough.language(p).queue());
                return this;
            }

            faction.addBalance(amount);

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
