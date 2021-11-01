package org.line.com.aop;

import org.line.com.aop.jdkproxy.Subject;

/**
 * @Author: yangcs
 * @Date: 2021/3/1 14:41
 * @Description: 代理目标类
 */
public class RealSubject implements Subject {

    @Override
    public void speak() {
        System.out.println("subject 实际运行代码");
    }



}
