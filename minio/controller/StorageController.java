package tz.go.tcra.lims.minio.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import tz.go.tcra.lims.attachments.dto.AttachmentDto;
import tz.go.tcra.lims.minio.service.MinioS3Service;

@Slf4j
@RestController
@RequestMapping("/store-file")
public class StorageController {

	private MinioS3Service s3Service;

	@Autowired
	public StorageController(@Qualifier("minioS3ServiceImpl") MinioS3Service s3Service) {
		this.s3Service = s3Service;
	}

	@PostMapping(value = "/{bucketName}/files", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public Map<String, String> upload(@PathVariable("bucketName") String bucketName,
			@RequestPart(value = "file") MultipartFile files) throws Exception {

		s3Service.uploadFile(bucketName, files.getOriginalFilename(), files);
		Map<String, String> result = new HashMap<>();
		result.put("key", s3Service.uploadFile(bucketName, files.getOriginalFilename(), files));
		return result;
	}

	@PostMapping(value = "/{bucketName}/uploadFileMult")
	public String uploadFileMult(@PathVariable("bucketName") String bucketName,
			@ModelAttribute AttachmentDto attachmentDto) throws Exception {

		System.out.println(" REQUEST IN" + attachmentDto);

		return s3Service.uploadFileMult(bucketName, attachmentDto);
	}

	@GetMapping(value = "/{bucketName}/{keyName}", consumes = "application/octet-stream")
	public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable("bucketName") String bucketName,
			@PathVariable("keyName") String keyName) throws Exception {
		byte[] data = s3Service.downloadFile(bucketName, keyName);
		ByteArrayResource resource = new ByteArrayResource(data);

		return ResponseEntity.ok().contentLength(data.length).header("Content-type", "application/octet-stream")
				.header("Content-disposition", "attachment; filename=\"" + keyName + "\"").body(resource);
	}

	@DeleteMapping("/{bucketName}/files/{keyName}")
	public void delete(@PathVariable("bucketName") String bucketName, @PathVariable(value = "keyName") String keyName)
			throws Exception {
		s3Service.deleteFile(bucketName, keyName);
	}

	@GetMapping("/{bucketName}/files")
	public List<String> listObjects(@PathVariable("bucketName") String bucketName) throws Exception {
		return s3Service.listFiles(bucketName);
	}

	@GetMapping("/{bucketName}/files/{keyName}/{expire}")
	public String getFileToDownload(@PathVariable("bucketName") String bucketName,
			@PathVariable(value = "keyName") String key, @PathVariable(value = "expire") int expire) throws Exception {

		return s3Service.getFileToDownload(bucketName, key, expire);
	}

}
