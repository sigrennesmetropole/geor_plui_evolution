/**
 * 
 */
package org.georchestra.pluievolution.service.st.ldap.impl;

import java.util.Map;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.georchestra.pluievolution.core.dto.User;
import org.springframework.ldap.core.AttributesMapper;

/**
 * @author FNI18300
 *
 */
public class UserAttributeMapper implements AttributesMapper<User> {

	public static final String LOGIN_FIELD = "login";

	public static final String FIRSTNAME_FIELD = "firstName";

	public static final String LASTNAME_FIELD = "lastName";

	public static final String ORGANIZATION_FIELD = "organization";

	public static final String EMAIL_FIELD = "email";

	private Map<String, String> attributeMappings;

	/**
	 * 
	 * @param attributeMappings
	 */
	public UserAttributeMapper(Map<String, String> attributeMappings) {
		this.attributeMappings = attributeMappings;
	}

	@Override
	public User mapFromAttributes(Attributes attributes) throws NamingException {
		User user = new User();
		user.setLogin(extractValue(attributes,LOGIN_FIELD));
		user.setFirstName(extractValue(attributes,FIRSTNAME_FIELD));
		user.setLastName(extractValue(attributes,LASTNAME_FIELD));
		user.setOrganization(extractValue(attributes,ORGANIZATION_FIELD));
		user.setEmail(extractValue(attributes,EMAIL_FIELD));
		return user;
	}

	private String extractValue(Attributes attributes, String fieldKey) throws NamingException {
		String result = null;
		String attributeName = attributeMappings.get(fieldKey);
		if (attributeName != null) {
			result = (String) attributes.get(attributeName).get();
		}
		return result;
	}

}
