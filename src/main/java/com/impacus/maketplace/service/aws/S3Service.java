package com.impacus.maketplace.service.aws;

import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.service.CloudFileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.Tag;
import software.amazon.awssdk.services.s3.model.Tagging;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class S3Service implements CloudFileUploadService {

    private static final String UPLOAD_FILE_NAME_FORMAT = "yy-MM-dd-HH-mm-ss";
    private static final DateTimeFormatter UPLOAD_FILE_FORMATTER = DateTimeFormatter.ofPattern(UPLOAD_FILE_NAME_FORMAT);

    private final S3Client amazonS3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String s3BucketName;

    /**
     * S3에 파일을 업로드 후, 업로드 된 URL을 반환받는 함수
     *
     * @param file 업로드할 파일
     * @return
     */
    @Override
    public URI uploadFile(MultipartFile file, Path directoryPath) {
        // 1. MultipartFile을 File로 변환
        File uploadFile = convertAndSaveInLocal(file);
        if (uploadFile == null) {
            throw new CustomException(CommonErrorType.FAIL_TO_CONVERT_FILE);
        }

        // 2. 업로드할 파일명 지정
        String fileName = com.impacus.maketplace.common.utils.StringUtils.generateUniqueNumber();
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String fileKey = (extension == null) ? String.format("%s/%s", directoryPath, fileName) :
                String.format("%s/%s.%s", directoryPath, fileName, extension);

        // 3. S3에 업로드
        return putExcelInS3AndGetUrl(uploadFile, fileKey);
    }

    /**
     * s3 File을 올리는 함수
     */
    private URI putExcelInS3AndGetUrl(File uploadFile, String fileKey) {
        try {
            amazonS3Client.putObject(builder -> builder
                            .acl(ObjectCannedACL.PUBLIC_READ)
                            .bucket(s3BucketName)
                            .key(fileKey),
                    uploadFile.toPath());
            return amazonS3Client.utilities()
                    .getUrl(builder -> builder
                            .bucket(s3BucketName)
                            .key(fileKey))
                    .toURI();
        } catch (Exception ex) {
            throw new CustomException(ex);
        } finally {
            removeFileInLocal(uploadFile);
        }
    }

    /**
     * s3에 Excel File을 올리는 함수 (24시간 뒤 만료)
     *
     * @param fileData 엑셀 파일
     * @param fileKey 저장할 파일 경로
     * @return
     */
    public URI putExcelInS3AndGetUrl(byte[] fileData, String fileKey) {
        try {
            amazonS3Client.putObject(builder -> builder
                            .acl(ObjectCannedACL.PUBLIC_READ)
                            .bucket(s3BucketName)
                            .tagging(Tagging.builder()
                                    .tagSet(Tag.builder().key("status").value("delete").build())
                                    .build())
                            .key(fileKey),
                    RequestBody.fromBytes(fileData));
            return amazonS3Client.utilities()
                    .getUrl(builder -> builder
                            .bucket(s3BucketName)
                            .key(fileKey))
                    .toURI();
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * MultipartFile을 File로 변환하고, Local에 저장하는 함수
     *
     * @param file
     * @return
     */
    private File convertAndSaveInLocal(MultipartFile file) {
        File convertFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            if (convertFile.createNewFile()) {
                try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                    fos.write(file.getBytes());
                }
                return convertFile;
            }
            return null;
        } catch (IOException e) {
            throw new CustomException(e);
        }
    }

    /**
     * Local에 저장되어 있는 파일을 삭제하는 함수
     *
     * @param targetFile
     */
    private void removeFileInLocal(File targetFile) {
        targetFile.delete();
    }

    @Override
    public boolean deleteFile(String fileKey) {
        URI uri = URI.create(fileKey);

        amazonS3Client.deleteObject(builder -> builder
                .bucket(s3BucketName)
                .key(uri.getPath().substring(1, uri.getPath().length())));
        return true;
    }
}
