/**
 * 
 */
package org.georchestra.pluievolution.service.st.mail;

import org.georchestra.pluievolution.service.exception.EMailException;

/**
 * @author FNI18300
 *
 */
public interface MailService {

	void sendMail(MailDescription mailDescription) throws EMailException;

}
