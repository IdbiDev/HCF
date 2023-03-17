package me.idbi.hcf.BukkitCommands;

import me.idbi.hcf.CustomFiles.Messages.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class HCFCommand implements CommandExecutor {

    private final CommandInfo commandInfo;

    public HCFCommand() {
        commandInfo = getClass().getDeclaredAnnotation(CommandInfo.class);
        Objects.requireNonNull(commandInfo, "Commands must have CommandInfo annotations");
    }

    public CommandInfo getCommandInfo() {
        return commandInfo;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (!commandInfo.permission().isEmpty()) {
                    if (!sender.hasPermission(commandInfo.permission())) {
                        sender.sendMessage(Messages.no_permission.language(p).queue());
                        return true;
                    }
                }
                execute(p, args);
            } else if (sender instanceof ConsoleCommandSender) {
                ConsoleCommandSender ccs = (ConsoleCommandSender) sender;
                execute(ccs, args);
            }

        } catch (ArrayIndexOutOfBoundsException e) {
            sender.sendMessage("§cUsage: " + getCommandInfo().syntax());
        }
        return true;
    }

    public void execute(Player player, String[] args) {};
    public void execute(ConsoleCommandSender ccs, String[] args) {};
}
