package me.idbi.hcf.AdminSystem;

import me.idbi.hcf.AdminSystem.Commands.*;
import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminCommandManager implements CommandExecutor {

    private final static ArrayList<SubCommand> subcommands = new ArrayList<>();

    public AdminCommandManager() {
        subcommands.add(new AdminChatCommand());
        subcommands.add(new AdminDeleteFactionCommand());
        subcommands.add(new AdminDepositCommand());
        subcommands.add(new AdminDutyCommand());
        subcommands.add(new AdminFreezeCommand());
        subcommands.add(new AdminGiveMoneyCommand());
        subcommands.add(new AdminHelpCommand());
        subcommands.add(new AdminKickPlayerCommand());
        subcommands.add(new AdminReclaimResetCommand());
        subcommands.add(new AdminReloadCommand());
        subcommands.add(new AdminSetDTRCommand());
        subcommands.add(new AdminSetFactionLeaderCommand());
        subcommands.add(new AdminSetFactionNameCommand());
        subcommands.add(new AdminSetPlayerFactionCommand());
        subcommands.add(new AdminSpawnPlaceCommand());
        subcommands.add(new AdminTakeMoneyCommand());
        subcommands.add(new AdminDTRFreezeCommand());

        subcommands.add(new AdminVanishCommand());
        subcommands.add(new AdminWithdrawCommand());

        // Ennek itt kell lennie!!!44!!
        subcommands.add(new AdminItemsCommand());

        for (SubCommand command : subcommands) {
            SubCommand.commandCooldowns.put(command, new HashMap<>());
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("admin")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;

                if (args.length > 0) {
                    AdminItemsCommand items = new AdminItemsCommand();
                    if (items.isCommand(args[0])) {
                        try {
                            items.perform(sender, args);
                            return false;
                        } catch (IndexOutOfBoundsException e) {
                            sender.sendMessage("§cUsage: " + items.getSyntax());
                            sender.sendMessage(Messages.missing_argument.queue());
                        } catch (Exception e) {
                            sender.sendMessage(Messages.error_while_executing.queue());
                            e.printStackTrace();
                        }
                    }
                    for (int i = 0; i < getSubcommands().size(); i++) {
                        try {
                            SubCommand cmd = getSubcommands().get(i);
                            if (cmd.isCommand(args[0])) {
                                if (cmd.hasPermission(p)) {
                                    if (cmd.getName().equalsIgnoreCase("chat")
                                            || cmd.getName().equalsIgnoreCase("duty")) {
                                        cmd.perform(p, args);
                                    } else {
                                        if (!Playertools.isInStaffDuty(p)) {
                                            p.sendMessage(Messages.not_in_duty.language(p).queue());
                                            return false;
                                        }
                                        cmd.perform(p, args);
                                    }
                                } else {
                                    p.sendMessage(Messages.no_permission.language(p).queue());
                                }
                            }
                        } catch (IndexOutOfBoundsException e) {
                            p.sendMessage("§cUsage: " + getSubcommands().get(i).getSyntax());
                            p.sendMessage(Messages.missing_argument.language(p).queue());
                        } catch (Exception e) {
                            p.sendMessage(Messages.error_while_executing.language(p).queue());
                            e.printStackTrace();
                        }
                    }
                } else if (args.length == 0) {
                    AdminDutyCommand.duty(p);
                }
            }
        }
        return true;
    }

    public static ArrayList<SubCommand> getSubcommands() {
        return subcommands;
    }
}
