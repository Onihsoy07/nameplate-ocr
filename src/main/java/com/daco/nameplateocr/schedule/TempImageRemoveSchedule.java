package com.daco.nameplateocr.schedule;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;


@Slf4j
@Component
public class TempImageRemoveSchedule {

    @Value("${file.dir}")
    private String FILEDIR;

    // 매일 13시에 임시 파일 삭제
    @Scheduled(cron = "0 0 13 * * *", zone = "Asia/Seoul")
    public void tempImageRemoveInLunchTime() throws IOException {
        String tempPath = FILEDIR + "/temp";
        File tempFolder = new File(tempPath);

        if (tempFolder.exists()) {
            FileUtils.cleanDirectory(tempFolder);//하위 폴더와 파일 모두 삭제
        }

        log.info("TempImageRemoveSchedule - tempImageRemoveInLunchTime 실행 완료(temp 이미지 삭제 완료)");
    }

}
