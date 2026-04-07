package org.georchestra.pluievolution.api.config;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.georchestra.pluievolution.api.security.PreAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.Filter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

	private static final String[] SB_PERMIT_ALL_URL = { BasicSecurityConstants.SWAGGER_RESSOURCE_URL,
			BasicSecurityConstants.SWAGGER_RESSOURCE_UI, BasicSecurityConstants.SWAGGER_RESSOURCE_INDEX,
			BasicSecurityConstants.WEBJARS_URL, BasicSecurityConstants.API_DOCS_URL,
			BasicSecurityConstants.CONFIGURATION_SECURITY_URL, BasicSecurityConstants.CONFIGURATION_UI_URL,
			BasicSecurityConstants.ASSETS_URL, BasicSecurityConstants.ICONES_URL, BasicSecurityConstants.CSS_URL,
			BasicSecurityConstants.SLASH_URL, BasicSecurityConstants.JS_URL, BasicSecurityConstants.CRSF_URL };

	@Value("${security.authentication.disabled:false}")
	private boolean disableAuthentification = false;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		if (!disableAuthentification) {
			http.cors(Customizer.withDefaults()).csrf(csrf -> csrf.disable())
					.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
							.requestMatchers(SB_PERMIT_ALL_URL).permitAll().anyRequest().fullyAuthenticated())
					.exceptionHandling(Customizer.withDefaults())
					.sessionManagement(sessionManagement -> sessionManagement
							.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
					.addFilterAfter(createPreAuthenticationFilter(), BasicAuthenticationFilter.class);
		} else {
			http.cors(Customizer.withDefaults()).csrf(csrf -> csrf.disable())
					.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests.anyRequest().permitAll());
		}
		return http.build();
	}

	@Bean
	protected CorsConfigurationSource corsConfigurationSource() {
		final CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "PUT", "DELETE"));
		configuration.addAllowedHeader("*");
		configuration.setAllowCredentials(true);

		// Url autorisées
		// 4200 pour les développement | 8080 pour le déploiement
		configuration.setAllowedOriginPatterns(List.of("*"));

		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	protected GrantedAuthorityDefaults grantedAuthorityDefaults() {
		// Remove the ROLE_ prefix
		return new GrantedAuthorityDefaults("");
	}

	protected Filter createPreAuthenticationFilter() {
		return new PreAuthenticationFilter(ArrayUtils.addAll(SB_PERMIT_ALL_URL));
	}
}
