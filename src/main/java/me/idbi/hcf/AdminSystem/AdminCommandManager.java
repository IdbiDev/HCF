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

public class AdminCommandManager implements CommandExecutor {

    private final ArrayList<SubCommand> subcommands = new ArrayList<>();

    public AdminCommandManager() {
        subcommands.add(new AdminChatCommand());
        subcommands.add(new AdminDeleteFactionCommand());
        subcommands.add(new AdminDepositCommand());
        subcommands.add(new AdminDutyCommand());
        subcommands.add(new AdminFreezeCommand());
        subcommands.add(new AdminGiveMoneyCommand());
        subcommands.add(new AdminKickPlayerCommand());
        subcommands.add(new AdminSetDTRCommand());
        subcommands.add(new AdminSetFactionLeaderCommand());
        subcommands.add(new AdminSetFactionNameCommand());
        subcommands.add(new AdminSetPlayerFactionCommand());
        subcommands.add(new AdminSpawnPlaceCommand());
        subcommands.add(new AdminTakeMoneyCommand());

        subcommands.add(new AdminVanishCommand());
        subcommands.add(new AdminWithdrawCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("admin")) {
            if (sender instanceof Player p) {

                if (args.length > 0) {
                    for (int i = 0; i < getSubcommands().size(); i++) {
                        try {
                            if (getSubcommands().get(i).isCommand(args[0])) {
                                if (getSubcommands().get(i).hasPermission(p)) {
                                    if (getSubcommands().get(i).getName().equalsIgnoreCase("chat")
                                            || getSubcommands().get(i).getName().equalsIgnoreCase("duty")) {
                                        getSubcommands().get(i).perform(p, args);
                                    } else {
                                        if (!Playertools.isInStaffDuty(p)) {
                                            p.sendMessage(Messages.not_in_duty.language(p).queue());
                                            return false;
                                        }
                                        getSubcommands().get(i).perform(p, args);
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
