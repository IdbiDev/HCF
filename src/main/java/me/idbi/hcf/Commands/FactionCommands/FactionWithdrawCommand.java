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

public class FactionWithdrawCommand extends SubCommand {
    boolean transaction;

    @Override
    public String getName() {
        return "withdraw";
    }

    @Override
    public boolean isCommand(String argument) {
        return argument.equalsIgnoreCase(getName()) || argument.equalsIgnoreCase("w");
    }

    @Override
    public String getDescription() {
        return "Withdraws from the faction's balance.";
    }

    @Override
    public String getSyntax() {
        return "/faction withdraw <amount>";
    }

    @Override
    public int getCooldown() {
        return 2;
    }

    @Override
    public void perform(Player p, String[] args) {
        if (!Playertools.hasPermission(p, FactionRankManager.Permissions.MANAGE_MONEY)) {
            p.sendMessage(Messages.no_permission.language(p).queue());
            return;
        }
        if (args[1].matches("^[0-9]+$") || args[1].equalsIgnoreCase("all")) {
            try {
                Faction faction = Playertools.getPlayerFaction(p);
                int arg1 = args[1].matches("^[0-9]+$") ? Integer.parseInt(args[1]) : faction.getBalance();
                if (arg1 <= 0) {
                    p.sendMessage(Messages.faction_bank_not_enough.language(p).queue());
                    return;
                }
                if (faction.getBalance() <= 0) {
                    p.sendMessage(Messages.faction_bank_not_enough.language(p).queue());
                    return;
                }

                if (faction.getBalance() < Integer.parseInt(args[1])) {
                    p.sendMessage(Messages.faction_bank_not_enough.language(p).queue());
                    return;
                }
                FactionWithdrawCommand withdraw =
                        new FactionWithdrawCommand().withdrawFaction(
                                faction,
                                p,
                                Integer.parseInt(args[1])
                        );

                addCooldown(p);
                if (withdraw.transactionSuccessfully()) {
                    p.sendMessage(Messages.faction_bank_withdraw.language(p).queue().replace("%amount%", arg1 + ""));
                    Scoreboards.refresh(p);
                    faction.balanceHistory.add(0,
                            new HistoryEntrys.BalanceEntry(-arg1, p.getName(), new Date().getTime())
                    );
                }
            } catch (NumberFormatException ex) {
                p.sendMessage(Messages.faction_bank_number_error.language(p).queue());
            }
        } else {
            p.sendMessage(Messages.faction_bank_number_error.language(p).queue());
        }
    }

    public FactionWithdrawCommand withdrawFaction(Faction faction, Player p, int amount) {
        try {
            int SumSumAmount = faction.getBalance() - amount;
            HCFPlayer player = HCFPlayer.getPlayer(p);
            faction.setBalance(SumSumAmount);

            player.addMoney(amount);

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
