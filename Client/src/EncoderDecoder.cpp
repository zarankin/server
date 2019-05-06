

#include <sstream>
#include <cstring>
#include <iostream>
#include "../include/EncoderDecoder.h"

EncoderDecoder::EncoderDecoder(mutex &mtx,
                               condition_variable &cv) : mtx(mtx), cv(cv), bytes(vector<char>()), foundOP(false),
                                                         byteCounter(0), op(0), isPm(-1), tmp(0),
                                                         postingUser(""), content(""), msgOP(0), numOfUsers(0),
                                                         userNameList(list<string>()), numPosts(0), NumFollowers(0),
                                                         NumFollowing(0), loguot(
                false) {

}


void EncoderDecoder::init() {
    bytes.clear();
    foundOP = false;
    byteCounter = 0;
    op = 0;
    msgOP = 0;
}

void EncoderDecoder::init2() {
    isPm = -1;
    tmp = 0;
    postingUser = "";
    content = "";
}

void EncoderDecoder::init3() {
    msgOP = 0;
    numOfUsers = 0;
    userNameList = list<string>();
}

void EncoderDecoder::init4() {
    numPosts = 0;
    NumFollowers = 0;
    NumFollowing = 0;
}

void EncoderDecoder::DecodeNextByte(char nextByte) {
    if (!foundOP) {
        if (byteCounter < 2) {
            opArr[byteCounter] = nextByte;
            this->byteCounter = this->byteCounter + 1;
        } else if (byteCounter == 2) {
            this->op = pollShort();
            this->foundOP = true;
            this->bytes.clear();
        }
    }

    if (this->op == 9) {
        if (notification(nextByte)) {
            init();
            init2();
            return;
        }
    } else if (this->op == 10) {
        if (ack(nextByte)) {
            init();
            init3();
            init4();
            return;
        }

    } else if (this->op == 11) {
        if (byteCounter < 4) {
            opArr[byteCounter - 2] = nextByte;
            byteCounter = byteCounter + 1;
        }
        if (byteCounter == 4) {
            this->msgOP = pollShort();
            cout << "ERROR " << this->msgOP << endl;
            init();
            init3();
            go();
            return;
        }
    }

}


string EncoderDecoder::VectorToString() {
    stringstream joinedValues;
    for (char value: bytes) {
        joinedValues << value;
    }

    std::string result = joinedValues.str();
    return result;

}


short EncoderDecoder::bytesToShort(char *bytesArr) {
    short result = (short) ((bytesArr[0] & 0xff) << 8);
    result += (short) (bytesArr[1] & 0xff);
    return result;
}


bool EncoderDecoder::notification(char nextByte) {
    if (byteCounter == 2) {
        this->isPm = nextByte;
        byteCounter = byteCounter + 1;
        return false;
    } else if (nextByte == '\0') {
        if (tmp == 0) {
            postingUser = VectorToString();
            bytes.clear();
            tmp = byteCounter;
            byteCounter = byteCounter + 1;
            return false;
        } else {
            content = VectorToString();
            bytes.clear();
            NotificationPacket notificationPacket(op, isPm, postingUser, content);
            cout << "NOTIFICATION " << notificationPacket.getIsPm() << " " << notificationPacket.getPostingUser() << " "
                 << notificationPacket.getContent() << endl;
            go();
            return true;
        }

    } else {
        this->bytes.push_back(nextByte);
        return false;
    }
}

bool EncoderDecoder::ack(char nextByte) {
    if (byteCounter < 4) {
        opArr[byteCounter - 2] = nextByte;
        byteCounter = byteCounter + 1;
        if (byteCounter == 2) {
            return false;
        } else if (byteCounter == 4) {
            this->msgOP = pollShort();
            bytes.clear();
        }
    }

    if (msgOP == 1 || msgOP == 2 || msgOP == 5 || msgOP == 6) {
        cout << "ACK " << this->msgOP << endl;
        go();
        return true;
    } else if (msgOP == 3) {
        cout << "ACK " << this->msgOP << endl;
        this->loguot = true;
        go();
        return true;
    } else if (msgOP == 4 || msgOP == 7) {
        return followUser(nextByte);

    } else if (msgOP == 8) {
        return stat(nextByte);
    }
    return false;
}

bool EncoderDecoder::followUser(char nextByte) {
    if (5 <= byteCounter && byteCounter < 7) {
        if (byteCounter == 5) {
            bytes.clear();
        }
        bytes.push_back(nextByte);
        byteCounter = byteCounter + 1;
        return false;
    }
    if (byteCounter == 7) {
        int i = 0;
        for (char s:bytes) {
            opArr[i] = s;
            i++;
        }
        this->numOfUsers = pollShort();
        bytes.clear();
    }
    if (nextByte == '\0') {
        string name = VectorToString();
        userNameList.push_back(name);
        bytes.clear();
        byteCounter = byteCounter + 1;
        if (userNameList.size()== (unsigned)numOfUsers) {
            cout << "ACK " << this->msgOP << " " << this->numOfUsers << " ";
            for (string name: this->userNameList) {
                cout << name << " ";
            }
            cout << endl;
            go();
            return true;
        } else return false;
    } else {
        byteCounter = byteCounter + 1;
        this->bytes.push_back(nextByte);
        return false;
    }
}


bool EncoderDecoder::stat(char nextByte) {
    if (byteCounter < 7) {
        if (byteCounter == 5) {
            bytes.clear();
        }
        bytes.push_back(nextByte);
        byteCounter = byteCounter + 1;
        return false;
    }
    if (byteCounter == 7) {
        int i = 0;
        for (char s:bytes) {
            opArr[i] = s;
            i++;
        }
        this->numPosts = pollShort();
        bytes.clear();
    }
    if (byteCounter < 9) {
        bytes.push_back(nextByte);
        byteCounter = byteCounter + 1;
        return false;
    } else if (byteCounter == 9) {
        int i = 0;
        for (char s:bytes) {
            opArr[i] = s;
            i++;
        }
        this->NumFollowers = pollShort();
        bytes.clear();
        bytes.push_back(nextByte);
        byteCounter = byteCounter + 1;
        return false;
    }
    if (byteCounter == 10) {
        bytes.push_back(nextByte);
        int i = 0;
        for (char s:bytes) {
            opArr[i] = s;
            i++;
        }
        this->NumFollowing = pollShort();
        cout << "ACK " << this->msgOP << " " << this->numPosts << " " << this->NumFollowers << " " << this->NumFollowing
             << endl;
        go();
        return true;
    }
    return false;
}

short EncoderDecoder::pollShort() {

    return bytesToShort(opArr);
}

void EncoderDecoder::go() {
    std::unique_lock<std::mutex> lck(mtx);
    cv.notify_all();
}