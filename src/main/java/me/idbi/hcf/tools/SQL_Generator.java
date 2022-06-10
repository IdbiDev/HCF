package me.idbi.hcf.tools;

import me.idbi.hcf.Main;

import java.sql.Connection;

public class SQL_Generator {
    private static final Connection con = Main.getConnection("SQL_GENERATOR");
    private final String[] tables = {
            "CREATE TABLE IF NOT EXISTS `factions` (\n" +
                    "  `ID` int(255) NOT NULL AUTO_INCREMENT,\n" +
                    "  `name` varchar(255) NOT NULL,\n" +
                    "  `money` int(255) NOT NULL DEFAULT 0,\n" +
                    "  `home` text CHARACTER SET utf8 COLLATE utf8_hungarian_ci DEFAULT NULL,\n" +
                    "  `DTR` int(11) NOT NULL DEFAULT 3,\n" +
                    "  `leader` text NOT NULL,\n" +
                    "  PRIMARY KEY (`ID`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;",
            "CREATE TABLE IF NOT EXISTS `members` (\n" +
                    "  `ID` int(255) NOT NULL AUTO_INCREMENT,\n" +
                    "  `name` varchar(255) NOT NULL,\n" +
                    "  `faction` int(255) NOT NULL DEFAULT 0,\n" +
                    "  `rank` varchar(255) CHARACTER SET utf8 COLLATE utf8_hungarian_ci DEFAULT 'alap',\n" +
                    "  `kills` int(255) NOT NULL DEFAULT 0,\n" +
                    "  `deaths` int(255) NOT NULL DEFAULT 0,\n" +
                    "  `money` int(255) NOT NULL DEFAULT 5000,\n" +
                    "  `uuid` text DEFAULT NULL,\n" +
                    "  `online` int(2) NOT NULL DEFAULT 0,\n" +
                    "  `factionname` varchar(255) NOT NULL DEFAULT 'Nincs',\n" +
                    "  PRIMARY KEY (`ID`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;",
            "CREATE TABLE IF NOT EXISTS `deathbans` (\n" +
                    "  `ID` int(255) NOT NULL AUTO_INCREMENT,\n" +
                    "  `uuid` text NOT NULL,\n" +
                    "  `time` bigint(255) NOT NULL DEFAULT 0,\n" +
                    "  PRIMARY KEY (`ID`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;",
            "CREATE TABLE IF NOT EXISTS `claims` (\n" +
                    "  `ID` bigint(255) NOT NULL AUTO_INCREMENT,\n" +
                    "  `factionid` int(255) NOT NULL DEFAULT 0,\n" +
                    "  `startX` int(255) NOT NULL DEFAULT 0,\n" +
                    "  `startZ` int(255) NOT NULL DEFAULT 0,\n" +
                    "  `endX` int(255) NOT NULL DEFAULT 0,\n" +
                    "  `endZ` int(255) NOT NULL DEFAULT 0,\n" +
                    "  `type` text NOT NULL DEFAULT 'faction' COMMENT 'Types: faction;spawn;koth;end;',\n" +
                    "  PRIMARY KEY (`ID`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;",
            "CREATE TABLE IF NOT EXISTS `ranks` (\n" +
                    "  `ID` int(11) NOT NULL AUTO_INCREMENT,\n" +
                    "  `faction` int(11) NOT NULL,\n" +
                    "  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_hungarian_ci NOT NULL,\n" +
                    "  `permissions` varchar(255) NOT NULL DEFAULT '{\"0\":true}',\n" +
                    "  `isDefault` int(2) NOT NULL DEFAULT 0,\n" +
                    "  `isLeader` int(2) NOT NULL DEFAULT 0,\n" +
                    "  PRIMARY KEY (`ID`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;"
    };

    public SQL_Generator() {
        for (String data : tables) {
            SQL_Connection.dbExecute(con, data);
        }
    }


}
