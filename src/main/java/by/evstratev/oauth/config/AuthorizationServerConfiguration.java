package by.evstratev.oauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AppOAuth2ClientProperties clientProperties;

    @Autowired
    private AuthenticationManager authenticationManager;

/*    @Autowired
    private DataSource dataSource;*/

    @Value("${app.security.oauth2.jwt.signingKey}")
    private String signingKey;

    @Bean
    public TokenStore tokenStore() {
        // To use JWT tokens you need a JwtTokenStore in your Authorization Server.
        // The Resource Server also needs to be able to decode the tokens so the JwtTokenStore
        // has a dependency on a JwtAccessTokenConverter, and the same implementation is needed
        // by both the Authorization Server and the Resource Server.
        // See https://projects.spring.io/spring-security-oauth/docs/oauth2.html
        return new JwtTokenStore(accessTokenConverter());
        //return new JdbcTokenStore(dataSource);
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        // You can use https://passwordsgenerator.net/ to generate some signingKey.
        converter.setSigningKey(signingKey);
        return converter;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(clientProperties.getClientId())
                .secret(clientProperties.getClientSecret())
                .authorizedGrantTypes(clientProperties.getAuthorizedGrantTypes().toArray(new String[0]))
                .scopes(clientProperties.getScopes().toArray(new String[0]))
                .resourceIds(clientProperties.getResourceIds().toArray(new String[0]))
                .accessTokenValiditySeconds(clientProperties.getAccessTokenValiditySeconds());

        // You can load clients from database. For that case you should create
        // the oauth_client_details table.
        // https://projects.spring.io/spring-security-oauth/docs/oauth2.html
        // clients.jdbc(dataSource);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        // By default the InMemoryTokenStore class is used.
        // We use JwtTokenStore. So we should add the next configuration:
        endpoints.tokenStore(tokenStore()).accessTokenConverter(accessTokenConverter());

/*
        // See https://docs.spring.io/spring-security-oauth2-boot/docs/2.2.6.RELEASE/reference/htmlsingle/#oauth2-boot-authorization-server-authentication-manager
        // Not all flows require an AuthenticationManager because not all flows have end users involved.
        // For example, the Client Credentials flow asks for a token based only on the client’s authority,
        // not the end user’s.
        if (authorizedGrantTypes.contains("password")) {
            endpoints.authenticationManager(this.authenticationManager);
        }*/
    }

}
