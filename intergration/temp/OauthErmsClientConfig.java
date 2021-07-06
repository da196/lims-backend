package tz.go.tcra.lims.intergration.temp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OauthErmsClientConfig {

    @Value("${erms.security.oauth2.token.uri}")
    private String accessTokenUri;

//    @Bean
//    public RequestInterceptor oauth2FeignRequestInterceptor() {
//         return new OAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), resource());
//    }

    @Value("${erms.security.oauth2.client.id}")
    private String id;

    @Value("${erms.security.oauth2.client.client-id}")
    private String clientId;

    @Value("${erms.security.oauth2.client.client-secret}")
    private String secret;

    @Value("${erms.security.oauth2.client.grant-type}")
    private String grantType;

    @Value("${erms.security.oauth2.client.scope}")
    private String scope;

//    private OAuth2ProtectedResourceDetails resource() {
//
//        ClientCredentialsResourceDetails resourceDetails = new ClientCredentialsResourceDetails();
//        resourceDetails.setAccessTokenUri(accessTokenUri);
//        resourceDetails.setClientId(clientId);
//        resourceDetails.setId(id);
//        resourceDetails.setClientSecret(secret);
//        resourceDetails.setGrantType(grantType);
//        resourceDetails.setScope(Arrays.asList(scope.split(",")));
//        return resourceDetails;
//    }
}
