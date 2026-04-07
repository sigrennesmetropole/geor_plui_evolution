package org.georchestra.pluievolution.api.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OpenApiSwaggerConfig {

	@Value("${swagger-server:" + "https://sigeo-srv.sig.rennesmetopole.fr:8443/pluievolution,"
			+ "http://sigeo-srv.sig.rennesmetopole.fr:8080/pluievolution,"
			+ "https://portail.sig.rennesmetropole.fr/pluievolution," + "}")
	private List<String> serverUrls;

	@Bean
	public OpenAPI springOpenAPI() {
		OpenAPI openApi = new OpenAPI().openapi("3.0.0").info(apiInfo()).components(apiComponents())
				.security(apiSecurityRequirements());
		if (CollectionUtils.isNotEmpty(serverUrls)) {
			openApi.servers(computeServers());
		}
		return openApi;
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

	protected List<Server> computeServers() {
		List<Server> result = new ArrayList<>();
		for (String serverUrl : serverUrls) {
			Server server = new Server();
			server.setUrl(serverUrl);
			result.add(server);
		}
		return result;
	}
}
