/**
 * 
 */
package org.georchestra.pluievolution.api.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * A provider that accepts {@link PreAuthenticationToken} authentication objects.
 *
 * @author from Jesse on 4/24/2014.
 */
public class PreAuthenticationProvider implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		if (authentication instanceof PreAuthenticationToken) {
            return authentication;
        }
        return null;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return PreAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
