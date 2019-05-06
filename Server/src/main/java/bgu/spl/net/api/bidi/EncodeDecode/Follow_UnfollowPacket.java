package bgu.spl.net.api.bidi.EncodeDecode;

import java.util.LinkedList;

public class Follow_UnfollowPacket extends Packet {

    private boolean follow;
    private int numOfUsers;
    private LinkedList<String> userNameList;

    public Follow_UnfollowPacket(short opCode, boolean follow, int numOfUsers, LinkedList<String> userNameList) {
        super(opCode);
        this.follow = follow;
        this.numOfUsers = numOfUsers;
        this.userNameList = userNameList;
    }

    public boolean isFollow() {
        return follow;
    }

    public int getNumOfUsers() {
        return numOfUsers;
    }

    public LinkedList<String> getUserNameList() {
        return userNameList;
    }
}
