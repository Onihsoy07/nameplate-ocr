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
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
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

    // 명판 OCR 결과 데이터 확인(단건, 이미지 확인)
    public NameplateOcrDataDto getResultData(Long id) throws IOException {
        // id 맞는 Entity 반환
        NameplateOcrData nameplateOcrData = getEntity(id);

        // Entity -> Dto
        NameplateOcrDataDto nameplateOcrDataDto = new NameplateOcrDataDto(nameplateOcrData);
        // Dto의 이미지 스트림에 이미지 바이트 스트림 넣기
        File file = new File(nameplateOcrData.getImagePath());
        nameplateOcrDataDto.setImageStream(FileCopyUtils.copyToByteArray(file));

        return nameplateOcrDataDto;
    }

    // id로 Entity 찾아 반환
    private NameplateOcrData getEntity(Long id) {
        return nameplateOcrDataRepository.findById(id).orElseThrow(() -> {
            throw new IllegalArgumentException("NameplateOcrData ID " + id + "를 찾을 수 없습니다");
        });
    }

}
