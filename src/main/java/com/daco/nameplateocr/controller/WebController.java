package com.daco.nameplateocr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {

    /**
     * vue 페이지 return
     */
    @RequestMapping(value = {"/", "/ocr-search" ,"/ocr/**"})
    public String viewMapping() {
        return "forward:/index.html";
    }

}
