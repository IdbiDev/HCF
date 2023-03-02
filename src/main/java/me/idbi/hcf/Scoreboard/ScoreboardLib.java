package me.idbi.hcf.Scoreboard;


//import com.comphenix.packetwrapper.WrapperPlayServerScoreboardTeam;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ScoreboardLib extends BukkitRunnable {

    private Scoreboard board;
    private Objective obj;
    private Team[] teams;
    private ScoreboardTextProvider textProvider;
    private List<String> players = new ArrayList<>();
    private List<String> rows;
    private int max;

    private Map<String, List<ScoreboardRow>> prev = null;
    private Map<String, List<ScoreboardRow>> send = new HashMap<>();

    private static final Pattern regex = Pattern.compile("§[0-9a-f]§r");

    private static List<ScoreboardLib> scoreboardHeap = new ArrayList<>();

    private ScoreboardLib(Plugin plugin, ScoreboardTextProvider textProvider, int max) {
        this.board = Bukkit.getScoreboardManager().getNewScoreboard();
        this.textProvider = textProvider;

        obj = board.getObjective("ScoreboardLib");
        if (obj == null)
            obj = board.registerNewObjective("ScoreboardLib", "dummy");

        setTitle(textProvider.getScoreboardTitle());
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        if (max > 16)
            max = 16;
        this.max = max;

        teams = new Team[max];

        for (int i = 0; i < max; i++) {
            teams[i] = board.getTeam("team" + (i + 1));
            if (teams[i] == null) {
                teams[i] = board.registerNewTeam("team" + (i + 1));
                teams[i].addPlayer(Bukkit.getOfflinePlayer(ChatColor.values()[i].toString() + ChatColor.RESET));
            }
        }

        setList(textProvider.getScoreboardText());

        ProtocolLibrary.getProtocolManager().addPacketListener(
                new PacketAdapter(plugin, PacketType.Play.Server.SCOREBOARD_SCORE) {
                    @Override
                    public void onPacketSending(PacketEvent event) {
                        PacketContainer packet = event.getPacket();

                        if (packet.getScoreboardActions().read(0) == EnumWrappers.ScoreboardAction.REMOVE &&
                                regex.matcher(packet.getStrings().read(0)).matches() &&
                                !players.contains(event.getPlayer().getName())) {
                            event.setCancelled(true);
                        }
                    }
                }
        );
    }

    private ScoreboardLib setList(List<String> list) {
        if (list.size() > max)
            list = list.subList(0, max);

        rows = new ArrayList<>(list);

        return this;
    }

    private ScoreboardLib setTitle(String title) {
        if (title.length() > 32)
            title = title.substring(0, 32);

        if (!obj.getDisplayName().equals(title))
            obj.setDisplayName(title);

        return this;
    }

    @Override
    public void run() {
        Iterator<String> iter = players.iterator();
        while (iter.hasNext()) {
            Player p = Bukkit.getPlayerExact(iter.next());
            if (p == null)
                iter.remove();
        }

        if (players.size() == 0)
            return;     //wait until someone shows up

        grabNewRows();

        prev = new HashMap<>(send);
        send.clear();
        players.forEach(p -> formatRows(Bukkit.getPlayerExact(p)));

        updateRows();
    }

    private void grabNewRows() {
        if (!textProvider.getScoreboardTitle().equals(obj.getDisplayName())) {
            setTitle(textProvider.getScoreboardTitle());
        }

        setList(textProvider.getScoreboardText());

    }

    private void formatRows(Player p) {
        send.put(p.getName(),
                rows.stream()
                        .map(row -> new ScoreboardRow(textProvider.format(p, row)))
                        .collect(Collectors.toList())
        );
    }

    private void updateRows() {
        int size = rows.size() <= max ? rows.size() : max;

        for (int i = 0; i < rows.size(); i++) {
            updateRow(size, i);
        }

        for (int i = teams.length; i > rows.size(); i--)
            board.resetScores(teams[i - 1].getPlayers().iterator().next());
    }

    private void updateRow(int size, int num) {
        Team team = teams[size - num - 1];
        Score score = obj.getScore(team.getPlayers().iterator().next());

        players.stream()
                .map(Bukkit::getPlayerExact)
                .forEach(p -> {
                    /*if(!send.containsKey(p.getName()))
                        return;*/

                    ScoreboardRow row = send.get(p.getName()).get(num);

                    if(prev == null || !prev.containsKey(p.getName()) || !row.equals(prev.get(p.getName()).get(num))) {
                        /*WrapperPlayServerScoreboardTeam wrapper = new WrapperPlayServerScoreboardTeam();

                        wrapper.setPlayers(Collections.emptyList());
                        wrapper.setMode(WrapperPlayServerScoreboardTeam.Mode.TEAM_UPDATED);
                        wrapper.setName(team.getName());
                        wrapper.setDisplayName(team.getName());
                        wrapper.setPrefix(row.getPrefix());
                        wrapper.setSuffix(row.getSuffix());

                        wrapper.sendPacket(p);*/
                    }
                });

        if (score.getScore() != size - num)
            score.setScore(size - num);
    }

    public boolean hasPlayer(Player p) {
        return players.contains(p.getName());
    }

    /**
     * Add player to scoreboard
     *
     * @param p Player to be added
     */

    public void addPlayer(Player p) {
        scoreboardHeap.stream()
                .filter(sc -> sc.hasPlayer(p))
                .forEach(sc -> sc.removePlayer(p));

        players.add(p.getName());
        formatRows(p);
        p.setScoreboard(board);
        updateRows();
    }

    /**
     * Remove player from scoreboard. He will be returned to main scoreboard
     *
     * @param p Player to be removed
     */

    public void removePlayer(Player p) {
        players.remove(p.getName());
        removePlayer0(p);
    }

    private void removePlayer0(Player p) {
        send.remove(p.getName());

        p.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
    }

    /**
     * Create scoreboard system with all available parameters
     *
     * @param pl           Plugin for scheduler
     * @param textProvider Implementation for ScoreboardTextProvider interface
     * @param max          Max count of rows in scoreboard (reduction may slightly improve performance), default 16
     * @param delay        Ticks between scoreboard updating
     * @return NoFlash object
     */

    public static ScoreboardLib start(Plugin pl, ScoreboardTextProvider textProvider, int max, long delay) {
        ScoreboardLib nf = new ScoreboardLib(pl, textProvider, max);
        scoreboardHeap.add(nf);
        //nf.runTaskTimer(pl, 1L, delay);
        return nf;
    }

    /**
     * Create scoreboard system with new scoreboard and all 16 scoreboard rows available
     *
     * @param pl           Plugin for scheduler
     * @param textProvider Implementation for ScoreboardTextProvider interface
     * @param delay        Ticks between scoreboard updating
     * @return NoFlash object
     */

    @SuppressWarnings("SameParameterValue")
    public static ScoreboardLib start(Plugin pl, ScoreboardTextProvider textProvider, long delay) {
        return start(pl, textProvider, 16, delay);
    }

    /**
     * Stop scoreboard system. All players will be returned to main scoreboard
     */

    public void stop() {
        this.cancel();
        obj.unregister();

        Iterator<String> iter = players.iterator();
        while (iter.hasNext()) {
            String p = iter.next();
            iter.remove();
            removePlayer0(Bukkit.getPlayerExact(p));
        }

        scoreboardHeap.remove(this);
    }

    /**
     * Interface to get new title & rows and to format rows for the specified player
     */

    public interface ScoreboardTextProvider {

        List<String> getScoreboardText();

        String getScoreboardTitle();

        String format(Player p, String row);

    }

    private class ScoreboardRow {

        private String prefix, suffix;

        public ScoreboardRow(String row) {
            if (row.length() <= 16) {
                prefix = row;
                suffix = "";
            } else {     //up to 16+16, color pair is in single part
                int cut = findCutPoint(row);
                prefix = row.substring(0, cut);
                suffix = continueColors(prefix) + row.substring(cut, row.length());

                if (suffix.length() > 16) {
                    suffix = suffix.substring(0, 16);
                }
            }
        }

        private int findCutPoint(String s) {
            for (int i = 16; i > 0; i--) {
                if (s.charAt(i - 1) == ChatColor.COLOR_CHAR && ChatColor.getByChar(s.charAt(i)) != null)
                    continue;
                return i;
            }
            return 16;
        }

        private String continueColors(String prefix) {
            ChatColor activeColor = null;
            Set<ChatColor> formats = new HashSet<>();

            for (int i = 0; i < prefix.length() - 1; i++) {
                char c1 = prefix.charAt(i);
                char c2 = prefix.charAt(i + 1);

                ChatColor color = ChatColor.getByChar(c2);
                if (c1 == ChatColor.COLOR_CHAR && color != null) {
                    if (color == ChatColor.RESET) {
                        activeColor = null;
                        formats.clear();
                    } else if (color.isColor()) {
                        activeColor = color;
                    } else {
                        formats.add(color);
                    }
                }
            }

            StringBuffer sb = new StringBuffer();

            if (activeColor != null)
                sb.append(activeColor.toString());
            formats.forEach(format -> sb.append(format.toString()));

            return sb.toString();
        }

        public String getPrefix() {
            return prefix;
        }

        public String getSuffix() {
            return suffix;
        }

        @Override
        public String toString() {
            return prefix + suffix;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ScoreboardRow that = (ScoreboardRow) o;

            return prefix.equals(that.prefix) && suffix.equals(that.suffix);
        }

        @Override
        public int hashCode() {
            int result = prefix.hashCode();
            result = 31 * result + suffix.hashCode();
            return result;
        }
    }


}
