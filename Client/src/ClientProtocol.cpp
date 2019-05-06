//
// Created by alona on 30/12/2018.
//

#include <cstring>
#include <iostream>
#include "../include/ClientProtocol.h"


ClientProtocol::ClientProtocol() {
};

//ClientProtocol::~ClientProtocol() {delete(bytes);}


vector<char> *ClientProtocol::process(string cmd) {
    vector<char> *bytes = new vector<char>();

    string userName = "";
    string password = "";
    char numOfUsers = -1;
    int count = 1;


    short op = 0;
    int counter = 0;
    string str = "";
    char space = ' ';
    char lastChar = cmd.back();
    for (char a: cmd) {
        if (a == space || a == lastChar) {
            if (counter == 0) {
                op = getOp(str);

                counter++;
                str = "";
            } else if (op == 1) {
                if (counter == 1) {
                    bytes->push_back(0);
                    bytes->push_back(1);
                    copyIntoBytes(str, bytes);
                    bytes->push_back('0');
                    str = "";
                    counter++;
                } else if (counter == 2) {
                    copyIntoBytes(str, bytes);
                    bytes->push_back('0');
                    return bytes;
                }
            } else if (op == 2) {
                if (counter == 1) {
                    bytes->push_back(0);
                    bytes->push_back(2);
                    copyIntoBytes(str, bytes);
                    bytes->push_back('0');
                    str = "";
                    counter++;
                } else if (counter == 2) {
                    copyIntoBytes(str, bytes);
                    bytes->push_back('0');
                    return bytes;
                }

            }
            if (op == 3) {
                bytes->push_back(0);
                bytes->push_back(3);
                return bytes;

            } else if (op == 4) {
                if (counter == 1) {
                    bytes->push_back(0);
                    bytes->push_back(4);
                    str = "";
                    counter++;
                } else if (counter == 2) {
                    char follow_unfollow = str[0];
                    bytes->push_back(follow_unfollow);
                    str = "";
                    counter++;

                } else if (counter == 3) {
                    numOfUsers = stoi(str);
                    shortToBytes((short) numOfUsers, bytes);
                    str = "";
                    counter++;
                } else if (counter == 4) {
                    if (numOfUsers > count) {
                        copyIntoBytes(str, bytes);
                        bytes->push_back('0');
                        str = "";
                        count++;
                    } else if (numOfUsers == count) {
                        copyIntoBytes(str, bytes);
                        bytes->push_back('0');
                        return bytes;
                    }
                }
            } else if (op == 5) {
                if (counter == 1) {
                    bytes->push_back(0);
                    bytes->push_back(5);
                    counter++;
                }

                if (a == lastChar) {
                    copyIntoBytes(str, bytes);
                    bytes->push_back('0');
                    return bytes;
                }
                str = str + a;

            } else if (op == 6) {
                if (counter == 1) {
                    bytes->push_back(0);
                    bytes->push_back(6);
                    counter++;
                    str = "";
                } else if (counter == 2) {
                    copyIntoBytes(str, bytes);
                    bytes->push_back('0');
                    counter++;
                    str = "";

                } else if (counter == 3) {
                    if (a == lastChar) {
                        copyIntoBytes(str, bytes);
                        bytes->push_back('0');
                        return bytes;
                    } else { str = str + a; }
                }
            } else if (op == 7) {
                bytes->push_back(0);
                bytes->push_back(7);
                return bytes;
            } else if (op == 8) {
                if (counter == 1) {
                    bytes->push_back(0);
                    bytes->push_back(8);
                    counter++;
                }
                if (counter == 2) {
                    if (a == lastChar) {
                        copyIntoBytes(str, bytes);
                        bytes->push_back('0');
                        return bytes;
                    }
                }
            }


        } else { str = str + a; }
    }
    return bytes;
}


short ClientProtocol::getOp(string str) {

    if (str == "REGISTER") {
        return 1;
    }
    if (str == "LOGIN") {
        return 2;
    }
    if (str == "LOGOUT") {
        return 3;
    }
    if (str == "FOLLOW") {
        return 4;
    }
    if (str == "POST") {
        return 5;
    }
    if (str == "PM") {
        return 6;
    }
    if (str == "USERLIST") {
        return 7;
    }
    if (str == "STAT") {
        return 8;
    }
    return 0;
}

void ClientProtocol::copyIntoBytes(string str, vector<char> *bytes) {
    char *ca = new char[str.length()];
    copy(str.begin(), str.end(), ca);
    for (unsigned int i = 0; i < str.length(); ++i) {
        bytes->push_back(ca[i]);
    }
    delete[] ca;
}


void ClientProtocol::shortToBytes(short num, vector<char> *bytes) {

    bytes->push_back((num >> 8));
    bytes->push_back(num);


}

