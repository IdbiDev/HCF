package me.idbi.hcf.Commands;

import me.idbi.hcf.Commands.KothCommands.*;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class KothCommandManager implements CommandExecutor {

    private final ArrayList<SubCommand> subcommands = new ArrayList<>();

    public KothCommandManager() {
        subcommands.add(new KothSetRewardCommand());
        subcommands.add(new KothStartCommand());
        subcommands.add(new KothStopCommand());

        for (SubCommand command : subcommands) {
            SubCommand.commandCooldowns.put(command, new HashMap<>());
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("koth")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;

                if (args.length > 0) {
                    for (int i = 0; i < getSubcommands().size(); i++) {
                        try {
                            SubCommand cmd = getSubcommands().get(i);
                            if (cmd.isCommand(args[0])) {
                                if (cmd.hasPermission(p)) {
                                    if (!cmd.hasCooldown(p)) {
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
                } else {
                    p.sendMessage("§e§m--------------------------------");
                    for (int i = 0; i < getSubcommands().size(); i++) {
                        p.sendMessage(getSubcommands().get(i).getSyntax() + " - " + getSubcommands().get(i).getDescription());
                    }
                    p.sendMessage("§e§m--------------------------------");
                }

            }
        }
        return true;
    }

    public ArrayList<SubCommand> getSubcommands() {
        return subcommands;
    }
}
