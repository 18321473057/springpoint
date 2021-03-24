package org.line.com.bean.life;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @Author: yangcs
 * @Date: 2021/2/25 15:59
 * @Description:
 */
public class BranPostProcessTest1 implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        System.out.println("后处理 BranPostProcessTest1    前置方法");
        return o;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        System.out.println("后处理 BranPostProcessTest1 后置方法");
        return o;
    }
}
