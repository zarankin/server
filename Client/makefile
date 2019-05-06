# All Targets
all: BGSclient

# Tool invocations
# Executable "BGSclient" depends on the files inside bin folder
BGSclient: bin/Client.o bin/ConnectionHandler.o bin/EncoderDecoder.o bin/NotificationPacket.o bin/ClientProtocol.o bin/echoClient.o bin/userReadTask.o bin/userWriteTask.o bin/userWriteTask.o
	@echo 'Building target: BGSclient'
	@echo 'Invoking: C++ Linker'
	g++ -o bin/BGSclient bin/Client.o bin/ConnectionHandler.o bin/EncoderDecoder.o bin/NotificationPacket.o bin/ClientProtocol.o bin/echoClient.o bin/userReadTask.o bin/userWriteTask.o  -lboost_system -lpthread
	@echo 'Finished building target: BGSclient'
	@echo ' '

# Depends on the source and header files 
bin/Client.o: src/Client.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/Client.o src/Client.cpp

# Depends on the source and header files 
bin/ConnectionHandler.o: src/ConnectionHandler.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/ConnectionHandler.o src/ConnectionHandler.cpp

# Depends on the source and header files 
bin/EncoderDecoder.o: src/EncoderDecoder.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/EncoderDecoder.o src/EncoderDecoder.cpp


# Depends on the source and header files 
bin/NotificationPacket.o: src/NotificationPacket.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/NotificationPacket.o src/NotificationPacket.cpp

# Depends on the source and header files 
bin/ClientProtocol.o: src/ClientProtocol.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/ClientProtocol.o src/ClientProtocol.cpp

# Depends on the source and header files 
bin/echoClient.o: src/echoClient.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/echoClient.o src/echoClient.cpp

# Depends on the source and header files 
bin/userReadTask.o: src/userReadTask.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/userReadTask.o src/userReadTask.cpp

# Depends on the source and header files 
bin/userWriteTask.o: src/userWriteTask.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/userWriteTask.o src/userWriteTask.cpp

#Clean the build directory
clean: 
	rm -f bin/*
