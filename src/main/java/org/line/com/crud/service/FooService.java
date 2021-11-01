package org.line.com.crud.service;

import org.springframework.stereotype.Service;


/**
 * @Author: yangcs
 * @Date: 2021/10/26 9:56
 * @Description:
 */
@Service
public class FooService  {

//    @Override
    public void once() {
        this.second();
    }

//    @Override
    public void second() {

    }
}
