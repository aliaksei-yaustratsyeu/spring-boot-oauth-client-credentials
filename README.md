# Spring Boot OAuth2 application demo with Client Credentials authorized grant type 

- Java 11
- Spring Boot 2.2.6
- Spring Security 5.2.2
- Authorization Service and Resource Service reside on one server
- The client_credentials authorized grant type
- In memory Client Details
- JWT access token


## How to use

Test authorization server - generating the token:
```shell script
$ curl -u some-client:secret -X POST localhost:8080/oauth/token\?grant_type=client_credentials
```

Test resource server:
```shell script
$ curl -H "Authorization: Bearer $access_token" http://localhost:8080/v1/hello
```

## Documentation

OAuth2 Boot https://docs.spring.io/spring-security-oauth2-boot/docs/2.2.6.RELEASE/reference/htmlsingle/

Note: do not use `spring-security-oauth2-autoconfigure` dependency. It's suited only
for simple cases which you can easily configure yourself. Add only `spring-security-oauth2`
dependency.

OAuth 2 Developers Guide https://projects.spring.io/spring-security-oauth/docs/oauth2.html

You can generate client secret using online Bcrypt Hash Generator and Checker:
https://www.devglan.com/online-tools/bcrypt-hash-generator .

Add {bcrypt} before encoded password so spring security will know 
which password hashing function to use for password validation.

For example `{bcrypt}$2a$07$1WmbnNsU6X74PSjsSQPxCeQI8CLdpNS7GuaBiiSDTZUrd1FNPpWxa` 
will match `secret` password.
See https://docs.spring.io/spring-security/site/docs/5.3.1.RELEASE/reference/html5/#authentication-password-storage-dpe

Secure Password Generator https://passwordsgenerator.net/