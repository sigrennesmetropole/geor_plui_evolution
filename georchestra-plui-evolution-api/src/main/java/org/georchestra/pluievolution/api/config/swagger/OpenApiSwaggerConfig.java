package org.georchestra.pluievolution.api.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;

import java.util.Collections;
import java.util.List;

public class OpenApiSwaggerConfig {

	@Bean
	public OpenAPI springOpenAPI() {
		return new OpenAPI().openapi("3.0.0").info(apiInfo()).components(apiComponents()).security(apiSecurityRequirements());
	}

	protected Info apiInfo() {
		return new Info().title("Georchestra plui-evolution API").version("1.0");
	}

	protected Components apiComponents() {
		return new Components().addSecuritySchemes("basicauth", securityScheme());
	}
	protected SecurityScheme securityScheme() {
		return new SecurityScheme().type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP).scheme("basic");
	}

	protected List<SecurityRequirement> apiSecurityRequirements() {
		return Collections.singletonList(new SecurityRequirement().addList("basicauth"));
	}
}
