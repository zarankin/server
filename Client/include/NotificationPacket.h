//
// Created by alona on 29/12/2018.
//

#ifndef CLIENT_NOTIFICATIONPACKET_H
#define CLIENT_NOTIFICATIONPACKET_H

#include <string>

using namespace std;

class NotificationPacket   {
private:
    char isPm;
    string postingUser;
    string content;
    short op;


public:
    NotificationPacket(short op, char isPm, string postingUser,string content);
    string getIsPm ();
    string getPostingUser();
    string getContent();
     short getOP();

};


#endif //CLIENT_NOTIFICATIONPACKET_H
