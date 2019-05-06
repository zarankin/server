package bgu.spl.net.api.bidi.EncodeDecode;

public abstract class Packet {
    protected short opCode;

    public Packet(short opCode)
    {this.opCode=opCode;}

    public short getOpCode() {
        return opCode;
    }
}