package me.idbi.hcf.Commands;

import me.idbi.hcf.Commands.LivesCommand.LivesGetCommand;
import me.idbi.hcf.Commands.LivesCommand.LivesGiveCommand;
import me.idbi.hcf.Commands.LivesCommand.LivesSetCommand;
import me.idbi.hcf.Commands.LivesCommand.LivesTakeCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LivesCommandManager implements CommandExecutor, TabCompleter {

    private final ArrayList<SubCommand> subcommands = new ArrayList<>();

    public LivesCommandManager() {
        subcommands.add(new LivesGiveCommand());
        subcommands.add(new LivesSetCommand());
        subcommands.add(new LivesTakeCommand());
        subcommands.add(new LivesGetCommand());

        for (SubCommand command : subcommands) {
            SubCommand.commandCooldowns.put(command, new HashMap<>());
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("lives")) {
            //if (sender instanceof Player p) {

                if (args.length == 0) {
                    showSubcommands(sender);
                    return true;
                }

                for (int i = 0; i < getSubcommands().size(); i++) {
                    try {
                        SubCommand cmd = getSubcommands().get(i);
                        if (!cmd.isCommand(args[0])) continue;
                        if(sender instanceof Player p) {
                            if (!cmd.hasPermission(p)) {
                                p.sendMessage(Messages.no_permission.language(p).queue());
                                return true;
                            }
                        }

                        if(sender instanceof Player p) {
                            if (cmd.hasCooldown(p)) {
                                long cooldown = SubCommand.commandCooldowns.get(cmd).get(p);
                                p.sendMessage(Messages.command_cooldown.language(p)
                                        .setTime((cooldown / 1000 - System.currentTimeMillis() / 1000) + "").queue());
                                return true;
                            }
                        }

                        cmd.perform(sender, args);
                        //cmd.perform(sender);
                    } catch (IndexOutOfBoundsException e) {
                        sender.sendMessage("§cUsage: " + getSubcommands().get(i).getSyntax());
                        e.printStackTrace();
                        if(sender instanceof Player p)
                            sender.sendMessage(Messages.missing_argument.language(p).queue());
                        else
                            sender.sendMessage(Messages.missing_argument.queue());
                    } catch (Exception e) {
                        e.printStackTrace();
                        if(sender instanceof Player p)
                            sender.sendMessage(Messages.error_while_executing.language(p).queue());
                        else sender.sendMessage(Messages.error_while_executing.queue());
                    }
                }
           // }
        }
        return true;
    }

    public ArrayList<SubCommand> getSubcommands() {
        return subcommands;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(command.getName().equalsIgnoreCase("lives")) {
            if(args.length == 1) {
                List<String> lista = new ArrayList<>();
                for (SubCommand subcommand : this.getSubcommands()) {
                    lista.add(subcommand.getName());
                }
                return lista;
            }
        }
        return null;
    }

    private void showSubcommands(CommandSender player) {
        player.sendMessage("--------------------------------");
        for (SubCommand cmd : getSubcommands()) {
            player.sendMessage(cmd.getSyntax() + " - " + cmd.getDescription());
        }
        player.sendMessage("--------------------------------");
    }
}
