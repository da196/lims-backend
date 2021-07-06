package tz.go.tcra.lims.minio.service;

import java.io.File;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import tz.go.tcra.lims.attachments.dto.AttachmentDto;

@Service
public interface MinioS3Service {

	// String uploadFile(String bucketName, String originalFilename, byte[] bytes)
	// throws Exception;
	String uploadFile(String bucketName, String originalFilename, MultipartFile files) throws Exception;

	byte[] downloadFile(String bucketName, String fileUrl) throws Exception;

	void deleteFile(String bucketName, String fileUrl) throws Exception;

	List<String> listFiles(String bucketName) throws Exception;

	File upload(String bucketName, String name, byte[] content) throws Exception;

	byte[] getFile(String bucketName, String key) throws Exception;

	String getFileToDownload(String bucketName, String key, int expire) throws Exception;

	String uploadFileMult(String bucketName, AttachmentDto attachmentDto) throws Exception;
        
        String uploadFileInternal(File file) throws Exception;

}
