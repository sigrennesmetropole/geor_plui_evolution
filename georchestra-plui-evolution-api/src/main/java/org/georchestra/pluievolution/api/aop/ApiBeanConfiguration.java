package org.georchestra.pluievolution.api.aop;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy()
@ComponentScan(basePackages = {"org.georchestra.pluievolution.api.aop"})
public class ApiBeanConfiguration {


}
