//package tz.go.tcra.lims.payment.service;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.security.KeyStore;
//
//import org.apache.http.Consts;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
//import org.apache.http.conn.ssl.SSLContexts;
//import org.apache.http.entity.ContentType;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//@Service
//public class ErmsCallService {
//	@Value("${lims.erms.uri}")
//	private String uri;
//
//	@Value("${lims.erms.keyStoreFile}")
//	private String keystoreFile;
//
//	@Value("${lims.erms.keyStorePassword}")
//	private String keyStoreFilePassword;
//
//	@Value("${lims.erms.contentTypeString}")
//	private String contentTypeString;
//
//	public void sendInvoice(String content) {
//
//		CloseableHttpClient client = HttpClients.custom()
//				.setSSLSocketFactory(getFactory(new File(keystoreFile), keyStoreFilePassword)).build();
//		HttpPost post = new HttpPost(uri);
//		post.setHeader("Content-Type", contentTypeString);
//
//		StringEntity entity = new StringEntity(content, ContentType.create(contentTypeString, Consts.UTF_8));
//		entity.setChunked(true);
//		post.setEntity(entity);
//		try {
//			HttpResponse responseHttp = client.execute(post);
//		} catch (ClientProtocolException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	private SSLConnectionSocketFactory getFactory(File pKeyFile, String pKeyPassword) {
//		try {
//			// New Added for SSL
//			KeyStore localKeyStore = KeyStore.getInstance("PKCS12");
//			char[] localKeyStorePassword = pKeyPassword.toCharArray();
//			localKeyStore.load(new FileInputStream(pKeyFile), localKeyStorePassword);
//
//			// SSL Context
//			javax.net.ssl.SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(localKeyStore)
//					.loadKeyMaterial(localKeyStore, localKeyStorePassword).build();
//
//			// create socket and build http custom client
//			SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslcontext,
//					new String[] { "TLSv1.2", "TLSv1.1", "TLSv1" }, null,
//					SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//
//			return sslSocketFactory;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
//
//}
