package tz.go.tcra.lims.intergration.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.util.Base64;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tz.go.ega.signature.crypto.RSA;
import tz.go.ega.signature.dtos.CryptoData;

/**
 * @author DonaldSj
 */

@Service
@Slf4j
public class SignatureService<T> {

	private final SysConfig sysConfig;

	public SignatureService(SysConfig sysConfig) {
		this.sysConfig = sysConfig;
	}

	public CryptoData<T> generateSignedData(T invoiceDto) throws Exception {
		CryptoData<T> cryptoData = new CryptoData<>();

		System.out.println("IMEFIKA KWENYE SIGNED DATA");

		String limsPrivateKey = this.getStringPrivateKeyFromFile(); // My private key kept private. this is base 64

		PrivateKey aPrivate = RSA.getPrivate(limsPrivateKey); // client singing
		log.info("ABOUT TO CALL CONVERTER TO JSON ");
		String jsonPlainData = RSA.convertToJSON(invoiceDto);
		System.out.println("jsonPlainData => " + jsonPlainData);
		String signatureString = RSA.sign(jsonPlainData, aPrivate, sysConfig.getSignatureAlgorithm());

		log.info("signatureString => " + signatureString);

		cryptoData.setData(invoiceDto);
		cryptoData.setSignature(signatureString);
		return cryptoData;
	}

	public String getStringPrivateKeyFromFile() {
		File privateKeyFile = new File(sysConfig.getLimsPrivateKeyFile());

		System.out.println(
				privateKeyFile.getAbsoluteFile() + " " + privateKeyFile.getName() + " " + privateKeyFile.exists());
		String data = "";
		try {
			data = new String(Files.readAllBytes(Paths.get(privateKeyFile.getAbsolutePath())));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(data);
		return data;
	}

	public String getBase64AuthorizationStr(String credential) {
		byte[] credentials = credential.getBytes();
		byte[] base64CredBytes = Base64.getEncoder().encode(credentials);
		return new String(base64CredBytes);
	}

}