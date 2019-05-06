package bgu.spl.net.api.bidi.EncodeDecode;

import java.util.LinkedList;

public class  ACKPacket extends Packet {

    private short messageOp;
    private Object optional;
    private short optionalNum;

    public ACKPacket(short opCode, short messageOp, Object optional, short optionalNum) {
        super(opCode);
        this.messageOp=messageOp;
        this.optional=optional;
        this.optionalNum=optionalNum;
    }

    public short getMessageOp() {
        return messageOp;
    }

    public short getOptionalNum() {
        return optionalNum;
    }

    public Object getOptional() {
        return optional;
    }
}
