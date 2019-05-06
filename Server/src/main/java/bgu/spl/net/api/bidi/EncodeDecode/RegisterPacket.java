package bgu.spl.net.api.bidi.EncodeDecode;

public class RegisterPacket extends Packet {

    private String username;
    private String password;

    public RegisterPacket(short opCode, String username, String password) {
        super(opCode);
        this.username=username;
        this.password=password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
