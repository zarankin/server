package bgu.spl.net.api.bidi.EncodeDecode;

public class LoginPacket extends Packet {

    private String username;
    private String password;

    public LoginPacket(short opCode, String username, String password) {
        super(opCode);

        this.username=username;
        this.password=password;;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
