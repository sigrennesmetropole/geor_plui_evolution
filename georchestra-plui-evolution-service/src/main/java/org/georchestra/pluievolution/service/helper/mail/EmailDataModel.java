/**
 * 
 */
package org.georchestra.pluievolution.service.helper.mail;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.georchestra.pluievolution.core.dto.User;
import org.georchestra.pluievolution.service.st.generator.datamodel.DataModel;
import org.georchestra.pluievolution.service.st.generator.datamodel.GenerationFormat;
import org.georchestra.pluievolution.service.st.ldap.LdapService;

/**
 * @author FNI18300
 *
 */
public class EmailDataModel extends DataModel {

	private LdapService userService;

	/**
	 * @param executionEntity
	 * @param reportingEntity
	 * @param template
	 */
	public EmailDataModel(LdapService userService, String template) {
		super(GenerationFormat.HTML);
		this.userService = userService;
		setModelFileName(template);
	}

	@Override
	public Map<Object, Object> getDataModel() throws IOException {
		Map<Object, Object> datas = new HashMap<>();
		datas.put("dataModelUtils", this);
		return datas;
	}

	@Override
	protected String generateFileName() {
		return "emailBody.html";
	}

	public User getUser(String login) {
		return userService.getUserByLogin(login);
	}

}
