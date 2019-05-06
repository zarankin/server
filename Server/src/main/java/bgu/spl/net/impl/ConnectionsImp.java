package bgu.spl.net.impl;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.bidi.ConnectionHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionsImp implements Connections {

    ConcurrentHashMap <Integer, ConnectionHandler> connections=new ConcurrentHashMap<>();


    public ConnectionsImp(){}
    @Override
    public synchronized boolean send(int connectionId, Object msg) {

        if( connections.get(connectionId)!=null) {
            connections.get(connectionId).send(msg);
            return true;
        }
        return false;
    }

    @Override
    public void broadcast(Object msg) {
        for (Integer i: connections.keySet()) {
            connections.get(i).send(msg);
        }
    }

    public void addClient(int connectionId, ConnectionHandler connectionHandler){
        this.connections.put(connectionId, connectionHandler);

    }

    @Override
    public void disconnect(int connectionId) {

        connections.remove(connectionId);
    }
}
