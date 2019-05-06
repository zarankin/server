package bgu.spl.net.api.bidi.EncodeDecode;

public class ErrorPacket extends Packet {

   private short msgOp;

    public ErrorPacket(short opCode, short msgOp) {
        super(opCode);
    this.msgOp=msgOp;
    }

    public short getMsgOp() {
        return msgOp;
    }
}
