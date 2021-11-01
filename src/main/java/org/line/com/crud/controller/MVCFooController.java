package org.line.com.crud.controller;

import org.line.com.crud.service.FooService;
import org.line.com.crud.service.IFooService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mvc")
public class MVCFooController {

    @Autowired
    private FooService fooService;
    @Autowired
    private ApplicationContext applicationContext;


    //我想知道注入的到底是什么
    @RequestMapping("/real")
    public void  realBean(){
        System.out.println(fooService);
        FooService bean = applicationContext.getBean(FooService.class);
        if(bean == fooService){
            System.out.println("bean == fooService");
        }
        fooService.once();
    }


    @RequestMapping("/get")
    public String get() {
        return "今天星期日";
    }


}
