package me.idbi.hcf.Commands;

import me.idbi.hcf.Commands.FactionCommands.*;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class FactionCommandManager implements CommandExecutor {

    private final ArrayList<SubCommand> subcommands = new ArrayList<>();

    public FactionCommandManager() {
        subcommands.add(new FactionChatCommand());
        subcommands.add(new FactionClaimCommand());
        subcommands.add(new FactionCreateCommand());
        subcommands.add(new FactionDepositCommand());
        subcommands.add(new FactionDisbandCommand());
        subcommands.add(new FactionHomeCommand());
        subcommands.add(new FactionInviteCommand());
        subcommands.add(new FactionJoinCommand());
        subcommands.add(new FactionKickCommand());
        subcommands.add(new FactionLeaveCommand());
        subcommands.add(new FactionStatsCommand());
        subcommands.add(new FactionStuckCommand());
        subcommands.add(new FactionShowCommand());
        subcommands.add(new FactionTransferCommand());
        subcommands.add(new FactionWithdrawCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("faction")) {
            if (sender instanceof Player p) {

                if (args.length > 0) {
                    for (int i = 0; i < getSubcommands().size(); i++) {
                        try {
                            if (getSubcommands().get(i).isCommand(args[0])) {
                                if (getSubcommands().get(i).hasPermission(p)) {
                                    getSubcommands().get(i).perform(p, args);
                                } else {
                                    p.sendMessage(Messages.no_permission.language(p).queue());
                                }
                            }
                        } catch (IndexOutOfBoundsException e) {
                            p.sendMessage("§cUsage: " + getSubcommands().get(i).getSyntax());
                            p.sendMessage(Messages.missing_argument.language(p).queue());
                        } catch (Exception e) {
                            p.sendMessage(Messages.error_while_executing.language(p).queue());
                        }
                    }
                } else if (args.length == 0) {
                    p.sendMessage("--------------------------------");
                    for (int i = 0; i < getSubcommands().size(); i++) {
                        p.sendMessage(getSubcommands().get(i).getSyntax() + " - " + getSubcommands().get(i).getDescription());
                    }
                    p.sendMessage("--------------------------------");
                }

            }
        }
        return true;
    }

    public ArrayList<SubCommand> getSubcommands() {
        return subcommands;
    }
}
