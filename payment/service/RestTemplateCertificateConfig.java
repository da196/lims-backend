package tz.go.tcra.lims.payment.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class RestTemplateCertificateConfig {

	@Bean
	public RestTemplate restTemplate() throws GeneralSecurityException, FileNotFoundException, IOException {
		KeyStore clientStore = KeyStore.getInstance("PKCS12");
		clientStore.load(new FileInputStream("/path/to/certfile"), "certpassword".toCharArray());

		SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
		sslContextBuilder.useProtocol("TLS");
		sslContextBuilder.loadKeyMaterial(clientStore, "certpassword".toCharArray());
		sslContextBuilder.loadTrustMaterial(new TrustSelfSignedStrategy());

		SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
				sslContextBuilder.build());
		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build();
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
		requestFactory.setConnectTimeout(10000); // 10 seconds
		requestFactory.setReadTimeout(10000); // 10 seconds
		return new RestTemplate(requestFactory);
	}

}
