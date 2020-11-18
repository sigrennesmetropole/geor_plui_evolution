/**
 * 
 */
package org.georchestra.pluievolution.service.sm.impl;

import java.util.List;
import java.util.UUID;

import org.georchestra.pluievolution.core.common.DocumentContent;
import org.georchestra.pluievolution.core.dto.Attachment;
import org.georchestra.pluievolution.core.dto.AttachmentConfiguration;
import org.georchestra.pluievolution.service.exception.DocumentRepositoryException;
import org.georchestra.pluievolution.service.helper.authentification.AuthentificationHelper;
import org.georchestra.pluievolution.service.helper.request.AttachmentHelper;
import org.georchestra.pluievolution.service.sm.RequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author FNI18300
 *
 */
@Component
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RequestServiceImpl.class);
	
	@Autowired
	private AuthentificationHelper authentificationHelper;
	
	@Autowired
	private AttachmentHelper attachmentHelper;

	@Override
	@Transactional(readOnly = false)
	public Attachment addAttachment(UUID reportingUuid, DocumentContent documentContent)
			throws DocumentRepositoryException {
		return null;
	}

	@Override
	public Attachment getAttachment(UUID reportingUuid, Long attachmentId) throws DocumentRepositoryException {
		Attachment result = null;
		
		return result;
	}

	@Override
	public List<Attachment> getAttachments(UUID reportingUuid) {
		List<Attachment> result = null;
		
		return result;
	}

	@Override
	public DocumentContent getAttachmentContent(UUID reportingUuid, Long attachmentId)
			throws DocumentRepositoryException {
		DocumentContent result = null;
		return result;
	}

	@Override
	@Transactional(readOnly = false)
	public void removeAttachment(UUID reportingUuid, Long attachmentId) throws DocumentRepositoryException {
		
	}


	@Override
	public AttachmentConfiguration getAttachmentConfiguration() {
		return attachmentHelper.getAttachmentConfiguration();
	}

}
