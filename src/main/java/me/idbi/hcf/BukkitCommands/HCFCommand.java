package me.idbi.hcf.BukkitCommands;

import me.idbi.hcf.CustomFiles.Messages.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Objects;

public abstract class HCFCommand implements CommandExecutor {

    private final CommandInfo commandInfo;

    private static HashMap<HCFCommand, HashMap<Player, Long>> commandCooldowns = new HashMap<>();

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
                if(hasCooldown(p)) {
                    long cooldown = commandCooldowns.get(this).get(p);
                    p.sendMessage(Messages.command_cooldown.language(p)
                            .setTime((cooldown / 1000 - System.currentTimeMillis() / 1000) + "").queue());
                    return false;
                }
                execute(p, args);
            } else if (sender instanceof ConsoleCommandSender) {
                ConsoleCommandSender ccs = (ConsoleCommandSender) sender;
                execute(ccs, args);
            }

        } catch (ArrayIndexOutOfBoundsException e) {
            printUsage(sender);
        }
        return true;
    }

    public void execute(Player player, String[] args) {};
    public void execute(ConsoleCommandSender ccs, String[] args) {};
    public abstract int getCooldown();
    public void addCooldown(Player p) {
        if (getCooldown() == 0) return;
        if (!commandCooldowns.containsKey(this))
            commandCooldowns.put(this, new HashMap<>());
        HashMap<Player, Long> asd = commandCooldowns.get(this);
        asd.put(p, getCooldown() * 1000L + System.currentTimeMillis());
        commandCooldowns.put(this, asd);
    }

    public boolean hasCooldown(Player p) {
        if (!commandCooldowns.containsKey(this)) {
            commandCooldowns.put(this, new HashMap<>());
            return false;
        }
        HashMap<Player, Long> cds = commandCooldowns.get(this);
        if(cds.containsKey(p)) {
            return cds.get(p) > System.currentTimeMillis();
        }
        return false;
    }

    public void printUsage(CommandSender p) {
        p.sendMessage("§cUsage: " + getCommandInfo().syntax());
    }
}
