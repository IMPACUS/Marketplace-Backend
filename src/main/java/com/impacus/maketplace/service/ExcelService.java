package com.impacus.maketplace.service;

import com.impacus.maketplace.common.constants.DirectoryConstants;
import com.impacus.maketplace.service.aws.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.net.URI;

@Service
@RequiredArgsConstructor
public class ExcelService {

    private final static String EXCEL_EXTENSION = "xlsx";
    private final S3Service s3Service;

    public String getExcelFileName() {
        String fileName = com.impacus.maketplace.common.utils.StringUtils.generateUniqueNumber();
        return String.format("%s/%s.%s", DirectoryConstants.EXCEL_DIRECTORY, fileName, EXCEL_EXTENSION);
    }

    public URI saveExcelInS3(ByteArrayOutputStream out) {
        return s3Service.putExcelInS3AndGetUrl(out.toByteArray(), getExcelFileName());
    }
}
