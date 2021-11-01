package org.line.com.aop.jdkproxy;


import org.line.com.aop.RealSubject;

import java.lang.reflect.Proxy;

/**
 * @Author: yangcs
 * @Date: 2021/3/1 15:12
 * @Description:
 */
public class Test {


//    -Dsun.misc.ProxyGenerator.saveGeneratedFiles=true  保存生成的代理文件

//    动态代理是因为 这个代理class是由JDK在运行时动态帮我们生成。
//    静态代理是编译阶段生成AOP代理类，也就是说生成的字节码就织入了增强后的AOP对象；（并不会创建出多余的对象）
    public static void main(String[] args) {
        RealSubject realSubject = new RealSubject();
        SubjectProxyInvocationHandler subjectProxy = new SubjectProxyInvocationHandler(realSubject);
        Subject subject = (Subject) Proxy.newProxyInstance(realSubject.getClass().getClassLoader(), realSubject.getClass().getInterfaces(), subjectProxy);
        subject.speak();
    }

}
