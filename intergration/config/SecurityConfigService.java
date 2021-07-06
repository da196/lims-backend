package tz.go.tcra.lims.intergration.config;

import org.apache.http.impl.client.HttpClients;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

/**
 * @author DonaldSj
 */

@Service
public class SecurityConfigService {

    public TokenResponse accessToken(String clientId, String clientSecret, String authTokenUrl, String grantType) {
        String credentials = clientId + ":" + clientSecret;
        String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));
        // RestTemplate restTemplate = new RestTemplate();

        ClientHttpRequestFactory requestFactory = new
                HttpComponentsClientHttpRequestFactory(HttpClients.createDefault());

        RestTemplate restTemplate = new RestTemplate(requestFactory);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Basic " + encodedCredentials);
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<String> request = new HttpEntity<String>(headers);

        String access_token_url = authTokenUrl;
        access_token_url += "?grant_type=" + grantType;

        ResponseEntity<TokenResponse> responseEntity = restTemplate.exchange(access_token_url, HttpMethod.POST, request,
                TokenResponse.class);

        System.out.println("Response is =>" + responseEntity.getBody());

        return responseEntity.getBody();
    }
}
