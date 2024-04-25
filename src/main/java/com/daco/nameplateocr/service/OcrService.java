package com.daco.nameplateocr.service;

import com.daco.nameplateocr.dto.ItemDataDto;
import com.daco.nameplateocr.dto.OcrDto;
import com.daco.nameplateocr.dto.enumerate.OKorNG;
import com.daco.nameplateocr.exception.NotAttachMultipartFileException;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;


/**
if (folder.exists()) {
    FileUtils.cleanDirectory(folder);//하위 폴더와 파일 모두 삭제
}
 */

@Slf4j
@Service
public class OcrService {

    @Value("${file.dir}")
    private String FILEDIR;

    @Value("${file.tess-data}")
    private String TESSDATA;


    public OcrDto getReadForImageText(ItemDataDto itemDataDto) throws IOException, TesseractException {
        MultipartFile multipartFile = itemDataDto.getImage();

        // multipartFile에서 originalFilename를 받아 고유 이름으로 변경 및 저장할 파일 경로
        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        String fileFullPath = getFullPath(storeFileName);
        File originalImageFile = new File(fileFullPath);
        String imagePretreatmentFullPath = getImagePretreatmentFullPath(storeFileName);

        // multipartFile을 storeFileName으로 저장
        // 사진 저장할 디렉토리 없으면 만들어서 저장
        try {
            multipartFile.transferTo(originalImageFile);
        } catch (IOException e) {
            Files.createDirectories(getFileDirectory(originalImageFile));
            multipartFile.transferTo(originalImageFile);
        }

        // 이미지 전처리 후 파일 반환
        getConvertImageGrayScale(originalImageFile, imagePretreatmentFullPath, extractedExt(imagePretreatmentFullPath));
        File imagePretreatmentFile = new File(imagePretreatmentFullPath);


        // Tesseract 객체 생성
        ITesseract tesseract = new Tesseract();

        // OCR 분석에 필요한 기준 데이터(폴더 위치 넣어줌)
        tesseract.setDatapath(TESSDATA);

        // 영어(eng) 학습 데이터 선택
        tesseract.setLanguage("eng+kor");

        // OCR 작업 후 결과 반환
        String ocrResult = tesseract.doOCR(imagePretreatmentFile);

        // 저장한 이미지 파일 이름, 파일 경로, OCR 결과를 DTO로 반환
        return new OcrDto(ocrResult, OKorNG.OK);
    }


    // 원본 이미지 저장 위치 반환
    private String getFullPath(String fileName) {
        return FILEDIR + "original/" + fileName;
    }

    // 전처리 이미지 저장 위치 반환
    private String getImagePretreatmentFullPath(String fileName) {
        return FILEDIR + "imagePretreatment/" + fileName;
    }

    // 기존 파일 이름 + UUID
    private String createStoreFileName(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        String fileName = getFileName(originalFilename);
        String ext = extractedExt(originalFilename);
        return fileName + "-" + uuid + "." + ext;
    }

    // originalFilename에서 파일 확장자 반환
    private String extractedExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    // originalFilename에서 파일 이름 반환
    private String getFileName(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(0, pos - 1);
    }

    // 이미지 그레이 스케일 변환
    private void getConvertImageGrayScale(File file, String imagePretreatmentFilePath, String ext) throws IOException {
        BufferedImage image = ImageIO.read(file);

        for(int y = 0; y < image.getHeight(); y++) {
            for(int x = 0; x < image.getWidth(); x++) {
                Color colour = new Color(image.getRGB(x, y));
//       Choose one from below
//       int Y = (int) (0.299 * colour.getRed() + 0.587 * colour.getGreen() + 0.114 * colour.getBlue());
                int Y = (int) (0.2126 * colour.getRed() + 0.7152 * colour.getGreen() + 0.0722 * colour.getBlue());
                image.setRGB(x, y, new Color(Y, Y, Y).getRGB());
            }
        }

        File imagePretreatmentFile = new File(imagePretreatmentFilePath);

        // 사진 저장할 디렉토리 없으면 만들어서 저장
        try {
            ImageIO.write(image, ext, imagePretreatmentFile);
        } catch (FileNotFoundException e) {
            Files.createDirectories(getFileDirectory(imagePretreatmentFile));
            ImageIO.write(image, ext, imagePretreatmentFile);
        }

    }

    // 현재 파일의 디렉토리 반환
    private Path getFileDirectory(File file) {
        String parentPath = file.getParent();

        return Path.of(parentPath);
    }

}
