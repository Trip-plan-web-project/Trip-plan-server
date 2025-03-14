package project.tripplan.global.file;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.tripplan.domain.review.repository.ReviewImageRepositoryCustom;
import project.tripplan.global.common.exception.CustomException;
import project.tripplan.global.common.response.BaseResponseCode;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

	private final AmazonS3Client amazonS3;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	@Value("${cloud.aws.s3.review-bucket}")
	private String reviewBucket;

	private final ReviewImageRepositoryCustom reviewImageRepositoryCustom;

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

	public String uploadReviewFile(MultipartFile multipartFile) {

		if (multipartFile == null || multipartFile.isEmpty()) {
			return null;
		}

		String fileName = createFileName(multipartFile.getOriginalFilename());
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentLength(multipartFile.getSize());
		objectMetadata.setContentType(multipartFile.getContentType());

		try (InputStream inputStream = multipartFile.getInputStream()) {
			amazonS3.putObject(new PutObjectRequest(reviewBucket, fileName, inputStream, objectMetadata)
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

	public String copyFile(String fileName) {
		if (fileName == null || fileName.isBlank()) {
			return null;
		}

		String destKey = createFileName(fileName);

		amazonS3.copyObject(bucket, fileName, bucket, destKey);

		amazonS3.setObjectAcl(bucket, destKey, CannedAccessControlList.PublicRead);

		return destKey;
	}

	@Scheduled(fixedRate = 12 * 60 * 60 * 1000) // 12시간마다 실행
	public void cleanUpUnusedS3Images() {
		log.info("[S3 Cleanup] 시작");

		// 1. DB에서 imageUrl 전부 가져오기
		List<String> dbImageUrls = reviewImageRepositoryCustom.findAllImageUrls();

		// 2. 전체 URL일 경우 파일명만 추출
		List<String> dbFileNames = dbImageUrls.stream()
			.map(this::extractFileNameFromUrl)
			.collect(Collectors.toList());

		// 3. S3 버킷에서 모든 파일 목록 가져오기
		ListObjectsV2Request listReq = new ListObjectsV2Request().withBucketName(reviewBucket);
		ListObjectsV2Result listResult;

		List<String> s3FileKeys = new ArrayList<>();

		do {
			listResult = amazonS3.listObjectsV2(listReq);
			listResult.getObjectSummaries().forEach(s -> s3FileKeys.add(s.getKey()));
			listReq.setContinuationToken(listResult.getNextContinuationToken());
		} while (listResult.isTruncated());

		log.info("[S3 Cleanup] S3 총 파일 수: {}", s3FileKeys.size());

		// 4. DB에 없는 파일들만 필터링
		List<String> filesToDelete = s3FileKeys.stream()
			.filter(s3Key -> !dbFileNames.contains(s3Key)) // 파일 이름끼리 비교
			.collect(Collectors.toList());

		log.info("[S3 Cleanup] 삭제할 파일 수: {}", filesToDelete.size());

		// 5. 삭제
		for (String fileKey : filesToDelete) {
			amazonS3.deleteObject(reviewBucket, fileKey);
			log.info("[S3 Cleanup] 삭제됨: {}", fileKey);
		}

		log.info("[S3 Cleanup] 완료");
	}

	// url에서 파일명만 추출하는 메서드
	private String extractFileNameFromUrl(String url) {
		if (url == null || url.isBlank()) {
			return "";
		}

		try {
			// 마지막 '/' 뒤에 나오는 파일명 추출
			return url.substring(url.lastIndexOf("/") + 1);
		} catch (Exception e) {
			log.warn("[S3 Cleanup] 파일명 추출 실패: {}", url);
			return "";
		}
	}

}