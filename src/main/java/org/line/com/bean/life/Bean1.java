package org.line.com.bean.life;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class Bean1 implements InitializingBean ,BeanPostProcessor ,DisposableBean {

    public Bean1(){
        System.out.println("------------------------------构造方法!");
    }

    @PostConstruct
    public void postConstruct(){
        System.out.println("@postConstruct -- 后构造方法");
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("initializingBean.afterPropertiesSet()--初始化Bean 后配置方法  ");
    }

    public void initMethod(){
        System.out.println("@Bean.initMethod --  bean 初始化方法");
    }


    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        System.out.println("BeanpostProcessor.postprocessBeforInitialization -- 后处理bean--前置方法");
        return o;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        System.out.println("beanPostProcessor.postprocessAfterInitialization-- 后处理bean后置方法");
        return o;
    }

    @PreDestroy
    public  void  preDestroy(){
        System.out.println("@preDestroy 预销毁方法");
    }


    @Override
    public void destroy() throws Exception {
        System.out.println("disposableBean.destroy --(用完丢弃)销毁bean销毁方法 ");
    }

    public void destroyMethod(){
        System.out.println("@bean.destroyMethod . bean的销毁方法");
    }
}
