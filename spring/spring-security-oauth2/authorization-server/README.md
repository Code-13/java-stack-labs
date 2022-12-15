# 授权服务器

## 授权码流程测试

### 1. 向授权服务器发起授权码授权：

```bash
GET
http://127.0.0.1:9000/oauth2/authorize?client_id=test-client&response_type=code&redirect_uri=https://www.baidu.com&scope=message.read%20message.write
```

- client_id ： 注册客户端时的数据
- response_type：固定值 code，授权码模式
- redirect_uri ：注册客户端时的数据
- scope：注册客户端时的数据

### 2. 授权服务器拦截到该请求后，会先检查发起该请求的当前用户是否认证。如果没有认证就抛出401，跳到授权服务器的登录页面，

需要自定义

默认是 `formLogin`, 可自定义

```bash
http://127.0.0.1:9000/login
```

### 3. 认证成功进行了302跳转，继续执行/oauth2/authorize授权请求。

```bash
http://127.0.0.1:9000/oauth2/authorize?client_id=test-client&response_type=code&redirect_uri=https://www.baidu.com&scope=message.read%20message.write
```

默认的授权页面是一个静态页面，此处也可以自定义

### 4. 点击确认授权

```bash
POST Content-Type: application/x-www-form-urlencoded
http://127.0.0.1:9000/oauth2/authorize
client_id=test-client&state=KtbHiTS_6-FT6J-YpBvBU4_ArnHN7NAsGfKQU6rWerE%3D&scope=message.read&scope=message.write
```

### 5. 确认授权之后，会跳转到 redirect_uri 地址

```bash
// 多了一个 code 参数
https://www.baidu.com/?code=MCSJnvhXNyjilBaCyw1sCrrArWk1bzsEdxe5Z3EFbkdLwp8ASmum62n4M7Tz45VNpp_16IWboBnXlgG3LEfgN7MQqkf0-vVZufGrQpvRioRcBbesAiawMt4cspTk06ca
```

### 6. 根据code 置换 token

```http
POST /oauth2/token?grant_type=authorization_code&code=MCSJnvhXNyjilBaCyw1sCrrArWk1bzsEdxe5Z3EFbkdLwp8ASmum62n4M7Tz45VNpp_16IWboBnXlgG3LEfgN7MQqkf0-vVZufGrQpvRioRcBbesAiawMt4cspTk06ca&redirect_uri=https://www.baidu.com
Host: localhost:9000
Authorization: Basic bWVzc2FnaW5nLWNsaWVudDpzZWNyZXQ=
```

> 这里采用的认证方式是 `client-authentication-method: client_secret_basic`方式，详见OAuth2.0协议。

### 7. 授权服务器将Token返回给客户端，完成请求，

## [流程解析](https://www.yuque.com/pig4cloud/pig/dqnyuc)

### /oauth2/authorize

#### OAuth2AuthorizationEndpointFilter

```java
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

    if (!this.authorizationEndpointMatcher.matches(request)) {
        filterChain.doFilter(request, response);
        return;
    }

    try {
        // OAuth2AuthorizationCodeRequestAuthenticationConverter -> OAuth2AuthorizationCodeRequestAuthenticationToken
        Authentication authentication = this.authenticationConverter.convert(request);
        if (authentication instanceof AbstractAuthenticationToken) {
            ((AbstractAuthenticationToken) authentication)
                    .setDetails(this.authenticationDetailsSource.buildDetails(request));
        }
        
        // OAuth2AuthorizationCodeRequestAuthenticationProvider 验证 client
        Authentication authenticationResult = this.authenticationManager.authenticate(authentication);

        if (!authenticationResult.isAuthenticated()) {
            // If the Principal (Resource Owner) is not authenticated then
            // pass through the chain with the expectation that the authentication process
            // will commence via AuthenticationEntryPoint
            filterChain.doFilter(request, response);
            return;
        }

        if (authenticationResult instanceof OAuth2AuthorizationConsentAuthenticationToken) {
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("Authorization consent is required");
            }
            sendAuthorizationConsent(request, response,
                    (OAuth2AuthorizationCodeRequestAuthenticationToken) authentication,
                    (OAuth2AuthorizationConsentAuthenticationToken) authenticationResult);
            return;
        }

        this.authenticationSuccessHandler.onAuthenticationSuccess(
                request, response, authenticationResult);

    } catch (OAuth2AuthenticationException ex) {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(LogMessage.format("Authorization request failed: %s", ex.getError()), ex);
        }
        this.authenticationFailureHandler.onAuthenticationFailure(request, response, ex);
    }
}
```

##### OAuth2AuthorizationCodeRequestAuthenticationProvider 

```java

```

### /oauth2/token

#### OAuth2ClientAuthenticationFilter

```java
@Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

    if (!this.requestMatcher.matches(request)) {
        filterChain.doFilter(request, response);
        return;
    }

    try {
        // 此处的结果最终都是 OAuth2ClientAuthenticationToken
        // 调用 RegisteredClientRepository 来判断传入的 client 是否正确
        Authentication authenticationRequest = this.authenticationConverter.convert(request);
        if (authenticationRequest instanceof AbstractAuthenticationToken) {
            ((AbstractAuthenticationToken) authenticationRequest).setDetails(
                    this.authenticationDetailsSource.buildDetails(request));
        }
        if (authenticationRequest != null) {
            validateClientIdentifier(authenticationRequest);
            Authentication authenticationResult = this.authenticationManager.authenticate(authenticationRequest);
            this.authenticationSuccessHandler.onAuthenticationSuccess(request, response, authenticationResult);
        }
        filterChain.doFilter(request, response);

    } catch (OAuth2AuthenticationException ex) {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(LogMessage.format("Client authentication failed: %s", ex.getError()), ex);
        }
        this.authenticationFailureHandler.onAuthenticationFailure(request, response, ex);
    }
}
```

#### OAuth2TokenEndpointFilter

`OAuth2TokenEndpointFilter` 会接收通过上文 `OAuth2ClientAuthenticationFilter` 客户端认证的请求

```java
try {
    // 授权模式校验
    String[] grantTypes = request.getParameterValues(OAuth2ParameterNames.GRANT_TYPE);
    if (grantTypes == null || grantTypes.length != 1) {
        throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.GRANT_TYPE);
    }

    // 组装登录认证对象
    Authentication authorizationGrantAuthentication = this.authenticationConverter.convert(request);
    if (authorizationGrantAuthentication == null) {
        throwError(OAuth2ErrorCodes.UNSUPPORTED_GRANT_TYPE, OAuth2ParameterNames.GRANT_TYPE);
    }
    if (authorizationGrantAuthentication instanceof AbstractAuthenticationToken) {
        ((AbstractAuthenticationToken) authorizationGrantAuthentication)
                .setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    // 认证管理器认证
    OAuth2AccessTokenAuthenticationToken accessTokenAuthentication =
            (OAuth2AccessTokenAuthenticationToken) this.authenticationManager.authenticate(authorizationGrantAuthentication);
    // 成功
    this.authenticationSuccessHandler.onAuthenticationSuccess(request, response, accessTokenAuthentication);
} catch (OAuth2AuthenticationException ex) {
    SecurityContextHolder.clearContext();
    if (this.logger.isTraceEnabled()) {
        this.logger.trace(LogMessage.format("Token request failed: %s", ex.getError()), ex);
    }
    // 登录过程中的异常
    this.authenticationFailureHandler.onAuthenticationFailure(request, response, ex);
}
```

##### AuthenticationConverter

`AuthenticationConverter`  会根据请求中的参数和授权类型组装成对应的授权认证对象

`OAuth2AuthorizationCodeAuthenticationConverter` 授权码请求转换
`OAuth2RefreshTokenAuthenticationConverter` 刷新请求转换