
#CC=arm-linux-gnueabihf-
CC=

target:
	$(CC)gcc -Wall -o whiboxkey-websocket-test ./c_test/whiboxkey_client.c ./c_test/ws_com.c ./c_test/ws_server.c -I./c_test -lpthread

clean:
	@rm -rf whiboxkey-websocket-test
