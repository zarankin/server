
cmake_minimum_required(VERSION 3.13)
project(Client)

set(CMAKE_CXX_STANDARD 14)


find_package(Boost COMPONENTS system filesystem thread REQUIRED)
IF (Boost_FOUND)
    include_directories(${Boost_INCLUDE_DIR})
endif()


SET(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -pthread -lboost_thread")


include_directories(.)
include_directories(include)
include_directories(src)

add_executable(Client
        src/EncoderDecoder.cpp
        include/EncoderDecoder.h
        src/ConnectionHandler.cpp
        include/ConnectionHandler.h
        src/NotificationPacket.cpp
        include/NotificationPacket.h
        src/ClientProtocol.cpp
        include/ClientProtocol.h
        src/echoClient.cpp
        src/userReadTask.cpp
        include/userReadTask.h
        src/userWriteTask.cpp
        include/userWriteTask.h
        src/Client.cpp)


target_link_libraries( Client LINK_PUBLIC ${Boost_LIBRARIES} )
