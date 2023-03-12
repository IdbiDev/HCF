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
import java.util.HashMap;

public class AllyCommandManager implements CommandExecutor {

    private final ArrayList<SubCommand> subcommands = new ArrayList<>();

    public AllyCommandManager() {
        subcommands.add(new AllyAcceptCommand());
        subcommands.add(new AllyDeclineCommand());
        subcommands.add(new AllyInviteCommand());
        subcommands.add(new AllyResolveCommand());

        for (SubCommand command : subcommands) {
            SubCommand.commandCooldowns.put(command, new HashMap<>());
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("ally")) {
            if (sender instanceof Player p) {

                if (args.length > 0) {
                    for (int i = 0; i < getSubcommands().size(); i++) {
                        try {
                            SubCommand cmd = getSubcommands().get(i);
                            if (cmd.isCommand(args[0])) {
                                if (cmd.hasPermission(p)) {
                                    if(!cmd.hasCooldown(p)) {
                                        cmd.perform(p, args);
                                    } else {
                                        long cooldown = SubCommand.commandCooldowns.get(cmd).get(p);
                                        p.sendMessage(Messages.command_cooldown.language(p)
                                                .setTime((cooldown / 1000 - System.currentTimeMillis() / 1000) + "").queue());
                                    }
                                } else {
                                    p.sendMessage(Messages.no_permission.language(p).queue());
                                }
                            }
                        } catch (IndexOutOfBoundsException e) {
                            p.sendMessage("§cUsage: " + getSubcommands().get(i).getSyntax());
                            p.sendMessage(Messages.missing_argument.language(p).queue());
                        } catch (Exception e) {
                            e.printStackTrace();
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
