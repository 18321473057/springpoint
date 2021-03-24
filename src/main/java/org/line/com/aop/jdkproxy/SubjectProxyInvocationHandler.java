package org.line.com.aop.jdkproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Author: yangcs
 * @Date: 2021/3/1 14:47
 * @Description: 代理类
 */
public class SubjectProxyInvocationHandler implements InvocationHandler {

    private RealSubject realSubject;

    public SubjectProxyInvocationHandler(RealSubject realSubject) {
        this.realSubject = realSubject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("jdk invocationHandler.invoke is before");
        Object invoke = method.invoke(realSubject, args);
        System.out.println("jdk invocationHandler.invoke is after");
        return invoke;
    }

}
