package project.tripplan.domain.plan.file;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.tripplan.global.common.exception.CustomException;
import project.tripplan.global.common.response.BaseResponseCode;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

	private final AmazonS3Client amazonS3;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	public String uploadFile(MultipartFile multipartFile) {

		if (multipartFile == null || multipartFile.isEmpty()) {
			return null;
		}

		String fileName = createFileName(multipartFile.getOriginalFilename());
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentLength(multipartFile.getSize());
		objectMetadata.setContentType(multipartFile.getContentType());

		try (InputStream inputStream = multipartFile.getInputStream()) {
			amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
				.withCannedAcl(CannedAccessControlList.PublicRead));
		} catch (IOException e) {
			throw new CustomException(BaseResponseCode.FILE_UPLOAD_ERROR);
		}

		return fileName;
	}

	public String createFileName(String fileName) {
		return UUID.randomUUID().toString().concat(getFileExtension(fileName));
	}

	private String getFileExtension(String fileName) {
		try {
			return fileName.substring(fileName.lastIndexOf("."));
		} catch (StringIndexOutOfBoundsException e) {
			throw new CustomException(BaseResponseCode.FILE_FORMAT_FAIL);
		}
	}

	public void deleteFile(String fileName) {
		if (fileName == null || fileName.isEmpty()) {
			throw new CustomException(BaseResponseCode.FILE_DELETE_ERROR);
		}
		try {
			amazonS3.deleteObject(bucket, fileName);
		} catch (AmazonS3Exception e) {
			throw new CustomException(BaseResponseCode.FILE_DELETE_ERROR);
		} catch (Exception e) {
			throw new CustomException(BaseResponseCode.FILE_DELETE_ERROR);
		}
	}
}