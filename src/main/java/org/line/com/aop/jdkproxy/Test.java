package org.line.com.aop.jdkproxy;


import java.lang.reflect.Proxy;

/**
 * @Author: yangcs
 * @Date: 2021/3/1 15:12
 * @Description:
 */
public class Test {

    public static void main(String[] args) {
        RealSubject realSubject = new RealSubject();
        SubjectProxyInvocationHandler subjectProxy = new SubjectProxyInvocationHandler(realSubject);
        Subject subject = (Subject) Proxy.newProxyInstance(realSubject.getClass().getClassLoader(), realSubject.getClass().getInterfaces(), subjectProxy);
        subject.speak();

    }

}
