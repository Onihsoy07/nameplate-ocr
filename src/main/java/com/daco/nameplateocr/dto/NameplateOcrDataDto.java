package com.daco.nameplateocr.dto;

import com.daco.nameplateocr.dto.enumerate.OKorNG;
import com.daco.nameplateocr.entity.NameplateOcrData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NameplateOcrDataDto {

    // 데이터베이스 Primary Key(데이터베이스 식별 아이디)
    private Long id;
    // 라인 이름
    private String lineName;
    // ocr 확인할 정보
    private String correctData;
    // 이미지 생성 날짜
    private LocalDateTime imageCreateDate;
    // ocr 결과
    private String ocrResultText;
    // ocr 결과 확인 후 합불판정
    private OKorNG checkResult;
    // 이미지 저장 경로
    private String imagePath;

    // NameplateOcrDataDto 생성자(데이터베이스 Entity를 Dto로 변환)
    public NameplateOcrDataDto(NameplateOcrData nameplateOcrData) {
        this.id = nameplateOcrData.getId();
        this.lineName = nameplateOcrData.getLineName();
        this.correctData = nameplateOcrData.getCorrectData();
        this.imageCreateDate = nameplateOcrData.getImageCreateDate();
        this.ocrResultText = nameplateOcrData.getOcrResultText();
        this.checkResult = nameplateOcrData.getCheckResult();
        this.imagePath = nameplateOcrData.getImagePath();
    }

    // 데이터베이스 Entity 리스트 -> NameplateOcrDataDto(외부로 전송할 데이터) 리스트 변환
    public static List<NameplateOcrDataDto> convertToDtoList(List<NameplateOcrData> nameplateOcrDataList) {
        return nameplateOcrDataList.stream().map(NameplateOcrDataDto::new).toList();
    }

}
