/**
 * 
 */
package org.georchestra.pluievolution.service.st.mail.impl;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.georchestra.pluievolution.core.common.DocumentContent;
import org.georchestra.pluievolution.service.exception.EMailException;
import org.georchestra.pluievolution.service.st.mail.MailDescription;
import org.georchestra.pluievolution.service.st.mail.MailService;
import org.h2.util.IOUtils;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * @author FNI18300
 *
 */
@Service
public class MailServiceImpl implements MailService {

	@Value("${mail.transport.protocol}")
	private String protocol;

	@Value("${mail.smtp.host}")
	private String host;

	@Value("${mail.smtp.auth:false}")
	private boolean authentification;

	@Value("${mail.smtp.port:25}")
	private int port;

	@Value("${mail.smtp.user}")
	private String user;

	@Value("${mail.smtp.password}")
	private String password;

	@Value("${mail.from}")
	private String defaultFrom;

	@Value("${mail.smtp.starttls.enable:false}")
	private boolean ttlsEnable;

	@Value("${mail.debug:false}")
	private boolean debug;

	@Autowired
	public JavaMailSender emailSender;

	@Override
	public void sendMail(MailDescription mailDescription) throws EMailException {
		if (mailDescription == null) {
			throw new IllegalArgumentException("Mail description required");
		}

		if (StringUtils.isEmpty(mailDescription.getFrom())) {
			mailDescription.setFrom(getDefaultFrom());
		}
		try {
			MimeMessage message = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(mailDescription.getFrom());
			if (CollectionUtils.isNotEmpty(mailDescription.getTos())) {
				helper.setTo(mailDescription.getTos().toArray(new String[mailDescription.getTos().size()]));
			}
			if (CollectionUtils.isNotEmpty(mailDescription.getCcs())) {
				helper.setCc(mailDescription.getCcs().toArray(new String[mailDescription.getCcs().size()]));
			}
			if (CollectionUtils.isNotEmpty(mailDescription.getBccs())) {
				helper.setBcc(mailDescription.getBccs().toArray(new String[mailDescription.getBccs().size()]));
			}
			if (StringUtils.isNotEmpty(mailDescription.getSubject())) {
				helper.setSubject(mailDescription.getSubject());
			}

			if (mailDescription.getBody() != null) {
				handleBody(helper, mailDescription);
			}

			if (CollectionUtils.isNotEmpty(mailDescription.getAttachments())) {
				for (DocumentContent attachment : mailDescription.getAttachments()) {
					handleAttachment(helper, attachment);
				}
			}

			emailSender.send(message);
		} catch (Exception e) {
			throw new EMailException("Failed to send mail:" + mailDescription, e);
		}
	}

	private void handleAttachment(MimeMessageHelper helper, DocumentContent attachment)
			throws MessagingException, FileNotFoundException {
		if (attachment.isFile()) {
			FileSystemResource fileResource = new FileSystemResource(attachment.getFile());
			helper.addAttachment(attachment.getFileName(), fileResource);
		} else if (attachment.isStream()) {
			InputStreamResource inputStreamResource = new InputStreamResource(attachment.getFileStream());
			helper.addAttachment(attachment.getFileName(), inputStreamResource, attachment.getContentType());
		}
	}

	private void handleBody(MimeMessageHelper helper, MailDescription mailDescription)
			throws IOException, MessagingException {
		String text = null;
		InputStream bodyStream = null;
		try {
			bodyStream = mailDescription.getBody().getFileStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			IOUtils.copy(bodyStream, baos);
			text = baos.toString();
			if (mailDescription.isHtml()) {
				String plainText = extractPlainText(text);
				helper.setText(plainText, text);
			} else {
				helper.setText(text);
			}
		} finally {
			mailDescription.getBody().closeStream();
		}
	}

	@Override
	public String getDefaultFrom() {
		return defaultFrom;
	}

	private String extractPlainText(String text) {
		return Jsoup.parse(text).wholeText();
	}

	@Bean
	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setProtocol(protocol);
		mailSender.setHost(host);
		mailSender.setPort(port);
		mailSender.setDefaultEncoding(StandardCharsets.UTF_8.name());

		if (authentification) {
			mailSender.setUsername(user);
			mailSender.setPassword(password);
		}

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", protocol);
		props.put("mail.smtp.auth", Boolean.toString(authentification));
		props.put("mail.smtp.starttls.enable", Boolean.toString(ttlsEnable));
		props.put("mail.debug", Boolean.toString(debug));

		return mailSender;
	}
}
