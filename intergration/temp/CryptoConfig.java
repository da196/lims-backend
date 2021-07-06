package tz.go.tcra.lims.intergration.temp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author hcmis-development team
 * @date Dec 22, 2020
 * @version 1.0.0
 */

@Slf4j
@Component
public class CryptoConfig {
	
	@Autowired
	private Environment env;

	private String algorithm = "SHA256withRSA";

	public String getEgaPrivateKey() {
		File privateKeyFile = new File(env.getProperty("gisp.private.key"));
		
		log.info("filePath: "+privateKeyFile.getAbsoluteFile()+" does file exists: "+privateKeyFile.exists());
		String data = "";
		try {
			data = new String(Files.readAllBytes(Paths.get(privateKeyFile.getAbsolutePath())),StandardCharsets.UTF_8);
		} catch (IOException e) {
			log.error("Exception: {}",e);
		}
		log.info(data);
		return data;
	}
	
	public String getclientPublicKey() {
		File publicKeyFile = new File(env.getProperty("gisp.public.key"));
		
		log.info(publicKeyFile.getAbsoluteFile()+" "+publicKeyFile.getName()+" "+publicKeyFile.exists());
		String data = "";
		try {
			data = new String(Files.readAllBytes(Paths.get(publicKeyFile.getAbsolutePath())),StandardCharsets.UTF_8);
		} catch (IOException e) {
			log.error("Exception: {}",e);;
		}
		log.info(data);
		return data;
	}
	
	public String getAlgorithm() {
		
		return algorithm;
	}
}
