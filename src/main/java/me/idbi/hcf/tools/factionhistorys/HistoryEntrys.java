package me.idbi.hcf.tools.factionhistorys;

public class HistoryEntrys {

    public static class BalanceEntry {

        public final int amount;
        public final String player;
        public final long time ;
        public BalanceEntry(int amount,String player,long time){
            this.amount = amount;
            this.player = player;
            this.time = time;
        }
    }
    public static class InviteEntry {

        public final String player;
        public final boolean isInvited;
        public final String executor;
        public final long time;
        public InviteEntry(String executor,String player,long time,boolean isInvited){
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
        public final long time;
        public RankEntry(String rank,String player,long time,String type){
            this.rank = rank;
            this.player = player;
            this.time = time;
            this.type = type;
        }
    }
    public static class JoinLeftEntry {

        public final String player;
        public final boolean isJoined;
        public final long time;
        public JoinLeftEntry(String player,boolean isJoined,long time) {
            this.player = player;
            this.time = time;
            this.isJoined = isJoined;
        }
    }
    public static class FactionJoinLeftEntry {

        public final String player;
        public final String type;
        public final long time;
        public FactionJoinLeftEntry(String player,String type,long time){
            this.player = player;
            this.time = time;
            this.type = type;
        }
    }
    public static class KickEntry {

        public final String player;
        public final String executor;
        public final long time;
        public final String reason;
        public KickEntry(String player,String executor,long time,String reason){
            this.player = player;
            this.time = time;
            this.reason = reason;
            this.executor = executor;
        }
    }
}
