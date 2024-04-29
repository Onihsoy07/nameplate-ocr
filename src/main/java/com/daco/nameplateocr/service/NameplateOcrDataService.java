package com.daco.nameplateocr.service;

import com.daco.nameplateocr.dto.ItemDataDto;
import com.daco.nameplateocr.dto.enumerate.OKorNG;
import com.daco.nameplateocr.entity.NameplateOcrData;
import com.daco.nameplateocr.repository.NameplateOcrDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NameplateOcrDataService {

    private final NameplateOcrDataRepository nameplateOcrDataRepository;


    // 명판 OCR 결과 데이터 DB에 저장
    public void save(ItemDataDto itemDataDto, String ocrResult, OKorNG checkResult, String finalImageSavePath) {
        String date = itemDataDto.getImage().getOriginalFilename().substring(0, 10);

        // 파일이름에서 LocalDateTime으로 변환
        //HH:mm:ss.SSS
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);

        NameplateOcrData nameplateOcrData = NameplateOcrData.builder()
                .lineName(itemDataDto.getLineName())
                .correctData(itemDataDto.getCorrectData())
                .ocrResultText(ocrResult)
                .checkResult(checkResult)
                .imagePath(finalImageSavePath)
                .imageCreateDate(LocalDateTime.now())
                .build();

        nameplateOcrDataRepository.save(nameplateOcrData);
    }

}
