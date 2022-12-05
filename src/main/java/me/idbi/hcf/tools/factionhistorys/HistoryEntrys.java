package me.idbi.hcf.tools.factionhistorys;

public class HistoryEntrys {

    public static class BalanceEntry {

        public final int amount;
        public final String player;
        public final String time ;
        public BalanceEntry(int amount,String player,String time){
            this.amount = amount;
            this.player = player;
            this.time = time;
        }
    }
    public static class InviteEntry {

        public final String player;
        public final boolean isInvited;
        public final String executor;
        public final String time;
        public InviteEntry(String executor,String player,String time,boolean isInvited){
            this.executor = executor;
            this.player = player;
            this.time = time;
            this.isInvited = isInvited;
        }
    }
    public static class RankEntry {

        public final String player;
        public final String type;
        public final String rank;
        public final String time;
        public RankEntry(String rank,String player,String time,String type){
            this.rank = rank;
            this.player = player;
            this.time = time;
            this.type = type;
        }
    }
    public static class JoinLeftEntry {

        public final String player;
        public final boolean isJoined;
        public final String time;
        public JoinLeftEntry(String player,boolean isJoined,String time) {
            this.player = player;
            this.time = time;
            this.isJoined = isJoined;
        }
    }
    public static class FactionJoinLeftEntry {

        public final String player;
        public final String type;
        public final String time;
        public FactionJoinLeftEntry(String player,String type,String time){
            this.player = player;
            this.time = time;
            this.type = type;
        }
    }
    public static class KickEntry {

        public final String player;
        public final String executor;
        public final String time;
        public final String reason;
        public KickEntry(String player,String executor,String time,String reason){
            this.player = player;
            this.time = time;
            this.reason = reason;
            this.executor = executor;
        }
    }
}
