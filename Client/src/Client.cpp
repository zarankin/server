//
// Created by zarankin@wincs.cs.bgu.ac.il on 1/2/19.
//

#include <stdlib.h>
#include <iostream>
#include <ClientProtocol.h>
#include <userReadTask.h>
#include <userWriteTask.h>
#include "ConnectionHandler.h"
#include <thread>
#include <mutex>
#include <condition_variable>

/**
* This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
*/
int main(int argc, char *argv[]) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);


    mutex mtx;
    condition_variable cv;
    EncoderDecoder encoderDecoder=EncoderDecoder(mtx,cv);
    ConnectionHandler connectionHandler(host, port, encoderDecoder);
    ClientProtocol clientProtocol = ClientProtocol();

    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }

    userReadTask userReadtask = userReadTask(connectionHandler, clientProtocol, mtx, cv);
    userWriteTask userWritetask = userWriteTask(connectionHandler, clientProtocol);
    thread t1(ref(userReadtask));
    thread t2(ref(userWritetask));

    t1.join();
    t2.join();


    return 0;
}
