package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.bidi.EncodeDecode.MessageEncoderDecoderImp;
import bgu.spl.net.api.bidi.ProtocolImp;
import bgu.spl.net.api.bidi.protocolDataBase;
import bgu.spl.net.srv.Server;

public class TPCMain {


    public static void main(String[] args) {
        protocolDataBase dataBase = new protocolDataBase();

        Server.threadPerClient(
                Integer.parseInt(args[0]), //port
                () -> new ProtocolImp(dataBase), //protocol factory
                () -> new MessageEncoderDecoderImp() //message encoder decoder factory
        ).serve();

    }
}

