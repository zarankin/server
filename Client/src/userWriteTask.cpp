//
// Created by alona on 02/01/2019.
//

#include "../include/userWriteTask.h"
#include "../include/NotificationPacket.h"


userWriteTask::userWriteTask(ConnectionHandler &handler, ClientProtocol protocol) : handler(handler),
                                                                                    protocol(protocol) {}

void userWriteTask::operator()() {


    char *a = new char[1];
    while (handler.isConnected()) {
        handler.getBytes(a, 1);
        handler.decode(*a);
    }
    delete[] a;
}

