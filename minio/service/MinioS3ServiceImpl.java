package tz.go.tcra.lims.minio.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import io.minio.Result;
import io.minio.http.Method;
import io.minio.messages.Item;
import tz.go.tcra.lims.attachments.dto.AttachmentDto;

@Service
public class MinioS3ServiceImpl implements MinioS3Service {

	@Value("${minio.buckek.name}")
	private String bucketName1;

	@Value("${minio.temp.dir}")
	private String path;

	private final MinioClient s3;

	@Autowired
	public MinioS3ServiceImpl(MinioClient s3) {
		this.s3 = s3;
	}

	@Override
	public String uploadFile(String bucketName, String originalFilename, MultipartFile files) throws Exception {
		String fileName = removeSpace(originalFilename);// + ("_") + new Timestamp(System.currentTimeMillis());

		// File file = upload(bucketName1, fileName, bytes);
		// s3.putObject(bucketName1, fileName, new FileInputStream(file), new
		// PutObjectOptions(file.length(), -1));
		s3.putObject(bucketName1, fileName, files.getInputStream(), new PutObjectOptions(files.getSize(), -1));

		return fileName;
	}

	@Override
	public byte[] downloadFile(String bucketName, String fileUrl) throws Exception {
		return getFile(bucketName1, fileUrl);
	}

	@Override
	public void deleteFile(String bucketName, String fileUrl) throws Exception {
		s3.removeObject(bucketName1, fileUrl);
	}

	@Override
	public List<String> listFiles(String bucketName) throws Exception {
		List<String> list = new LinkedList<>();
		Iterable<Result<Item>> buckets = s3.listObjects(bucketName1);
		for (Result<Item> itemResult : buckets) {
			Item item = itemResult.get();
//			System.out.println(item.userMetadata());
//			System.out.println(item.objectName());
//			System.out.println(item.storageClass());
			list.add(item.objectName());
		}
		return list;
	}

	@Override
	public File upload(String bucketName, String name, byte[] content) throws Exception {
		// File file = new File("C://logs//" + name);
		File file = new File(path + name);
		file.canWrite();
		file.canRead();
		FileOutputStream iofs = null;
		iofs = new FileOutputStream(file);
		iofs.write(content);
		System.out.println("HILI HAPA" + file);
		return file;
	}

	@Override
	public byte[] getFile(String bucketName, String key) throws Exception {
		InputStream stream = s3.getObject(bucketName1, key);
		try {
			byte[] content = IOUtils.toByteArray(stream);
			stream.close();
			return content;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getFileToDownload(String bucketName, String key, int expire) throws Exception {

		String url = null;
		if (listFiles(bucketName1).contains(key)) {
			url = s3.getPresignedObjectUrl(Method.GET, bucketName1, key, expire, null);
		}
		return url;
	}

	public String removeSpace(String name) {
		if (!StringUtils.isEmpty(name)) {
			name = name.replaceAll("\\s+", "_");
			int lastDotIndex = name.lastIndexOf('.');
			name = name.substring(0, lastDotIndex) + (new Timestamp(System.currentTimeMillis())).getTime()
					+ name.substring(lastDotIndex);
		}
		return name;
	}

	@Override
	public String uploadFileMult(String bucketName, AttachmentDto attachmentDto) throws Exception {

		String fileName = removeSpace(attachmentDto.getFiles().getOriginalFilename());// + ("_") + new
																						// Timestamp(System.currentTimeMillis());

		s3.putObject(bucketName1, fileName, attachmentDto.getFiles().getInputStream(),
				new PutObjectOptions(attachmentDto.getFiles().getSize(), -1));
		return fileName;
	}

    @Override
    public String uploadFileInternal(File file) throws Exception {
        
        s3.putObject(bucketName1, file.getName(), file.getAbsolutePath(), new PutObjectOptions(file.length(), -1));
        return file.getName();
    }

}
