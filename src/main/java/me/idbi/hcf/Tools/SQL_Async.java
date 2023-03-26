package me.idbi.hcf.Tools;

import me.idbi.hcf.Main;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class SQL_Async {

    private static final Main m = Main.getPlugin(Main.class);

    public static CompletableFuture<HashMap<String, Object>> dbPollAsync(Connection con, String query, String... args) {
        CompletableFuture<HashMap<String, Object>> result = new CompletableFuture<>();
        new BukkitRunnable() {
            @Override
            public void run() {
                HashMap<String, Object> qp_result = new HashMap<>();
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
                    try {
                        poll_stm.close();
                    } catch (SQLException ignored) {}
                    result.completeExceptionally(e);
                }
                assert poll_stm != null;
                ResultSet rs = null;
                try {
                    rs = poll_stm.executeQuery(private_string_query);
                } catch (SQLException e) {
                    e.printStackTrace();
                    result.completeExceptionally(e);
                }

                ResultSetMetaData metaData = null;

                try {
                    assert rs != null;
                    metaData = rs.getMetaData();
                } catch (SQLException e) {
                    e.printStackTrace();
                    result.completeExceptionally(e);
                }
                assert metaData != null;
                int colCount = 0;
                try {
                    colCount = metaData.getColumnCount();
                } catch (SQLException e) {
                    e.printStackTrace();
                    result.completeExceptionally(e);
                }
                // Key == oszlopnév; value == érték
                while (true) {
                    try {
                        if (!rs.next()) break;
                    } catch (SQLException e) {
                        e.printStackTrace();
                        result.completeExceptionally(e);
                    }
                    for (int i = 1; i <= colCount; i++) {
                        try {
                            qp_result.put(metaData.getColumnLabel(i), rs.getObject(i));
                            result.complete(qp_result);
                        } catch (SQLException e) {
                            e.printStackTrace();
                            result.completeExceptionally(e);
                        }
                    }
                }
                try {
                    poll_stm.close();
                    rs.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }.runTaskAsynchronously(m);
        return result;
    }

    public static CompletableFuture<Integer> dbExecuteAsync(Connection con, String query, String... args) {
        CompletableFuture<Integer> result = new CompletableFuture<>();
        new BukkitRunnable() {
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
                    PreparedStatement st = con.prepareStatement(private_string_query, Statement.RETURN_GENERATED_KEYS);
                    st.executeUpdate();
                    ResultSet genkys = st.getGeneratedKeys();
                    if (genkys != null && genkys.next()) {
                        result.complete((int) genkys.getLong(1));
                    }
                    st.close();
                    assert genkys != null;
                    genkys.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    result.completeExceptionally(e);
                }

            }
        }.runTaskAsynchronously(m);
        return result;
    }

}
