package bgu.spl.net.api.bidi.EncodeDecode;

public class NotificationPacket extends Packet {

    boolean isPm;
    String postingUser;
    String content;

    public NotificationPacket(short opCode,  boolean isPm,String postingUser,String content) {
        super(opCode);
        this.isPm=isPm;
        this.postingUser=postingUser;
        this.content=content;
    }

    public boolean isPm() {
        return isPm;
    }

    public String getPostingUser() {
        return postingUser;
    }

    public String getContent() {
        return content;
    }
}
