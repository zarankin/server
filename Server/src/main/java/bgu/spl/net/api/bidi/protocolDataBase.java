package bgu.spl.net.api.bidi;

import bgu.spl.net.api.bidi.EncodeDecode.NotificationPacket;

import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class protocolDataBase {
    private ConcurrentHashMap<String, LinkedList<String>> subscribedToMe;//who follow me
    private ConcurrentHashMap<String, LinkedList<String>> follow;
    private ConcurrentHashMap<String, String> logins;//name to password
    private ConcurrentHashMap<String, ConcurrentLinkedQueue<NotificationPacket>> postsAndPm;
    private ConcurrentHashMap<String, Boolean> loggedIn;
    private ConcurrentHashMap<String, Integer> userToId;
    private ConcurrentHashMap<String, Integer> posted;
    private LinkedList<String> registerd;

    public protocolDataBase() {

        subscribedToMe = new ConcurrentHashMap<>();
        follow = new ConcurrentHashMap<>();
        logins = new ConcurrentHashMap<>();
        loggedIn = new ConcurrentHashMap<>();
        postsAndPm = new ConcurrentHashMap<>();
        userToId = new ConcurrentHashMap<>();
        posted = new ConcurrentHashMap<>();
        registerd=new LinkedList<>();

    }

    public ConcurrentHashMap<String, Boolean> getLoggedIn() {
        return loggedIn;
    }

    public ConcurrentHashMap<String, Integer> getPosted() {
        return posted;
    }

    public ConcurrentHashMap<String, LinkedList<String>> getSubscribedToMe() {
        return subscribedToMe;
    }

    public ConcurrentHashMap<String, ConcurrentLinkedQueue<NotificationPacket>> getPostsAndPm() {
        return postsAndPm;
    }

    public ConcurrentHashMap<String, Integer> getUserToId() {
        return userToId;
    }

    public ConcurrentHashMap<String, LinkedList<String>> getFollow() {
        return follow;
    }

    public LinkedList<String> getRegisterd() {
        return registerd;
    }

    public ConcurrentHashMap<String, String> getLogins() {
        return logins;
    }


}
