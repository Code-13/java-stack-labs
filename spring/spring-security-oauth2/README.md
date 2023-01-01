## OAuth2.0

[OAuth 2.0 的四种方式](https://www.ruanyifeng.com/blog/2019/04/oauth-grant-types.html)

## JWT

- `JWT`：指的是 `JSON Web Token`，由 `header.payload.signature` 组成。不存在签名的 `JWT` 是不安全的，存在签名的 `JWT` 是不可窜改的。
- `JWS`：指的是签过名的 `JWT`，即拥有签名的 `JWT`。
- `JWK`：既然涉及到签名，就涉及到签名算法，对称加密还是非对称加密，那么就需要加密的 密钥或者公私钥对。此处我们将 `JWT` 的密钥或者公私钥对统一称为 `JSON WEB KEY`，即 `JWK`。