/**
 * 
 */
package org.georchestra.pluievolution.api.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * This filter is inspired from georchestra geowebcache project
 * 
 * @author FNI18300
 *
 */
public class PreAuthenticationFilter implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(PreAuthenticationFilter.class);

	public static final String SEC_USERNAME = "sec-username";
	public static final String SEC_ROLES = "sec-roles";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			if( LOGGER.isInfoEnabled()) {
				Enumeration<String> names = httpServletRequest.getHeaderNames();
				while(names.hasMoreElements()) {
					LOGGER.info("header:{}", names.nextElement());
				}
			}
			final String username = httpServletRequest.getHeader(SEC_USERNAME);
			if (username != null) {
				SecurityContextHolder.getContext().setAuthentication(createAuthentication(httpServletRequest));

				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("Populated SecurityContextHolder with pre-auth token: '{}'",
							SecurityContextHolder.getContext().getAuthentication());
				}
			} else {
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("SecurityContextHolder not populated with pre-auth token");
				}
			}
		}

		chain.doFilter(request, response);
	}

	/**
	 * Construction du token pre-authentification
	 * 
	 * @param httpServletRequest
	 * @return
	 */
	private Authentication createAuthentication(HttpServletRequest httpServletRequest) {
		final String username = httpServletRequest.getHeader(SEC_USERNAME);
		final String rolesString = httpServletRequest.getHeader(SEC_ROLES);
		Set<String> roles = new LinkedHashSet<String>();
		if (rolesString != null) {
			roles.addAll(Arrays.asList(rolesString.split(";")));
		}
		return new PreAuthenticationToken(username, roles);
	}

}
