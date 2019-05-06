package bgu.spl.net.api.bidi.EncodeDecode;

public class StatPacket extends Packet {

    String userName;

    public StatPacket(short opCode, String userName) {
        super(opCode);
        this.userName=userName;
    }

    public String getUserName() {
        return userName;
    }
}
