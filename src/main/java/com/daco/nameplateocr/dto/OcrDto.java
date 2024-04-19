package com.daco.nameplateocr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OcrDto {

    private String fileName;

    private String filePath;

    private String result;

}
