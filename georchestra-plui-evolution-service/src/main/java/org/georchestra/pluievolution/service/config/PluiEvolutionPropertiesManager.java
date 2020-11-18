package org.georchestra.pluievolution.service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;

@Configuration
@PropertySource("classpath:plui-evolution-common.properties")
@Data
public class PluiEvolutionPropertiesManager {

   
}
