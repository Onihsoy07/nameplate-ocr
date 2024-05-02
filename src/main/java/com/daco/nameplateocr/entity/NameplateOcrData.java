package com.daco.nameplateocr.entity;

import com.daco.nameplateocr.dto.enumerate.OKorNG;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// ORM
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(schema = "nameplate_ocr")
public class NameplateOcrData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = false)
    private String lineName;

    @Column(nullable = false, unique = false)
    private String correctData;

    @Column(nullable = false, unique = false)
    private LocalDateTime imageCreateDate;

    @Column(nullable = false, unique = false)
    private String ocrResultText;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = false)
    private OKorNG checkResult;

    @Column(nullable = false, unique = true)
    private String imagePath;

    @Builder
    public NameplateOcrData(String lineName, String correctData, LocalDateTime imageCreateDate, String ocrResultText, OKorNG checkResult, String imagePath) {
        this.lineName = lineName;
        this.correctData = correctData;
        this.imageCreateDate = imageCreateDate;
        this.ocrResultText = ocrResultText;
        this.checkResult = checkResult;
        this.imagePath = imagePath;
    }
}
