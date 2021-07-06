package tz.go.tcra.lims.minio.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;

@Configuration
public class AppConfig {

	@Value("${minio.url}")
	private String s3Url;
	@Value("${minio.access.name}")
	private String accessKey;
	@Value("${minio.access.secret}")
	private String secretKey;

	@Value("${minio.buckek.name}")
	private String bucketName;

	@Bean
	public MinioClient s3Client() throws InvalidPortException, InvalidEndpointException {
		return new MinioClient(s3Url, accessKey, secretKey);
	}

}
