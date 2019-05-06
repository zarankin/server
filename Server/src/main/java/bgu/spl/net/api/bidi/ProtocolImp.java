package bgu.spl.net.api.bidi;

import bgu.spl.net.api.bidi.EncodeDecode.*;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ProtocolImp implements BidiMessagingProtocol<Packet> {

    private Connections connections;
    private int connectionId;
    private String username;
    private String password;
    private protocolDataBase dataBase;
    private boolean shouldTerminate=false;

    public ProtocolImp(protocolDataBase dataBase) {
        this.username = "";
        this.password = "";
        this.dataBase = dataBase;
    }

    @Override
    public void start(int connectionId, Connections connections) {
        this.connectionId = connectionId;
        this.connections = connections;
        this.username = "";
        this.password = "";
        this.dataBase = dataBase;
        this.shouldTerminate=false;

    }


    @Override
    public void process(Packet message) {
        short op = message.getOpCode();
        if (op == 1) {
            RegisterPacket r = (RegisterPacket) message;
            if (register(r)) {
                ACK((short) 1, null, -1);
            } else {
                error((short) 1);
            }
        } else if (op == 2) {
            LoginPacket l = (LoginPacket) message;
            if (login(l)) {
                ACK((short) 2, null, -1);
            } else {
                error((short) 2);
            }
        } else if (op == 3) {
            if (logout()) {
                ACK((short) 3, null, -1);
            } else {
                error((short) 3);
            }
        } else if (op == 4) {
            Follow_UnfollowPacket f = (Follow_UnfollowPacket) message;
            if (!follow_unfollow(f)) {
                error((short) 4);
            }
        } else if (op == 5) {
            PostPacket p = (PostPacket) message;
            if (post(p)) {
                ACK((short) 5, null, -1);
            } else {
                error((short) 5);
            }
        } else if (op == 6) {
            PmPacket p = (PmPacket) message;
            if (pm(p)) {
                ACK((short) 6, null, -1);
            } else {
                error((short) 6);
            }
        } else if (op == 7) {
            if (!userList()) {
                error((short) 7);
            }
        } else if (op == 8) {
            StatPacket s = (StatPacket) message;
            if (!stat(s)) {
                error((short) 8);
            }
        }
    }

    @Override
    public boolean shouldTerminate() {

        return this.shouldTerminate;
    }

    private boolean register(RegisterPacket packet) {
        System.out.println("register");
        if (this.username == "") {
            if (dataBase.getLogins().get((packet.getUsername())) == null) {
                dataBase.getLogins().put(packet.getUsername(), packet.getPassword());
                dataBase.getLoggedIn().put(packet.getUsername(), false);
                dataBase.getFollow().put(packet.getUsername(), new LinkedList<>());
                dataBase.getSubscribedToMe().put(packet.getUsername(), new LinkedList<>());
                dataBase.getPostsAndPm().put(packet.getUsername(), new ConcurrentLinkedQueue<>());
                dataBase.getPosted().put(packet.getUsername(), 0);
                dataBase.getRegisterd().add(packet.getUsername());
                System.out.println(packet.getUsername() + " " + packet.getPassword());
                return true;
            } else return false;
        } else return false;
    }


    private boolean login(LoginPacket packet) {
        if (this.username == "" && this.password == "") {
            if (dataBase.getLogins().get(packet.getUsername()) != null) {
                synchronized (this.dataBase.getLoggedIn().get(packet.getUsername())) {
                    if (!dataBase.getLoggedIn().get(packet.getUsername())) {
                        if (dataBase.getLogins().get(packet.getUsername()).equals(packet.getPassword())) {
                            dataBase.getLoggedIn().remove(packet.getUsername());
                            dataBase.getLoggedIn().put(packet.getUsername(), true);
                            this.password = packet.getPassword();
                            this.username = packet.getUsername();
                            dataBase.getUserToId().put(this.username, this.connectionId);
                                if (!dataBase.getPostsAndPm().get(packet.getUsername()).isEmpty()) {
                                    while (!dataBase.getPostsAndPm().get(packet.getUsername()).isEmpty()) {
                                        this.connections.send(this.connectionId, dataBase.getPostsAndPm().get(packet.getUsername()).poll());
                                    }
                                }

                            System.out.println(packet.getUsername() + " " + packet.getPassword());
                            return true;
                        }
                        return false;
                    }
                    return false;
                }
            }
            return false;
        }
        return false;
    }

    private boolean logout() {
        if (this.username != "" && this.password != "") {
            System.out.println("logout");
            synchronized (this.dataBase.getLoggedIn().get(this.username)) {
                    dataBase.getLoggedIn().remove(this.username);
                    dataBase.getLoggedIn().put(this.username, false);
                    this.dataBase.getUserToId().remove(this.username);
                    this.username = "";
                    this.password = "";
                    this.shouldTerminate=true;
                    return true;
                }

            }
         else return false;
    }

    private boolean follow_unfollow(Follow_UnfollowPacket packet) {
        System.out.println("follow");
        if (this.username != "" && this.password != "") {

            LinkedList<String> successList = new LinkedList<>();
            if (packet.isFollow()) {
                for (String s : packet.getUserNameList()) {
                    if (!s.equals( this.username)) {
                        if (dataBase.getLogins().get(s) != null) {
                            if (!dataBase.getFollow().get(this.username).contains(s)) {
                                if (dataBase.getLogins().get(s) != null) {
                                    synchronized (dataBase.getFollow().get(this.username)) {
                                            dataBase.getFollow().get(this.username).add(s);
                                            dataBase.getSubscribedToMe().get(s).add(this.username);
                                            successList.add(s);

                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                for (String s : packet.getUserNameList()) {
                    if (!s.equals( this.username)) {
                        if (dataBase.getFollow().get(this.username).contains(s)) {
                            synchronized (dataBase.getFollow().get(this.username)) {
                                    dataBase.getFollow().get(this.username).remove(s);
                                    dataBase.getSubscribedToMe().get(s).remove(this.username);
                                    successList.add(s);

                            }
                        }
                    }
                }
            }
            if(successList.isEmpty()){
                return false;
            }
            ACK((short) 4, successList, successList.size());
            return true;
        }
        return false;
    }


    private boolean post(PostPacket packet) {
        if (this.username != "" && this.password != "") {
            LinkedList<String> sendTo = new LinkedList<>();
            int i = 0;
            String post = packet.getPost();
            while (i < post.length()) {
                if (post.charAt(i) == '@') {
                    String name = "";
                    i++;
                    while (i < post.length() && post.charAt(i) - 32 != 0) {
                        name = name + post.charAt(i);
                        i++;
                    }
                    if(!sendTo.contains(name)) {
                        sendTo.add(name);
                    }
                }
                i++;
            }
            Notification(false, packet.getPost(), sendTo);
            Integer k = this.dataBase.getPosted().get(this.username);
            this.dataBase.getPosted().remove(this.username);
            k = k + 1;
            this.dataBase.getPosted().put(this.username, k);

            return true;
        }
        return false;
    }


    private boolean pm(PmPacket packet) {
        if (this.username != "" && this.password != "") {
            if (dataBase.getLogins().get(packet.getUserName()) != null) {
                NotificationPacket p = new NotificationPacket((short) 9, true, this.username, packet.getContent());
                synchronized (dataBase.getLoggedIn().get(packet.getUserName())) {
                        if (dataBase.getLoggedIn().get(packet.getUserName())) {
                            this.connections.send(dataBase.getUserToId().get(packet.getUserName()), p);
                            return true;
                        } else {
                            this.dataBase.getPostsAndPm().get(packet.getUserName()).add(p);
                            return true;
                        }

                }
            }
            return false;
        }
        return true;
    }

    private boolean userList() {
        if (this.username != "" && this.password != "") {
            synchronized (dataBase.getRegisterd()) {
                ACK((short) 7, dataBase.getRegisterd(), dataBase.getRegisterd().size());
                return true;
            }
        }
        return false;
    }

    private boolean stat(StatPacket packet) {
        if (this.username != "" && this.password != "") {
            synchronized (dataBase.getLogins().get(packet.getUserName())) {
                if (dataBase.getLogins().get(packet.getUserName()) != null) {
                    short[] arr = new short[2];
                        arr[0] = (short) dataBase.getFollow().get(packet.getUserName()).size();

                        arr[1] = (short) dataBase.getSubscribedToMe().get(packet.getUserName()).size();
                        ACK((short) 8, arr, dataBase.getPosted().get(packet.getUserName()));
                        return true;


                }
            }
            return false;
        }
        return false;
    }

    private void Notification(boolean isPm, String content, LinkedList<String> sendTo) {
        NotificationPacket packet = new NotificationPacket((short) 9, isPm, this.username, content);
        synchronized (dataBase.getSubscribedToMe().get(this.username)) {

            for (String name : sendTo) {
                if (!name.equals( this.username)) {
                    if (!dataBase.getSubscribedToMe().get(this.username).contains(name)) {
                        if (dataBase.getLogins().get(name) != null) {
                            if (!dataBase.getLoggedIn().get(name)) {
                                    if (this.dataBase.getPostsAndPm().get(name) != null) {
                                        this.dataBase.getPostsAndPm().get(name).add(packet);
                                    } else {
                                        System.out.println("for some reason your queue is null :(");
                                    }

                            } else {
                                this.connections.send(dataBase.getUserToId().get(name), packet);
                            }
                        }
                    }
                }
            }
            for (String name : dataBase.getSubscribedToMe().get(this.username)) {
                if (name != this.username) {
                    if (dataBase.getLoggedIn().get(name)) {
                        this.connections.send(dataBase.getUserToId().get(name), packet);
                    } else {
                            if (this.dataBase.getPostsAndPm().get(name) != null) {
                                this.dataBase.getPostsAndPm().get(name).add(packet);
                            } else {
                                System.out.println("for some reason your queue is null :(");
                            }

                    }
                }
            }
        }
    }


    private void ACK(short messageOp, Object optional, int optionalNum) {

        ACKPacket ack = new ACKPacket((short) 10, messageOp, optional, (short) optionalNum);
        connections.send(this.connectionId, ack);
    }

    private void error(short msgOp) {
        connections.send(this.connectionId, new ErrorPacket((short) 11, msgOp));
    }

}
