package com.daco.nameplateocr.controller.api;

import com.daco.nameplateocr.dto.OcrDto;
import com.daco.nameplateocr.exception.NotAttachMultipartFileException;
import com.daco.nameplateocr.service.OcrService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/ocr")
@RequiredArgsConstructor
public class OcrApiController {

    private final OcrService ocrService;

    @PostMapping
    public OcrDto getReadForImageText(@RequestParam("image") MultipartFile multipartFile) {
        OcrDto ocrDto = null;

        try {
            ocrDto = ocrService.getReadForImageText(multipartFile);
        } catch (IOException | NotAttachMultipartFileException | TesseractException e) {
            log.info("OcrApiController getReadForImageText 에러 발생", e);
        }

        return ocrDto;
    }


//    @PostMapping("/api/ocr")
//    public String DoOCR(@RequestParam("DestinationLanguage") String destinationLanguage,
//                        @RequestParam("Image") MultipartFile image) throws IOException {
//
//
//        OcrModel request = new OcrModel();
//        request.setDestinationLanguage(destinationLanguage);
//        request.setImage(image);
//
//        ITesseract instance = new Tesseract();
//
//        try {
//
//            BufferedImage in = ImageIO.read(convert(image));
//
//            BufferedImage newImage = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB);
//
//            Graphics2D g = newImage.createGraphics();
//            g.drawImage(in, 0, 0, null);
//            g.dispose();
//
//            instance.setLanguage(request.getDestinationLanguage());
//            instance.setDatapath("..//tessdata");
//
//            String result = instance.doOCR(newImage);
//
//            return result;
//
//        } catch (TesseractException | IOException e) {
//            System.err.println(e.getMessage());
//            return "Error while reading image";
//        }
//
//    }
//
//    public static File convert(MultipartFile file) throws IOException {
//        File convFile = new File(file.getOriginalFilename());
//        convFile.createNewFile();
//        FileOutputStream fos = new FileOutputStream(convFile);
//        fos.write(file.getBytes());
//        fos.close();
//        return convFile;
//    }

}
