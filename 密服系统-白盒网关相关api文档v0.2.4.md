# 白盒密服系统-白盒网关相关api文档

## 说明

白盒密码服务系统提供云加解密功能和下载白盒密钥表功能。

其中服务方为白盒VPN/轻量级网关（以下统称网关），网关通信两端有两类使用方，一类是调用云加解密接口方（以下简称A方），一类是调用获取白盒密钥表接口方（以下简称B方）。一个A方可能对应多个B方。所有网关都有唯一身份序列，通过上传该身份序列验证身份进行相应操作（目前暂定mac地址为身份序列）。

另外，网关通信双方使用上下行密钥进行加密通信。即，存在以下两种类型通信：

1. B方从密服系统获取上行白盒加密表，对通信信息进行加密，传输至A方，A方将加密信息上传值密服系统进行解密。
2. A方将通信信息上传值密服系统进行加密，传输至B方，B方从密服系统获取下行白盒解密表，对通信信息进行解密。

白盒密钥表是以文件进行存储，B方下载白盒密钥表的调用顺序为：（1）获取白盒密钥表通行证api（2）下载白盒密钥表api。B方获取到通行证，再使用该通行证下载到对应的白盒密钥表。由于密钥表存在过期或失效的问题，B方需通过获取通行证api了解本地密钥是否出现问题需要更新，当获取通行证api返回的密钥版本号比本地密钥新时，用户需要通过下载密钥表api更新本地密钥表。



## 基本URL

服务器IP：112.74.182.172

服务Port：7888

协议：HTTP

响应数据格式（Restful规范）：

```json
{
    "code": 200,     	#状态码，200为成功，400开头为出错
    "data": [],			#获取的数据将封装在data中
    "msg": "success"	#状态信息
}
```





## 密服加密消息api

**请求类型：POST**

**Content-Type：application/json**

**拼接URL：/whibox/key/server/enc/{server_serial}/{client_serial}**

其中：

| 参数          | 含义        | 样例        |
| ------------- | ----------- | ----------- |
| server_serial | A方身份序列 | 暂定mac地址 |
| client_serial | B方身份序列 | 暂定mac地址 |

另外，提交中需包含消息主体（以json为结构），具有以下参数：

| 参数      | 含义                                                   | 样例                                       |
| --------- | ------------------------------------------------------ | ------------------------------------------ |
| algorithm | （非空）加解密的算法，目前仅支持WBSM4                  | "algorithm": "WBSM4"                       |
| mode      | （非空）加密模式，目前支持cbc或gcm                     | "mode": "cbc"或"mode": "gcm"               |
| text      | （非空）cbc或gcm模式中的进行加密的数据，需转换为16进制 | "text": "0123456789abcdeffedcba9876543210" |

返回参数：

| 参数   | 含义         |
| ------ | ------------ |
| answer | 加密后结果 |
| iv | 加密使用的iv值（16进制） |
| version | 加密使用的密钥版本 |
| tag    | （仅gcm模式存在）经gcm模式加密后获取的消息认证码 |

**例子**

URL：http://112.74.182.172:7888/whibox/key/server/enc/server-test/client-test

其中：server-test为server的身份序列，client-test为client的身份序列

**CBC模式**——提交的消息主体：

```json
{
    "algorithm": "WBSM4",
    "mode": "cbc",
    "text": "000102030405060708090a0b0c0d0e0f"
}
```

**CBC模式**——接收回的消息：

```json
{
    "code": 200,
    "data": {
        "answer": "63336c500334d6c8f30d6549d22ec654",
        "iv": "08d3da6617e4a6960d00a331e891ee3e",
        "version": 1
    },
    "msg": "success"
}
```

**GCM模式**——提交的消息主体：

```json
{
    "algorithm": "WBSM4",
    "mode": "cbc",
    "text": "000102030405060708090a0b0c0d0e0f"
}
```

**GCM模式**——接收回的消息：

```json
{
    "code": 200,
    "data": {
        "answer": "a4c461943f90b17ca37194b1c1a1a28c",
        "tag": "2e6412387775a64f78da87e55974d8fb",
        "iv": "e3c60ceb8ab9ccde3070983d4a1080a1",
        "version": 1
    },
    "msg": "success"
}
```





## 密服解密消息api

**请求类型：POST**

**Content-Type：application/json**

**拼接URL：/whibox/key/server/dec/{server_serial}/{client_serial}/{version}**

其中：

| 参数          | 含义        | 样例             |
| ------------- | ----------- | ---------------- |
| server_serial | A方身份序列 | 暂定mac地址      |
| client_serial | B方身份序列 | 暂定mac地址      |
| version       | 密钥版本    | 1,2,3...等版本号 |

另外，提交中需包含消息主体（以json为结构），具有以下参数：

| 参数      | 含义                                                   | 样例                                       |
| --------- | ------------------------------------------------------ | ------------------------------------------ |
| algorithm | （非空）加解密的算法，目前仅支持WBSM4                  | "algorithm": "WBSM4"                       |
| mode      | （非空）解密模式，目前支持cbc或gcm                     | "mode": "cbc"或"mode": "gcm"               |
| text      | （非空）cbc或gcm模式中的进行解密的数据，需转换为16进制 | "text": "0123456789abcdeffedcba9876543210" |
| iv        | （非空）cbc或gcm模式中需要的iv值，需转换为16进制       | "iv": "0123456789abcdeffedcba9876543210"   |

返回参数：

| 参数    | 含义                                             |
| ------- | ------------------------------------------------ |
| answer  | 解密后结果                                       |
| version | 解密使用的密钥版本                               |
| tag     | （仅gcm模式存在）经gcm模式解密后获取的消息认证码 |

**例子**

URL：http://112.74.182.172:7888/whibox/key/server/dec/server-test/client-test/1

其中：server-test为server的身份序列，client-test为client的身份序列，1位密钥版本号

**CBC模式**——提交的消息主体：

```json
{
    "algorithm": "WBSM4",
    "mode": "cbc",
    "iv": "000102030405060708090a0b0c0d0e0f",
    "text": "c2cbacc37d319b5e70a0104d2164c72f"
}
```

**CBC模式**——接收回的消息：

```json
{
    "code": 200,
    "data": {
        "answer": "000102030405060708090a0b0c0d0e0f",
        "version": 1
    },
    "msg": "success"
}
```

**GCM模式**——提交的消息主体：

```json
{
    "algorithm": "WBSM4",
    "mode": "gcm",
    "iv": "000102030405060708090a0b0c0d0e0f",
    "text": "49e08a202f0743334bde84a256027b3e"
}
```

**GCM模式**——接收回的消息：

```json
{
    "code": 200,
    "data": {
        "answer": "000102030405060708090a0b0c0d0e0f",
        "tag": "d113073547ebe8eff9466443744e2368",
        "version": 1
    },
    "msg": "success"
}
```





## 获取上行白盒加密表通行证api

**请求类型：GET**

**拼接URL：/whibox/key/client/up/{client_serial}**

其中：

| 参数          | 含义           | 样例        |
| ------------- | -------------- | ----------- |
| client_serial | 客户端身份序列 | 暂定mac地址 |

返回消息：

| 参数    | 含义                      |
| ------- | ------------------------- |
| pass    | 通行证，用于下载密钥表api |
| version | 密钥版本号                |

**例子**

URL：http://112.74.182.172:7888/whibox/key/client/up/client-test

其中：client-test为客户端的身份序列

接收回的消息：

```json
{
    "code": 200,
    "data": {
        "pass": "morxj1630246946726",
        "version": 1
    },
    "msg": "success"
}
```





## 获取下行白盒解密表通行证api

**请求类型：GET**

**拼接URL：/whibox/key/client/down/{client_serial}/{version}**

其中：

| 参数          | 含义               | 样例             |
| ------------- | ------------------ | ---------------- |
| client_serial | 客户端身份序列     | 暂定mac地址      |
| version       | 需要获取的密钥版本 | 1,2,3...等版本号 |

返回消息：

| 参数    | 含义                      |
| ------- | ------------------------- |
| pass    | 通行证，用于下载密钥表api |
| version | 密钥版本号                |

**例子**

URL：http://112.74.182.172:7888/whibox/key/client/down/client-test/1

其中：client-test为客户端的身份序列，1为需要获取的密钥版本号

接收回的消息：

```json
{
    "code": 200,
    "data": {
        "pass": "m6lf41630246975076",
        "version": 1
    },
    "msg": "success"
}
```





## 下载密钥表api

其中白盒密钥表是以文件流形式进行传输，本地需对文件流进行存储操作。

**请求类型：GET**

**拼接URL：/whibox/key/download/{pass}**

其中：

| 参数 | 含义                             | 样例 |
| ---- | -------------------------------- | ---- |
| pass | 通行证，用于下载密钥表的接口验证 |      |

返回密钥表文件流，请自行存储

**例子**

以上面获取的通行证为例：

URL：上行白盒加密表：http://112.74.182.172:7888/whibox/key/download/morxj1630246946726

​		   下行白盒解密表：http://112.74.182.172:7888/whibox/key/download/m6lf41630246975076

