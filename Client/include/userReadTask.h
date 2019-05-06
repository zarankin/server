//
// Created by alona on 02/01/2019.
//

#ifndef CLIENT_USERREADTASK_H
#define CLIENT_USERREADTASK_H

#include <mutex>
#include <condition_variable>
#include "ConnectionHandler.h"
#include "ClientProtocol.h"

class userReadTask {
private:
    ConnectionHandler& handler;
    ClientProtocol protocol;
    mutex & mtx;
    condition_variable & cv;

public:

    userReadTask(ConnectionHandler& handler, ClientProtocol protocol, mutex &mtx,
    condition_variable & cv);
    void operator ()();

};


#endif //CLIENT_USERREADTASK_H
