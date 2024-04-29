package com.daco.nameplateocr.controller.api;

import com.daco.nameplateocr.dto.HttpRequestDto;
import com.daco.nameplateocr.dto.ItemDataDto;
import com.daco.nameplateocr.dto.NameplateOcrDataDto;
import com.daco.nameplateocr.dto.OcrDto;
import com.daco.nameplateocr.exception.NotAttachMultipartFileException;
import com.daco.nameplateocr.service.NameplateOcrDataService;
import com.daco.nameplateocr.service.OcrService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/ocr")
@RequiredArgsConstructor
public class OcrApiController {

    private final OcrService ocrService;
    private final NameplateOcrDataService nameplateOcrDataService;

    /**
     * HTTP POST 요청 처리
     * url -> /api/ocr
     * method -> POST
     * 필요한 요청 데이터 -> MultipartFile(이미지 파일), String(라인 이름), String(양품 판정에 필요한 데이터)
     */
    @PostMapping
    public HttpRequestDto<OcrDto> getReadForImageText(final ItemDataDto itemDataDto,
                                                      BindingResult bindingResult) {
        OcrDto ocrDto = null;

        // 필요한 요청 데이터 없을 시 에러 메시지 담아서 반환
        if (bindingResult.hasErrors()) {
            String message = bindingResult.getAllErrors().get(0).getDefaultMessage();
            log.info(message);
            return new HttpRequestDto<>(HttpStatus.BAD_REQUEST.value(), false, message, null);
        }

        // OCR 주요 로직 실행
        try {
            ocrDto = ocrService.getReadForImageText(itemDataDto);
        } catch (IOException | NotAttachMultipartFileException | TesseractException e) {
            log.info("OcrApiController getReadForImageText 에러 발생", e);
        }

        // OCR 결과 데이터 담아 반환
        return new HttpRequestDto<>(HttpStatus.OK.value(), true, "OCR 성공", ocrDto);
    }

    /**
     * 명판 OCR 결과 데이터 검색 API
     * url -> /api/ocr
     * method -> GET
     * 필요한 요청 데이터 -> LocalDateTime startDate(검색할 시작 날짜, 필수 아님)
     *                    LocalDateTime endDate(검색할 시작 날짜, 필수 아님)
     *                    String lineName(검색할 라인 이름, 필수 아님)
     *                    String correctData(검색할 OCR 확인 정보, 필수 아님)
     *                    String checkResult(검색할 합불 판정, 필수 아님)
     */
    @GetMapping
    public HttpRequestDto<List<NameplateOcrDataDto>> searchOcrData(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd_HH:mm:ss") LocalDateTime startDate,
                                                                   @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd_HH:mm:ss") LocalDateTime endDate,
                                                                   @RequestParam(required = false) String lineName,
                                                                   @RequestParam(required = false) String correctData,
                                                                   @RequestParam(required = false) String checkResult) {
        List<NameplateOcrDataDto> nameplateOcrDataDtoList = nameplateOcrDataService.searchResultData(startDate, endDate, lineName, correctData, checkResult);

        return new HttpRequestDto<>(HttpStatus.OK.value(), true, "검색 성공", nameplateOcrDataDtoList);
    }

}
