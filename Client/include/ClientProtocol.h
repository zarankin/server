//
// Created by alona on 30/12/2018.
//

#ifndef CLIENT_CLIENTPROTOCOL_H
#define CLIENT_CLIENTPROTOCOL_H

#include <string>
#include <vector>

using namespace std;

class ClientProtocol {

private:;

    short getOp(string str);

public:

    ClientProtocol();

    //  ~ClientProtocol();

    vector<char> *process(string cmd);

    void copyIntoBytes(string str, vector<char> *bytes);


    void shortToBytes(short num, vector<char> *bytes);


};


#endif //CLIENT_CLIENTPROTOCOL_H
