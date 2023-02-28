package me.idbi.hcf.CustomFiles.Messages;

public class MessagesComments {

    private static MessagesComments comments;

    public MessagesComments() {
        comments = this;
    }

    public static MessagesComments get() {
        return comments;
    }

    protected String[] getPermission() {
        return new String[]{"Says when the player try run a blocked command!"};
    }

    protected String[] getPrefix() {
        return new String[]{"Chat messages prefix"};
    }

    protected String[] getFactionShow() {
        return new String[]{"Faction show design", "Use placeholders!"};
    }
}
