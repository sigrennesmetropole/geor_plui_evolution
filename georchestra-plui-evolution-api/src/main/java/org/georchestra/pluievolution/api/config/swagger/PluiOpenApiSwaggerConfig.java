package org.georchestra.pluievolution.api.config.swagger;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PluiOpenApiSwaggerConfig extends OpenApiSwaggerConfig {

	@Bean
	public GroupedOpenApi publicApi(){
		return GroupedOpenApi.builder().group("plui-evolution-back")
				.packagesToScan("org.georchestra.pluievolution.api.controller")
				.build();
	}
}
