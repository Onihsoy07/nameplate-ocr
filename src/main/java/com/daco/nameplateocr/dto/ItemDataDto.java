package com.daco.nameplateocr.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDataDto {

    // 명판 검사할 이미지
    @NotNull(message = "이미지가 없습니다.")
    private MultipartFile image;

    // 라인명
    @NotBlank(message = "라인 이름이 없습니다.")
    private String lineName;

    // 확인 정보
    @NotBlank(message = "확인 할 제품 정보가 없습니다.")
    private String correctData;

}
