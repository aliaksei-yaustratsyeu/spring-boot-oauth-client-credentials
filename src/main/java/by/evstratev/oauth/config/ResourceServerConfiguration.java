package by.evstratev.oauth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;


/**
 * Adding {@code @EnableResourceServer} annotation adds
 * the {@link org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationProcessingFilter},
 * which validates token with {@link org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager#authenticate(Authentication)}.
 * <p>
 * The {@code OAuth2AuthenticationManager} uses {@link org.springframework.security.oauth2.provider.token.DefaultTokenServices}
 * or {@link org.springframework.security.oauth2.provider.token.RemoteTokenServices} to load
 * the token from the database or the authorization server(calling /oauth/check_token endpoint) respectively.
 * <p>
 * The {@code DefaultTokenServices} is used by default. If you would like to use {@code RemoteTokenServices} then add
 * this code for resource server configuration:
 * <pre>
 *     &#64;Bean
 *     public RemoteTokenServices tokenService() {
 *         RemoteTokenServices tokenService = new RemoteTokenServices();
 *         tokenService.setCheckTokenEndpointUrl(authProperties.getCheckTokenEndpointUrl());
 *         tokenService.setClientId(authProperties.getClientId());
 *         tokenService.setClientSecret(authProperties.getClientSecret());
 *         return tokenService;
 *     }
 * </pre>
 *
 * <p>
 * The {@code DefaultTokenServices} load token by default from {@code InMemoryTokenStore}.
 * In production you may want to use {@code JdbcTokenStore} or {@code JwtTokenStore} or {@code JwkTokenStore}.
 *
 * <p>
 * An OAuth 2 authentication token can contain two authentications: one for the client and one for the user. Since some
 * OAuth authorization grants don't require user authentication, the user authentication may be null.
 * See {@link org.springframework.security.oauth2.provider.OAuth2Authentication} class.
 *
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        // the id for the resource (optional, but recommended and will be validated if present)
        resources.resourceId("api");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // If a server is configured both as a resource server and as an authorization server,
        // then isolate your resource server endpoints to a targeted directory only.
        // By adding requestMatchers we avoid configuring over the top of endpoints
        // which define the authorization server (like /oauth/token, /oauth/check_token).
        http
            .requestMatchers()
                .antMatchers("/v1/**", "/v2/**")
                .and()
            .authorizeRequests()
                .antMatchers("/v1/**", "/v2/**").authenticated();
    }

}
