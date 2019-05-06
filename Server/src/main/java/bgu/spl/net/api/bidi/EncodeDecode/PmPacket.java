package bgu.spl.net.api.bidi.EncodeDecode;

public class PmPacket extends Packet {

    private String userName;
    private String content;

    public PmPacket(short opCode, String userName, String content) {
        super(opCode);

        this.userName=userName;
        this.content=content;
    }

    public String getUserName() {
        return userName;
    }

    public String getContent() {
        return content;
    }
}
