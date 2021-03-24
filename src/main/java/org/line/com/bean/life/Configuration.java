package org.line.com.bean.life;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

@org.springframework.context.annotation.Configuration
public class Configuration {
    @Bean(initMethod = "initMethod", destroyMethod = "destroyMethod")
    @Scope("singleton")
    public Bean1 bean1() {
        return new Bean1();
    }


    @Bean
    public BranPostProcessTest1 branPostProcessTest1() {
        return new BranPostProcessTest1();
    }
}
