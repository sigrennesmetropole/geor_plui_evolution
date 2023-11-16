/**
 * 
 */
package org.georchestra.pluievolution.service.st.mail.impl;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.georchestra.pluievolution.core.common.DocumentContent;
import org.georchestra.pluievolution.service.exception.EMailException;
import org.georchestra.pluievolution.service.st.mail.EMailConfiguration;
import org.georchestra.pluievolution.service.st.mail.MailDescription;
import org.georchestra.pluievolution.service.st.mail.MailService;
import org.h2.util.IOUtils;
import org.jsoup.Jsoup;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * @author FNI18300
 *
 */
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {


	private final JavaMailSender emailSender;

	private final EMailConfiguration eMailConfiguration;

	@Override
	public void sendMail(MailDescription mailDescription) throws EMailException {
		if (mailDescription == null) {
			throw new IllegalArgumentException("Mail description required");
		}

		if (StringUtils.isEmpty(mailDescription.getFrom())) {
			mailDescription.setFrom(eMailConfiguration.getDefaultFrom());
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
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			bodyStream = mailDescription.getBody().getFileStream();
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

	private String extractPlainText(String text) {
		return Jsoup.parse(text).wholeText();
	}
}
