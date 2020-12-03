package org.georchestra.pluievolution.api.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Classe de configuration globale de l'application.
 */
@SpringBootApplication
@ComponentScan({ "org.georchestra.pluievolution.api", "org.georchestra.pluievolution.service",
		"org.georchestra.pluievolution.core" })
@EntityScan(basePackages = "org.georchestra.pluievolution.core.entity")
@EnableJpaRepositories(basePackages = "org.georchestra.pluievolution.core.dao")
@PropertySource(value = { "file:${georchestra.datadir}/default.properties" }, ignoreResourceNotFound = false)
@PropertySource(value = { "file:${georchestra.datadir}/plui-evolution/plui-evolution.properties" }, ignoreResourceNotFound = false)
@PropertySource(value = { "classpath:plui-evolution-common.properties" }, ignoreResourceNotFound = false)
@EnableSwagger2
public class PluiEvolutionSpringBootApplication extends SpringBootServletInitializer {

	public static void main(final String[] args) {
		// Renomage du fichier de properties pour éviter les conflits avec d'autres
		// applications sur le tomcat
		System.setProperty("spring.config.name", "plui-evolution");
		SpringApplication.run(PluiEvolutionSpringBootApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
		return application.sources(PluiEvolutionSpringBootApplication.class);
	}
}
