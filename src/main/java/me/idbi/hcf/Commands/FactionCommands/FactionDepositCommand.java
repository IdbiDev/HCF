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
        return null;
    }

    @Override
    public String getSyntax() {
        return "/faction deposit <amount>";
    }

    @Override
    public String getPermission() {
        return "factions.commands." + getName();
    }

    @Override
    public boolean hasPermission(Player p) {
        return p.hasPermission(getPermission());
    }

    @Override
    public boolean hasCooldown(Player p) {
        if(!SubCommand.commandCooldowns.get(this).containsKey(p)) return false;
        return SubCommand.commandCooldowns.get(this).get(p) > System.currentTimeMillis();
    }

    @Override
    public void addCooldown(Player p) {
        HashMap<Player, Long> hashMap = SubCommand.commandCooldowns.get(this);
        hashMap.put(p, System.currentTimeMillis() + (getCooldown() * 1000L));
        SubCommand.commandCooldowns.put(this, hashMap);
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
                int arg1 = args[1].matches("^[0-9]+$") ? Integer.parseInt(args[1]) : hcf.money;
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
