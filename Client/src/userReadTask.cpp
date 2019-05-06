//
// Created by alona on 02/01/2019.
//

#include "../include/userReadTask.h"

userReadTask::userReadTask(ConnectionHandler &handler, ClientProtocol protocol, mutex &mtx,
                           condition_variable &cv) : handler(handler),
                                                     protocol(protocol), mtx(mtx), cv(cv) {}

void userReadTask::operator()() {

    string input = "";

    while (handler.isConnected()) {
        getline(cin, input);
        vector<char> *k = protocol.process(input+'\n');
        int size = (int)k->size();

        handler.sendBytes(k->data(), size);
        delete k;
        std::unique_lock<std::mutex> lck(mtx);
        cv.wait(lck);

    }
}