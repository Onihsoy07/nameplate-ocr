package com.daco.nameplateocr.repository.qeury;

import com.daco.nameplateocr.dto.enumerate.OKorNG;
import com.daco.nameplateocr.entity.NameplateOcrData;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.daco.nameplateocr.entity.QNameplateOcrData.*;

@Repository
@RequiredArgsConstructor
public class NameplateOcrDataQueryRepository {

    private final JPAQueryFactory query;

    // 지난 OCR 결과 데이터 조회
    public List<NameplateOcrData> search(LocalDateTime startData, LocalDateTime endDate,
                                            String lineName, String correctData, String checkResult) {
        List<NameplateOcrData> nameplateOcrDataList = query.select(nameplateOcrData)
                .from(nameplateOcrData)
                .where(dateFilter(startData, endDate),
                        lineNameEq(lineName),
                        correctDataEq(correctData),
                        checkResultEq(checkResult))
                .orderBy(nameplateOcrData.imageCreateDate.asc())
                .fetch();

        return nameplateOcrDataList;
    }


    /**
     * 검색 시 시작 날짜 또는 끝 날짜 없으면 모두 검색
     * 있으면 시작 날짜 00:00:00 ~ 끝 날짜 23:59:99 범위 데이터 검색
     */
    private BooleanExpression dateFilter(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            return null;
        }

        return nameplateOcrData.imageCreateDate.between(startDate, endDate);
    }

    /**
     * 검색 시 라인 이름 없으면 모두 검색
     * 있으면 라인 이름 맞는 데이터 검색
     */
    private BooleanExpression lineNameEq(String lineName) {
        return lineName != null ? nameplateOcrData.lineName.eq(lineName) : null;
    }

    /**
     * 검색 시 제품 확인 정보 없으면 모두 검색
     * 있으면 제품 확인 정보 맞는 데이터 검색
     */
    private BooleanExpression correctDataEq(String correctData) {
        return correctData != null ? nameplateOcrData.correctData.eq(correctData) : null;
    }

    /**
     * 검색 시 합불 판정 없으면 모두 검색
     * 있으면 합불 판정 맞는 데이터 검색
     */
    private BooleanExpression checkResultEq(String checkResult) {
        return checkResult != null ? nameplateOcrData.checkResult.eq(OKorNG.valueOf(checkResult)) : null;
    }

}
