package bgu.spl.net.api.bidi.EncodeDecode;

import bgu.spl.net.api.bidi.MessageEncoderDecoder;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;

public class MessageEncoderDecoderImp implements MessageEncoderDecoder<Packet> {


    byte[] Oparr = new byte[2];
    byte[] bytes = new byte[1 << 10];
    short op = -1;
    int len = 0;
    boolean gotOp = false;

    //for register&login&pm
    String first = "";
    String second = "";
    int lenfirst = Integer.MAX_VALUE;

    //for follow/unfollow
    byte[] follow = new byte[2];
    byte[] numOfUsers = new byte[2];
    boolean toFollow = false;
    int numOfU = 0;
    LinkedList<String> list = new LinkedList<>();
    int tmp = 0;


    @Override
    public Packet decodeNextByte(byte nextByte) {
        if (!gotOp) {
            if (len < 2) {
                Oparr[len] = nextByte;
                len++;
                if(nextByte==3||nextByte==7){
                    op = bytesToShort(Oparr);
                    gotOp = true;
                    len = 0;
                }
               else return null;
            } else if (len == 2) {
                op = bytesToShort(Oparr);
                gotOp = true;
                len = 0;
            }
        }
        if (op == 1) {
            RegisterPacket p = registerBuild(nextByte);
            if (p != null) {
                init();
                init1();
                return p;
            } else return null;

        } else if (op == 2) {
            LoginPacket p = loginBuild(nextByte);
            if (p != null) {
                init1();
                init();
                return p;
            }
        } else if (op == 3) {
            init();
            System.out.println("im op 3");
            return new LogoutPacket((short) 3);
        } else if (op == 4) {
            Follow_UnfollowPacket p = follow_unfollowBuild(nextByte);
            if (p != null) {
                init();
                init2();
                return p;
            }
        } else if (op == 5) {
            PostPacket p = postBuild(nextByte);
            if (p != null) {
                init();
                return p;
            }
        } else if (op == 6) {
            PmPacket p = pmBuild(nextByte);
            if (p != null) {
                init1();
                init();
                return p;
            }
        } else if (op == 7) {
            init();
            return new UserListPacket((short) 7);
        } else if (op == 8) {
            StatPacket p = statBuild(nextByte);
            if (p != null) {
                init();
                return p;
            }
        }
        return null;
    }

    @Override
    public byte[] encode(Packet message) {
        this.op = message.getOpCode();

        if (op == 9) {
            NotificationPacket packet = (NotificationPacket) message;
            push((short) 9);
            if (packet.isPm()) {
               pushByte((byte) 0);
            } else {
                pushByte((byte) 1);
            }

            byte[] c = packet.postingUser.getBytes(StandardCharsets.UTF_8);
            for (int i = 0; i < c.length; i++) {
                pushByte(c[i]);
            }
            pushByte((byte) 0);
            c = packet.content.getBytes(StandardCharsets.UTF_8);
            for (int i = 0; i < c.length; i++) {
                pushByte(c[i]);
            }
            pushByte((byte) 0);
            byte[] ans = Arrays.copyOfRange(this.bytes,0,len);
            init();
            return ans;
        } else if (op == 10) {
            ACKPacket packet = (ACKPacket) message;
            push((short) 10);
            byte[] ans = ACKBuild(packet);
            init();

            return ans;

        }
        else if(op==11){
            ErrorPacket packet= (ErrorPacket) message;
            push((short) 11);
            short msgOp = packet.getMsgOp();
            push(msgOp);
            byte[] ans = Arrays.copyOfRange(this.bytes,0,len);
            init();
            return ans;
        }
        return null;


    }

    private byte[] ACKBuild(ACKPacket packet) {
        short msgOp = packet.getMessageOp();
        push(msgOp);


        if (msgOp == 1 || msgOp == 2 || msgOp == 3 || msgOp == 5 || msgOp == 6) {
            return Arrays.copyOfRange(this.bytes,0,len);
        }
        if (msgOp == 4 || msgOp == 7) {
            push(packet.getOptionalNum());

            LinkedList<String> list = (LinkedList) packet.getOptional();

            for (String name : list) {
                byte[] c = name.getBytes(StandardCharsets.UTF_8);
                for (int i = 0; i < c.length; i++) {
                    pushByte(c[i]);
                }
                pushByte((byte) 0);
            }
            return Arrays.copyOfRange(this.bytes,0,len);
        }
        if (msgOp == 8) {
            short[] a = (short[]) packet.getOptional();
            push(packet.getOptionalNum());
            push(a[1]);
            push(a[0]);
            System.out.println("len is "+len);
            return Arrays.copyOfRange(this.bytes,0,len);
        }
        return null;
    }

    private void push(short a) {
        byte[] c = shortToBytes(a);
        pushByte(c[0]);
        pushByte(c[1]);
    }


    public byte[] shortToBytes(short num) {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte) ((num >> 8) & 0xFF);
        bytesArr[1] = (byte) (num & 0xFF);
        return bytesArr;
    }

    public short bytesToShort(byte[] byteArr) {
        short result = (short) ((byteArr[0] & 0xff) << 8);
        result += (short) (byteArr[1] & 0xff);
        return result;
//        short s = (short) (byteArr[0]<<8 | byteArr[0]);
//        return s;
    }


    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len++] = nextByte;
    }

    private void init() {
        op = -1;
        gotOp = false;
        this.bytes = new byte[1 << 10];
        len = 0;
    }

    private void init1() {
        first = "";
        second = "";
        lenfirst = Integer.MAX_VALUE;
    }

    private void init2() {
        follow = new byte[2];
        numOfUsers = new byte[2];
        toFollow = false;
        numOfU = 0;
        list = new LinkedList<>();
        tmp = 0;
    }

    private RegisterPacket registerBuild(byte nextByte) {

        if (firstSecond(nextByte)) {
            return new RegisterPacket((short) 1, this.first, this.second);
        } else return null;
    }

    private LoginPacket loginBuild(byte nextByte) {

        if (firstSecond(nextByte)) {
            return new LoginPacket((short) 2, this.first, this.second);
        } else return null;
    }

    private Follow_UnfollowPacket follow_unfollowBuild(byte nextByte) {

        if (len == 0) {
            if(nextByte=='0'){
                toFollow = true;
            }
            else{toFollow=false;}
           len++;
            return null;
        }
        if (1 <= len && len < 3) {
            numOfUsers[len - 1] = nextByte;
            len++;
            return null;
        } else if (len == 3) {
            numOfU = bytesToShort(numOfUsers);
            tmp = len;
        }
        if (nextByte == '0') {
            String name = new String(bytes, tmp, len - tmp, StandardCharsets.UTF_8);
            list.add(name);
            tmp = len;
            if (numOfU == list.size()) {

                return new Follow_UnfollowPacket((short) 4, toFollow, numOfU, list);
            } else return null;
        } else {
            pushByte(nextByte);
            return null;
        }
    }

    private PostPacket postBuild(byte nextByte) {

        if (nextByte == '0') {
            String post = new String(bytes, 0, len, StandardCharsets.UTF_8);
            return new PostPacket((short) 5, post);
        }
        pushByte(nextByte);
        return null;
    }

    private PmPacket pmBuild(byte nextByte) {

        if (firstSecond(nextByte)) {
            return new PmPacket((short) 6, this.first, this.second);
        } else return null;
    }

    private boolean firstSecond(byte nextByte) {
        if (nextByte == '0') {
            if (lenfirst > len) {
                lenfirst = len;
                this.first = new String(bytes, 0, lenfirst, StandardCharsets.UTF_8);
            } else {
                this.second = new String(bytes, lenfirst , len - lenfirst, StandardCharsets.UTF_8);
                return true;
            }
        } else pushByte(nextByte);
        return false;
    }

    private StatPacket statBuild(byte nextByte) {
        if (nextByte == '0') {
            String user = new String(bytes, 0, len, StandardCharsets.UTF_8);
            return new StatPacket((short) 8, user);
        }
        pushByte(nextByte);
        return null;
    }
}


