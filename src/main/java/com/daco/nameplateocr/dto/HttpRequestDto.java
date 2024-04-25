package com.daco.nameplateocr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HttpRequestDto<T> {

    /**
     * HTTP status code
     * 200 -> 정상 완료
     * 400 -> 클라이언트 요청 정보 이상
     * 등등 찾으면 나옴
     */
    private int statusCode;

    /**
     * HTTP 요청 결과
     * true -> 정상 실행 완료
     * false -> 비정상 종료
     */
    private Boolean success;

    /**
     * 성공 -> 성공 메시지
     * 실패 -> 에러 메시지
     */
    private String message;

    /**
     * 결과 데이터 담는 곳
     */
    private T data;

}
