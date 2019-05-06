

#ifndef CLIENT_ECODERDECODER_H
#define CLIENT_ECODERDECODER_H



#include "NotificationPacket.h"
#include <vector>
#include <mutex>
#include <condition_variable>
#include <list>

using namespace std;

class EncoderDecoder {

private:

    mutex &mtx;
    condition_variable &cv;

    vector<char> bytes;
    bool foundOP;
    int byteCounter;
    short op;
    char opArr[2];


//for notication
    char isPm;
    int tmp;
    string postingUser;
    string content;

    //for error&ack
    short msgOP;
    short numOfUsers;
    list <string> userNameList;

    //for stat
    short numPosts;
    short NumFollowers;
    short NumFollowing;


public:
    bool loguot;

    EncoderDecoder(mutex &mtx,
                   condition_variable &cv);


    void init();

    void init2();

    void init3();

    void init4();

    void DecodeNextByte(char nextByte);


    string VectorToString();

    short bytesToShort(char *bytesArr);


    bool notification(char nextByte);

    bool ack(char nextByte);

    bool followUser(char nextByte);

    bool stat(char nextByte);

    short pollShort();

    void go();

};


#endif //CLIENT_ECODERDECODER_H
