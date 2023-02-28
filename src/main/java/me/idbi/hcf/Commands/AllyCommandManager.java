package me.idbi.hcf.Commands;

import me.idbi.hcf.Commands.AllyCommands.AllyAcceptCommand;
import me.idbi.hcf.Commands.AllyCommands.AllyDeclineCommand;
import me.idbi.hcf.Commands.AllyCommands.AllyInviteCommand;
import me.idbi.hcf.Commands.AllyCommands.AllyResolveCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class AllyCommandManager implements CommandExecutor {

    private final ArrayList<SubCommand> subcommands = new ArrayList<>();

    public AllyCommandManager() {
        subcommands.add(new AllyAcceptCommand());
        subcommands.add(new AllyDeclineCommand());
        subcommands.add(new AllyInviteCommand());
        subcommands.add(new AllyResolveCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("ally")) {
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
