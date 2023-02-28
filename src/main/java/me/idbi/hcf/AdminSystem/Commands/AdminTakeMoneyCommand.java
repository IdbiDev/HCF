package me.idbi.hcf.AdminSystem.Commands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Objects.PlayerStatistic;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class AdminTakeMoneyCommand extends SubCommand {
    @Override
    public String getName() {
        return "takemoney";
    }

    @Override
    public boolean isCommand(String argument) {
        return argument.equalsIgnoreCase(getName());
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getSyntax() {
        return "/admin " + getName() + " <player> <amount>";
    }

    @Override
    public String getPermission() {
        return "factions.command.admin." + getName();
    }

    @Override
    public boolean hasPermission(Player p) {
        return p.hasPermission(getPermission());
    }

    @Override
    public void perform(Player p, String[] args) {
        if (Bukkit.getPlayer(args[1]) == null) {
            p.sendMessage(Messages.not_found_player.language(p).queue());
            return;
        }
        if (!args[2].matches("^[0-9]+$")) {
            p.sendMessage(Messages.not_a_number.language(p).queue());
            return;
        }
        int amount = Integer.parseInt(args[2]);

        if (amount <= 0) {
            p.sendMessage(Messages.not_a_number.language(p).queue());
            return;
        }
        Player player = Bukkit.getPlayer(args[1]);
        HCFPlayer hcf = HCFPlayer.getPlayer(player);
        hcf.takeMoney(amount);

        player.sendMessage(Messages.take_money.language(p).setExecutor(p).setAmount(String.valueOf(amount)).queue());
        Scoreboards.refresh(player);
        PlayerStatistic stat = hcf.playerStatistic;
        stat.MoneySpend += amount;
    }
}
