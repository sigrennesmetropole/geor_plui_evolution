/**
 * 
 */
package org.georchestra.pluievolution.service.st.mail;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * @author fni18300
 *
 */
@Configuration
@Getter
public class EMailConfiguration {

	@Value("${mail.transport.protocol:smtp}")
	private String protocol;

	@Value("${mail.smtp.host}")
	private String host;

	@Value("${mail.smtp.auth:false}")
	private boolean authentification;

	@Value("${mail.smtp.port:25}")
	private int port;

	@Value("${mail.smtp.user:}")
	private String user;

	@Value("${mail.smtp.password:}")
	private String password;

	@Value("${mail.from:ne-pas-repondre@org.shom.fr}")
	private String defaultFrom;

	@Value("${mail.smtp.starttls.enable:false}")
	private boolean ttlsEnable;

	@Value("${mail.smtp.debug:false}")
	private boolean debug;

	@Bean
	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setProtocol(getProtocol());
		mailSender.setHost(getHost());
		mailSender.setPort(getPort());
		mailSender.setDefaultEncoding(StandardCharsets.UTF_8.name());

		if (isAuthentification()) {
			mailSender.setUsername(getUser());
			mailSender.setPassword(getPassword());
		}

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", getProtocol());
		props.put("mail.smtp.auth", Boolean.toString(isAuthentification()));
		props.put("mail.smtp.starttls.enable", Boolean.toString(isTtlsEnable()));
		props.put("mail.debug", Boolean.toString(isDebug()));

		return mailSender;
	}
}
