package com.daco.nameplateocr.service;

import com.daco.nameplateocr.dto.ItemDataDto;
import com.daco.nameplateocr.dto.NameplateOcrDataDto;
import com.daco.nameplateocr.dto.enumerate.OKorNG;
import com.daco.nameplateocr.entity.NameplateOcrData;
import com.daco.nameplateocr.repository.NameplateOcrDataRepository;
import com.daco.nameplateocr.repository.qeury.NameplateOcrDataQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NameplateOcrDataService {

    private final NameplateOcrDataRepository nameplateOcrDataRepository;
    private final NameplateOcrDataQueryRepository nameplateOcrDataQueryRepository;


    // 명판 OCR 결과 데이터 DB에 저장
    public void save(ItemDataDto itemDataDto, String ocrResult, OKorNG checkResult, String finalImageSavePath) {
        String date = itemDataDto.getImage().getOriginalFilename().substring(0, 19);

        // 파일이름에서 LocalDateTime으로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
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

    // 명판 OCR 결과 데이터 검색
    public List<NameplateOcrDataDto> searchResultData(LocalDateTime startDate, LocalDateTime endDate,
                                                      String lineName, String correctData, String checkResult) {
        List<NameplateOcrData> nameplateOcrDataList = nameplateOcrDataQueryRepository.search(startDate, endDate, lineName, correctData, checkResult);

        List<NameplateOcrDataDto> nameplateOcrDataDtoList = NameplateOcrDataDto.convertToDtoList(nameplateOcrDataList);

        return nameplateOcrDataDtoList;
    }

}
