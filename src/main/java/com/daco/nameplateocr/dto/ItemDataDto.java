package com.daco.nameplateocr.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDataDto {

    @NotBlank(message = "라인 이름이 없습니다.")
    private String lineName;

    @NotBlank(message = "확인 할 제품 정보가 없습니다.")
    private String correctData;

}
