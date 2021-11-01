package org.line.com.aop.cglib;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

// 注意：这个是org.springframework.cglib.proxy.MethodInterceptor
// 而不是org.aopalliance.intercept包下的
public class MyMethodInterceptor implements MethodInterceptor {

    @Override
    public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Object intercept = methodProxy.invokeSuper(object, args); // 注意这里调用的是methodProxy.invokeSuper
        System.out.println("中介：该房源已发布！");
        return intercept;
    }
}
