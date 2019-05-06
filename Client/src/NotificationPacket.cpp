//
// Created by alona on 29/12/2018.
//

#include "../include/NotificationPacket.h"

NotificationPacket::NotificationPacket(short op, char isPm, string postingUser,string content) :  isPm(isPm), postingUser(postingUser), content(content), op(op){}

string NotificationPacket::getIsPm (){
    if(this->isPm=='\0'){
        return "PM";
    }
   else return "Public";
}
string NotificationPacket::getPostingUser(){
    return this->postingUser;
}
string NotificationPacket::getContent(){
    return this->content;
}
short NotificationPacket::getOP() {
    return this->op;
}