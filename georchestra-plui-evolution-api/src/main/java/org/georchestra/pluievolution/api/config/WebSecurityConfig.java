package org.georchestra.pluievolution.api.config;

import java.util.Arrays;
import java.util.Collections;

import javax.servlet.Filter;

import org.georchestra.pluievolution.api.security.PreAuthenticationFilter;
import org.georchestra.pluievolution.api.security.PreAuthenticationProvider;
import org.georchestra.pluievolution.service.sm.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().authorizeRequests().antMatchers("/", "/*.js", "/*.css", "/*.ico").permitAll()
				// On autorise l'accès aux json de conf pour l'authent
				.antMatchers("/assets/{\\p}/*.json").permitAll()
				// On autorise l'accès aux images pour l'authent
				.antMatchers("/assets/{\\p}/*.jpeg").permitAll().antMatchers("/assets/{\\p}/{\\p}/*.svg").permitAll()
				// On autorise certains WS utilisés pour l'authent
				.antMatchers("/plui-evolution/**").permitAll()
				// -- swagger ui
				.antMatchers("/csrf", "/plui-evolution/swagger-resources/**", "/plui-evolution/swagger-ui.html", "/webjars/**", "/v2/api-docs/**",
						"/configuration/ui", "/configuration/security")
				.permitAll().antMatchers("/administration/**").fullyAuthenticated().and().httpBasic().and()
				.addFilterAfter(createPreAuthenticationFilter(), BasicAuthenticationFilter.class).sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().csrf().disable();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "PUT", "DELETE"));
		configuration.addAllowedHeader("*");
		configuration.setAllowCredentials(true);

		// Url autorisées
		// 4200 pour les développement | 8080 pour le déploiement
		configuration.setAllowedOriginPatterns(Collections.singletonList("*"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("admin").password("{noop}4dM1nApp!").roles("ADMIN");
		auth.authenticationProvider(createPreAuthenticationProvider());
	}

	private AuthenticationProvider createPreAuthenticationProvider() {
		return new PreAuthenticationProvider();
	}

	private Filter createPreAuthenticationFilter() {
		return new PreAuthenticationFilter();
	}
}
