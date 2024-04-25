package com.daco.nameplateocr.dto;

import com.daco.nameplateocr.dto.enumerate.OKorNG;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OcrDto {

    // OCR 결과
    private String text;

    // 합불 판정 결과(OK or NG)
    private OKorNG result;

}
