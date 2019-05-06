//
// Created by alona on 02/01/2019.
//

#ifndef CLIENT_USERWRITETASK_H
#define CLIENT_USERWRITETASK_H


#include "ClientProtocol.h"
#include "ConnectionHandler.h"

class userWriteTask {
private:
ConnectionHandler &handler;
ClientProtocol protocol;

public:
    userWriteTask(ConnectionHandler &handler, ClientProtocol protocol);

    void operator()();

};


#endif //CLIENT_USERWRITETASK_H
