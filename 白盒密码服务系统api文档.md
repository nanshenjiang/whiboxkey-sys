# 白盒密码服务系统api文档

## 说明

白盒密码服务系统根据VPN/轻量级网关需求，分为了VPN/轻量级网关服务端和客户端。其中服务端获取的是黑盒密钥，客户端获取的是白盒加密密钥表（以文件流形式传输）。另外，服务端和客户端现将运行于树莓派上，将树莓派的Mac地址作为唯一身份序列，服务端和客户端需向密服系统提交自身的身份序列以获取密钥。



## 基本URL

服务器IP：112.74.182.172

服务PORT：8080

协议：HTTP

响应数据格式（Restful规范）：

```json
{
    "code": 200,     	#状态码，200为成功，400开头为出错
    "data": [],			#获取的数据将封装在data中
    "msg": "success"	#状态信息
}
```



## 服务端api

由于单个服务端与多个客户端相连接，所以服务端获取黑盒密钥不仅需要自身身份序列，也需提交相应客户端的身份序列。

------

### 获取服务端与所有客户端对应密钥

**请求类型：GET**

**拼接URL：/whibox/key/server/{server_serial}**

**例子**

URL：http://112.74.182.172:8080/whibox/key/server/test

其中：test为server的序列号

```json
{
    "code": 200,
    "data": [
        {
            "ClientSerial": "test",
            "key": "dMbu9hEV3SY1FKIe"
        },
        {
            "ClientSerial": "test2",
            "key": "7D3x3d8j6F5MmWn1"
        }
    ],
    "msg": "success"
}
```

其中data中包含了所有与该服务端对应的客户端及通信密钥，ClientSerial为客户端的身份序列，key为通信黑盒密钥。	

------

### 获取服务端与某个客户端对应密钥

**请求类型：GET**

**拼接URL：/whibox/key/server/{server_serial}/{client_serial}**

提交服务端身份序列和相应客户端身份序列获取目的密钥

**例子**

URL：http://112.74.182.172:8080/whibox/key/server/test/test

其中：第一个test为server的身份序列，第二个test为client的身份序列

```json
{
    "code": 200,
    "data": {
        "key": "dMbu9hEV3SY1FKIe"
    },
    "msg": "success"
}
```

其中key跟着的就是服务端（序列为test）和客户端（序列为test）之间通信的黑盒密钥。





## 客户端api

### 前置步骤（必须）

获取客户端的白盒密钥表前置步骤。**注意**：由于白盒密钥表是以文件进行存储，当服务端和客户端通信密钥未过期之前，客户端可对白盒密钥表进行本地存储操作。客户端在通信前，先访问该接口，了解是否需要更新白盒密钥表操作。假设密钥表无需更新，则直接使用本地存储密钥进行加解密操作。假设密钥表需要更新，则通过下一个接口获取白盒密钥表文件流进行下载覆盖本地密钥表操作。

**请求类型：GET**

**拼接URL：/whibox/key/client/{client_serial}**

**例子**

URL：http://112.74.182.172:8080/whibox/key/client/test

其中：test为客户端的身份序列

```json
{
    "code": 200,
    "data": {
        "pass": "DQhuO1622886062441",
        "update": false
    },
    "msg": "success"
}
```

其中：pass为通行证，假设客户端本地密钥表过期或损坏，则客户端需要使用该通行证利用下一个接口进行下载最新白盒密钥表操作。update为是否需要更新，false为无需更新，true为需要更新，可能密钥过期或其他原因失效，客户端需使用通行证下载密钥表。

------

### 下载密钥表

其中白盒密钥表是以文件流形式进行传输，本地需进行文件流操作存储。

**请求类型：GET**

**拼接URL：/whibox/key/download/{pass}**

**例子**

URL：http://112.74.182.172:8080/whibox/key/download/DQhuO1622886062441

其中：DQhuO1622886062441为上一个例子获取的通行证pass

将获取文件流，请自行存储。