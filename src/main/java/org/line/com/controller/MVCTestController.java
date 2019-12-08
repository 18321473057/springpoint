package org.line.com.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mvc")
public class MVCTestController {

    @RequestMapping("/get")
    public String get() {
        return "今天星期日";
    }

}
