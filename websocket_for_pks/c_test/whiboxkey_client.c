//命令：gcc -Wall -o whiboxkey-websocket-test ./c_test/whiboxkey_client.c ./c_test/ws_com.c ./c_test/ws_server.c -I./c_test -lpthread
#include <stdio.h>
#include <stdlib.h> //exit()
#include <string.h>
#include <errno.h>
#include <unistd.h> //getpid

#include "ws_com.h"

//发包数据量 10K
#define SEND_PKG_MAX (1024 * 10)

//收包缓冲区大小 10K+
#define RECV_PKG_MAX (SEND_PKG_MAX + 16)

#define SERVER_IP "112.74.182.172"
#define SERVER_PORT 7888
#define SERVER_PATH "/whibox/key/websocket/dec/server-test/client-test/1"
#define SEND_MSG "{\"algorithm\": \"WBSM4\",\"mode\": \"cbc\",\"iv\": \"000102030405060708090a0b0c0d0e0f\",\"text\": \"48c0ea68f19195426e5a9bb5b2f91517666a67eda121f6cfb6b1e37f44218d40\",\"pid\": \"1\"}"

/*
 *  usage: ./client ip port path
 */
int main(int argc, char **argv)
{
    int fd, pid;
    int ret;
    Ws_DataType retPkgType;

    char recv_buff[RECV_PKG_MAX];
    char send_buff[SEND_PKG_MAX];

    int port = SERVER_PORT;
    char ip[32] = SERVER_IP;
    char path[64] = SERVER_PATH;

    //用本进程pid作为唯一标识
    pid = getpid();
    printf("client ws://%s:%d%s pid/%d\r\n", ip, port, path, pid);

    //3秒超时连接服务器
    //同时大量接入时,服务器不能及时响应,可以加大超时时间
    if ((fd = ws_connectToServer(ip, port, path, 3000)) <= 0)
    {
        printf("connect failed !!\r\n");
        return -1;
    }

    while (1)
    {
            
        //发送数据至服务器
        snprintf(send_buff, sizeof(send_buff), SEND_MSG);
        ret = ws_send(fd, send_buff, strlen(send_buff), true, WDT_TXTDATA);

        //send返回异常, 连接已断开
        if (ret <= 0)
        {
            printf("client(%d): send failed %d, disconnect now ...\r\n", pid, ret);
        }

        //接收数据
        ret = ws_recv(fd, recv_buff, sizeof(recv_buff), &retPkgType);

        //正常包
        if (ret > 0)
        {
            printf("client(%d): recv len/%d %s\r\n", pid, ret, recv_buff);
        }
        //延迟1ms
        ws_delayms(1);
    }
    
    close(fd);
    printf("client(%d): close\r\n", pid);
    return 0;
}

