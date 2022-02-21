# 授权服务器

## 授权码流程测试

### 1. 向授权服务器发起授权码授权：

```bash
http://127.0.0.1:9000/oauth2/authorize?client_id=felord-client&response_type=code&redirect_uri=https://www.baidu.com&scope=message.read%20message.write
```

- client_id ： 注册客户端时的数据
- response_type：固定值 code，授权码模式
- redirect_uri ：注册客户端时的数据
- scope：注册客户端时的数据

### 2. 授权服务器拦截到该请求后，会先检查发起该请求的当前用户是否认证。如果没有认证就抛出401，跳到授权服务器的登录页面，

需要自定义

默认是 `formLogin`, 可自定义

### 3. 认证成功进行了302跳转，继续执行/oauth2/authorize授权请求。

```bash
http://127.0.0.1:9000/oauth2/authorize?client_id=test-client&response_type=code&redirect_uri=https://www.baidu.com&scope=message.read%20message.write
```

默认的授权页面是一个静态页面，此处也可以自定义

### 4. 确认授权之后，会跳转到 redirect_uri 地址

```bash
// 多了一个 code 参数
https://www.baidu.com/?code=MCSJnvhXNyjilBaCyw1sCrrArWk1bzsEdxe5Z3EFbkdLwp8ASmum62n4M7Tz45VNpp_16IWboBnXlgG3LEfgN7MQqkf0-vVZufGrQpvRioRcBbesAiawMt4cspTk06ca
```

### 5. 根据code 置换 token

```http
POST /oauth2/token?grant_type=authorization_code&code=MCSJnvhXNyjilBaCyw1sCrrArWk1bzsEdxe5Z3EFbkdLwp8ASmum62n4M7Tz45VNpp_16IWboBnXlgG3LEfgN7MQqkf0-vVZufGrQpvRioRcBbesAiawMt4cspTk06ca&redirect_uri=https://127.0.0.1:8080/foo/bar HTTP/1.1
Host: localhost:9000
Authorization: Basic bWVzc2FnaW5nLWNsaWVudDpzZWNyZXQ=
```

> 这里采用的认证方式是 `client-authentication-method: client_secret_basic`方式，详见OAuth2.0协议。

### 6. 授权服务器将Token返回给客户端，完成请求，
