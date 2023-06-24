package me.idbi.hcf.Commands.FactionCommands;

import me.idbi.hcf.ClickableMessages.ClickableFactions;
import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.FactionListGUI.FactionToplistInventory;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FactionTopCommand extends SubCommand {

    @Override
    public String getName() {
        return "top";
    }

    @Override
    public boolean isCommand(String argument) {
        return argument.equalsIgnoreCase(getName());
    }

    @Override
    public String getDescription() {
        return "Sorts the top factions by the selected filter";
    }

    @Override
    public String getSyntax() {
        return "/faction top [points | balance | kills | [page]]";
    }

    @Override
    public int getCooldown() {
        return 2;
    }

    @Override
    public void perform(Player p, String[] args) {
        ArrayList<Faction> factions = Playertools.sortByPoints();
        String type = "Points";
        if(args.length == 1) {
            try {
                //List<Faction> newFactions = factions.subList(0, Math.min(factions.size(), 10));
                run(p, factions, 1, type);
                //printFactions(p, newFactions, "Points");
            } catch (IndexOutOfBoundsException e) {
                p.sendMessage(Messages.cant_find_page.language(p).queue());
            }
            return;
        }

        if(SortTypes.equals(args[1], SortTypes.BALANCE)) {
            factions = Playertools.sortByBalance();
            type = "Balance";
        } else if(SortTypes.equals(args[1], SortTypes.KILLS)) {
            factions = Playertools.sortByKills();
            type = "Kills";
        }

        if(Playertools.isInt(args[1])) {
            int page = Integer.parseInt(args[1]);
            run(p, factions, page, type);

        } else if(args.length == 3) {
            if(Playertools.isInt(args[2])) {
                int page = Integer.parseInt(args[2]);
                run(p, factions, page, type);
            }
        } else {

            run(p, factions, 1, type);
        }
    }

    private void printFactions(Player p, List<Faction> factions, String type) {
        int count = 1;
        p.sendMessage(Messages.faction_top_title.language(p).setType(type).queue());
        for (Faction f : factions) {
            if(type.equalsIgnoreCase("Kills"))
                ClickableFactions.sendTop(p, f, count, f.getKills());
            else if(type.equalsIgnoreCase("Points"))
                ClickableFactions.sendTop(p, f, count, f.getPoints());
            else if(type.equalsIgnoreCase("Balance"))
                ClickableFactions.sendTop(p, f, count, f.getBalance());
            count++;
        }
    }

    private void run(Player p, List<Faction> factions, int page, String type) {
        if (!(page > 0)) {
            p.sendMessage(Messages.cant_find_page.language(p).queue());
            return;
        }
        boolean hasNext = false;
        if(factions.size() > 10 && factions.size() > (page * 10 + 10))
            hasNext = true;

        p.openInventory(FactionToplistInventory.topInv(p, factions, type, 1));
        if(page == 1) {
            List<Faction> newFactions = factions.subList(0, Math.min(factions.size(), 10));
            printFactions(p, newFactions, type);
            ClickableFactions.sendPage(p, page, hasNext, false, "/f top " + type.toLowerCase());
            return;
        }
        try {
            if (page * 10 <= factions.size()) {
                List<Faction> pageFactions = factions.subList(page * 10, Math.min(page * 10 + 10, factions.size()));
                printFactions(p, pageFactions, type);
                ClickableFactions.sendPage(p, page, hasNext, true, "/f top " + type.toLowerCase());
            } else {
                p.sendMessage(Messages.cant_find_page.language(p).queue());
            }
        } catch (IndexOutOfBoundsException e) {
            p.sendMessage(Messages.cant_find_page.language(p).queue());
        }
    }

    private static enum SortTypes {
        BALANCE(Arrays.asList("money", "bal", "balance", "b")),
        KILLS(Arrays.asList("kills", "kill", "k"));

        List<String> names;

        SortTypes(List<String> names) {
            this.names = names;
        }

        public static boolean equals(String value, SortTypes type) {
            return type.names.contains(value.toLowerCase());
        }
    }
}
