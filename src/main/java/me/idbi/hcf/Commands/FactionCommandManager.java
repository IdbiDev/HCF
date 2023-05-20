package me.idbi.hcf.Commands;

import me.idbi.hcf.Commands.FactionCommands.*;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FactionCommandManager implements CommandExecutor, TabCompleter {

    private final ArrayList<SubCommand> subcommands = new ArrayList<>();

    public FactionCommandManager() {
        subcommands.add(new FactionChatCommand());
        subcommands.add(new FactionChatToggleCommand());
        subcommands.add(new FactionClaimCommand());
        subcommands.add(new FactionCreateCommand());
        subcommands.add(new FactionDepositCommand());
        subcommands.add(new FactionDisbandCommand());
        subcommands.add(new FactionHomeCommand());
        subcommands.add(new FactionInviteCommand());
        subcommands.add(new FactionJoinCommand());
        subcommands.add(new FactionKickCommand());
        subcommands.add(new FactionLeaveCommand());
        subcommands.add(new FactionListCommand());
        subcommands.add(new FactionManageCommand());
        subcommands.add(new FactionMapCommand());
        subcommands.add(new FactionRallyCommand());
        subcommands.add(new FactionSetHomeCommand());
        subcommands.add(new FactionShowCommand());
        subcommands.add(new FactionStatsCommand());
        subcommands.add(new FactionStuckCommand());
        subcommands.add(new FactionTeamFocusCommand());
        subcommands.add(new FactionTopCommand());
        subcommands.add(new FactionTransferCommand());
        subcommands.add(new FactionUnclaimCommand());
        subcommands.add(new FactionUninviteCommand());
        subcommands.add(new FactionUnrallyCommand());
        subcommands.add(new FactionUnteamFocusCommand());
        subcommands.add(new FactionWithdrawCommand());
        subcommands.add(new FactionStartEventCommand());
        subcommands.add(new FactionPromoteCommand());
        subcommands.add(new FactionDemoteCommand());
        subcommands.add(new FactionRenameCommand());
        subcommands.add(new FactionTopGuiCommand());

        for (SubCommand command : subcommands) {
            SubCommand.commandCooldowns.put(command, new HashMap<>());
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("faction")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;

                if (args.length == 0) {
                    showSubcommands(p);
                    return true;
                }

                for (int i = 0; i < getSubcommands().size(); i++) {
                    try {
                        SubCommand cmd = getSubcommands().get(i);
                        if (cmd.isCommand(args[0])) {
                            if (!cmd.hasPermission(p)) {
                                p.sendMessage(Messages.no_permission.language(p).queue());
                                return true;
                            }

                            if (cmd.hasCooldown(p)) {
                                long cooldown = SubCommand.commandCooldowns.get(cmd).get(p);
                                p.sendMessage(Messages.command_cooldown.language(p)
                                        .setTime((cooldown / 1000 - System.currentTimeMillis() / 1000) + "").queue());
                                return true;
                            }

                            cmd.perform(p, args);
                        }
                    } catch (IndexOutOfBoundsException e) {
                        p.sendMessage("§cUsage: " + getSubcommands().get(i).getSyntax());
                        e.printStackTrace();
                        p.sendMessage(Messages.missing_argument.language(p).queue());
                    } catch (Exception e) {
                        e.printStackTrace();
                        p.sendMessage(Messages.error_while_executing.language(p).queue());
                    }
                }
            }
        }
        return true;
    }

    public ArrayList<SubCommand> getSubcommands() {
        return subcommands;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(command.getName().equalsIgnoreCase("faction")) {
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

    private void showSubcommands(Player player) {
        player.sendMessage("--------------------------------");
        for (SubCommand cmd : getSubcommands()) {
            player.sendMessage(cmd.getSyntax() + " - " + cmd.getDescription());
        }
        player.sendMessage("--------------------------------");
    }
}
