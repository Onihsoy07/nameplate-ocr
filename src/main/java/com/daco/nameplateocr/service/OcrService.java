package com.daco.nameplateocr.service;

import com.daco.nameplateocr.dto.OcrDto;
import com.daco.nameplateocr.exception.NotAttachMultipartFileException;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
public class OcrService {

    @Value("${file.dir}")
    private String FILEDIR;

    @Value("${file.tess-data}")
    private String TESSDATA;


    public OcrDto getReadForImageText(MultipartFile multipartFile) throws IOException, TesseractException {
        if (multipartFile.isEmpty()) {
            throw new NotAttachMultipartFileException("파일이 존재하지 않습니다");
        }

        // multipartFile에서 originalFilename를 받아 고유 이름으로 변경 및 저장할 파일 경로
        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        String fileFullPath = getFullPath(storeFileName);
        File originalImageFile = new File(fileFullPath);

        // multipartFile을 storeFileName으로 저장
        multipartFile.transferTo(originalImageFile);

        // 이미지 전처리 후 파일 반환
        File imagePretreatmentFile = getImagePretreatmentFile(originalImageFile);


        // Tesseract 객체 생성
        ITesseract tesseract = new Tesseract();

        // OCR 분석에 필요한 기준 데이터(폴더 위치 넣어줌)
        tesseract.setDatapath(TESSDATA);

        // 영어(eng) 학습 데이터 선택
        tesseract.setLanguage("kor");

        // OCR 작업 후 결과 반환
        String result = tesseract.doOCR(imagePretreatmentFile);

        // 저장한 이미지 파일 이름, 파일 경로, OCR 결과를 DTO로 반환
        return new OcrDto(storeFileName, fileFullPath, result);
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
    private File getConvertImageGrayScale(File file) {

    }


    // 이미지 전처리(노이즈 제거)
    private void ImageProcess(BufferedImage Input_Image, float scaleFactor, float offset,
                              String fileFullPath, String extension) throws IOException {
        // First of all, create an empty image buffer
        BufferedImage Output_Image = new BufferedImage(1050, 1024, Input_Image.getType());

        // Now create a 2D platform
        Graphics2D DemoGraphic = Output_Image.createGraphics();

        // draw a new image
        DemoGraphic.drawImage(Input_Image, 0, 0, 1050, 1024, null);
        DemoGraphic.dispose();

        // rescale the OP object for the grey images
        RescaleOp RescaleImage = new RescaleOp(scaleFactor, offset, null);

        // perform scaling
        BufferedImage Buffered_FOP_Image = RescaleImage.filter(Output_Image, null);
        ImageIO.write(Buffered_FOP_Image, extension,
                new File(fileFullPath));
    }

    // 조건별 이미지 전처리 작업
    private File getImagePretreatmentFile(File file) throws IOException {
        String imagePretreatmentFilePath = getImagePretreatmentFullPath(file.getName());
        String extension = extractedExt(imagePretreatmentFilePath);
        BufferedImage Input_Image = ImageIO.read(file);

        double Image_Double =
                Input_Image.getRGB(Input_Image.getTileWidth() / 2, Input_Image.getTileHeight() / 2);

        // compare the values
        if (Image_Double >= -1.4211511E7 && Image_Double < -7254228) {
            ImageProcess(Input_Image, 3f, -10f, imagePretreatmentFilePath, extension);
        } else if (Image_Double >= -7254228 && Image_Double < -2171170) {
            ImageProcess(Input_Image, 1.455f, -47f, imagePretreatmentFilePath, extension);
        } else if (Image_Double >= -2171170 && Image_Double < -1907998) {
            ImageProcess(Input_Image, 1.35f, -10f, imagePretreatmentFilePath, extension);
        } else if (Image_Double >= -1907998 && Image_Double < -257) {
            ImageProcess(Input_Image, 1.19f, 0.5f, imagePretreatmentFilePath, extension);
        } else if (Image_Double >= -257 && Image_Double < -1) {
            ImageProcess(Input_Image, 1f, 0.5f, imagePretreatmentFilePath, extension);
        } else if (Image_Double >= -1 && Image_Double < 2) {
            ImageProcess(Input_Image, 1f, 0.35f, imagePretreatmentFilePath, extension);
        }

        return new File(imagePretreatmentFilePath);
    }

}
