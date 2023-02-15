package me.idbi.hcf.tools;

import me.idbi.hcf.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;
import java.util.HashMap;

public class SQL_Connection {

    private static Main m = Main.getPlugin(Main.class);
    // Adatbázis kapcsolat... host port databae user password >> Nem nagy cucc
    public static Connection dbConnect(String host, String port, String database, String username, String password) {
        Connection con = null;
        try {
        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection(
                "jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
        if (con.isValid(0)) {
            if (Main.debug)
                Main.sendCmdMessage(ChatColor.GREEN+"Sucessfully connected to the database!");
        }
    } catch (Exception e) {
            Main.sendCmdMessage(ChatColor.DARK_RED + "Invaild Database connection... shutting down!");
            Bukkit.getServer().shutdown();
    }
        return con;
}

    // Sima query futtatás... args-t használ

    public static void dbExecute(Connection con, String query, String... args) {
        new BukkitRunnable(){
            @Override
            public void run() {
                String private_string_query = "";
                int c_arg = 0;
                for (char c : query.toCharArray()) {
                    if (c == '?') {
                        private_string_query += args[c_arg];
                        c_arg++;
                    } else {
                        private_string_query += c;
                    }
                }
                // Az elkészített qh-t dobni az sqlnek
                try {
                    PreparedStatement st = con.prepareStatement(private_string_query);
                    st.executeUpdate();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(m);
    }
    public static int dbSyncExec(Connection con, String query, String... args) {
                String private_string_query = "";
                int c_arg = 0;
                for (char c : query.toCharArray()) {
                    if (c == '?') {
                        private_string_query += args[c_arg];
                        c_arg++;
                    } else {
                        private_string_query += c;
                    }
                }
                // Az elkészített qh-t dobni az sqlnek
                try {
                    PreparedStatement st = con.prepareStatement(private_string_query,Statement.RETURN_GENERATED_KEYS);
                    st.executeUpdate();
                    ResultSet genkys = st.getGeneratedKeys();
                    if (genkys != null && genkys.next()) {
                        return ((int) genkys.getLong(1));
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return 0;
    }

    public static HashMap<String, Object> dbPoll(Connection con, String query, String... args) {
        HashMap<String, Object> poll_qh = new HashMap<String, Object>();
                // '?'-ek replacelése args-al
                String private_string_query = "";
                int c_arg = 0;
                for (char c : query.toCharArray()) {
                    if (c == '?') {
                        private_string_query += args[c_arg];
                        c_arg++;
                    } else {
                        private_string_query += c;
                    }
                }
                // Hashmap-be elmentés


                Statement poll_stm = null;
                try {
                    poll_stm = con.createStatement();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                ResultSet rs = null;
                try {
                    rs = poll_stm.executeQuery(private_string_query);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                ResultSetMetaData metaData = null;
                try {
                    metaData = rs.getMetaData();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                int colCount = 0;
                try {
                    colCount = metaData.getColumnCount();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                // Key == oszlopnév; value == érték
                while (true) {
                    try {
                        if (!rs.next()) break;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    for (int i = 1; i <= colCount; i++) {
                        try {
                            poll_qh.put(metaData.getColumnLabel(i), rs.getObject(i));
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }

        return poll_qh;

    }

    // Kobának bruh..          tábla név, Listába a keyek, valuek
    public static int dbInsert(Connection con, String table, String[] keys, String[] values) {
        String private_string_query = "INSERT INTO " + table + "(" + String.join(",", keys) + ") VALUES (" + String.join(",", values) + ")";
        Statement st = null;
        try {
            st = con.createStatement();
            return st.executeUpdate(private_string_query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Kobának bruh..          tábla név, Listába a keyek, valuek; whereclause pl: "x=1 AND y=2" tehát a 'WHERE' állítás nem kell
    public static int dbUpdate(Connection con, String table, String[] keys, String[] values, String whereClause) {
        String private_string_query = "UPDATE " + table + "SET ";
        for (int i = 0; i < keys.length; i++) {
            private_string_query += keys[i] + "=" + values[i];
        }
        private_string_query += " WHERE " + whereClause;
        try {
            Statement st = con.createStatement();
            return st.executeUpdate(private_string_query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}