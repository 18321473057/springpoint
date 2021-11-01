package org.line.com.aop.cglib;

import org.line.com.aop.RealSubject;
import org.springframework.cglib.proxy.Enhancer;

/**
 * @Author: yangcs
 * @Date: 2021/10/28 10:06
 * @Description:
 */
public class CglibProxyFactory {
    // 此处需要说明：Enhancer实际属于CGLIB包的，也就是`net.sf.cglib.proxy.Enhancer`
    // 但是Spring把这些类都拷贝到自己这来了，因此我用的Spring的Enhancer，包名为;`org.springframework.cglib.proxy.Enhancer`
    public static void main(String[] args) throws Exception {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(RealSubject.class); // 注意此处的类型必须是实体类
        enhancer.setCallback(new MyMethodInterceptor());
        RealSubject realSubject = (RealSubject) enhancer.create();
        realSubject.speak();
    }

}
