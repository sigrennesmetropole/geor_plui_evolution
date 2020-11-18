/**
 * 
 */
package org.georchestra.pluievolution.service.st.mail;

import java.util.ArrayList;
import java.util.List;

import org.georchestra.pluievolution.core.common.DocumentContent;

import lombok.Data;

/**
 * @author FNI18300
 *
 */
@Data
public class MailDescription {

	private String from;

	private List<String> tos;

	private List<String> ccs;

	private List<String> bccs;

	private String subject;

	private DocumentContent body;

	private boolean html = true;

	private List<DocumentContent> attachments;

	public void addTo(String to) {
		if (tos == null) {
			tos = new ArrayList<>();
		}
		tos.add(to);
	}

	public void addCc(String cc) {
		if (ccs == null) {
			ccs = new ArrayList<>();
		}
		ccs.add(cc);
	}

	public void addBcc(String bcc) {
		if (bccs == null) {
			bccs = new ArrayList<>();
		}
		bccs.add(bcc);
	}

	public void addAttachment(DocumentContent attachment) {
		if (attachments == null) {
			attachments = new ArrayList<>();
		}
		this.attachments.add(attachment);
	}
}
