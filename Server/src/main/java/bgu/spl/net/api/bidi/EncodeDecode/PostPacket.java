package bgu.spl.net.api.bidi.EncodeDecode;

public class PostPacket extends Packet {

    private String post;

    public PostPacket(short opCode, String post) {
        super(opCode);

        this.post=post;
    }

    public String getPost() {
        return post;
    }
}
