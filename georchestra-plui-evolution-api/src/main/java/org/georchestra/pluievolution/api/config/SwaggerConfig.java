package org.georchestra.pluievolution.api.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket productApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("org.georchestra.pluievolution.api.controller"))
				.paths(PathSelectors.any()).build().apiInfo(apiInfo()).securitySchemes(securitySchemes())
				.securityContexts(securityContexts());
	}

	protected ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Georchestra plui-evolution API").version("1.0").build();
	}

	protected List<SecurityScheme> securitySchemes() {
		List<SecurityScheme> result = new ArrayList<>();
		BasicAuth basicAuth = new BasicAuth("adm");
		result.add(basicAuth);
		return result;
	}

	protected List<SecurityContext> securityContexts() {
		List<SecurityContext> securityContexts = Arrays.asList(SecurityContext.builder()
				.forPaths(PathSelectors.regex("/administration/.*")).securityReferences(securityReferences()).build());
		return securityContexts;
	}

	private List<SecurityReference> securityReferences() {
		return Arrays.asList(new SecurityReference("adm", scopes()));
	}

	private AuthorizationScope[] scopes() {
		return new AuthorizationScope[] { new AuthorizationScope("read", "for read operations"),
				new AuthorizationScope("write", "for write operations") };
	}
}
