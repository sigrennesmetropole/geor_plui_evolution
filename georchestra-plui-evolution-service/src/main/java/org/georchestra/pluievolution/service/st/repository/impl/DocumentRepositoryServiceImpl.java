/**
 * 
 */
package org.georchestra.pluievolution.service.st.repository.impl;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.georchestra.pluievolution.core.common.DocumentContent;
import org.georchestra.pluievolution.core.dto.Attachment;
import org.georchestra.pluievolution.service.exception.ApiServiceException;
import org.georchestra.pluievolution.service.exception.DocumentRepositoryException;
import org.georchestra.pluievolution.service.helper.request.RedmineHelper;
import org.georchestra.pluievolution.service.st.repository.DocumentRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author FNI18300
 *
 */
@Service
@Transactional(readOnly = true)
public class DocumentRepositoryServiceImpl implements DocumentRepositoryService {

	@Autowired
	RedmineHelper redmineHelper;

	@Override
	@Transactional(readOnly = false)
	public Long createDocument(List<String> attachmentIds, DocumentContent documentContent)
			throws DocumentRepositoryException {
		return null;
	}

	@Override
	public DocumentContent getDocumentContent(Long attachmentId) throws DocumentRepositoryException {
		try {
			com.taskadapter.redmineapi.bean.Attachment attachment = redmineHelper.getAttachmentById(attachmentId.intValue());
			if (attachment == null) {
				throw new DocumentRepositoryException("");
			}
			DocumentContent documentContent = new DocumentContent();
			documentContent.setContentType(attachment.getContentType());
			documentContent.setFileName(attachment.getFileName());
			documentContent.setFileStream(new ByteArrayInputStream(redmineHelper.downloadAttachment(attachment)));

			return documentContent;

		} catch (ApiServiceException e) {
			throw new DocumentRepositoryException("", e);
		}
	}

	@Override
	public List<DocumentContent> getDocumentContents(String attachmentId) throws DocumentRepositoryException {
		List<DocumentContent> result = null;

		return result;
	}

	@Override
	public Attachment getDocument(Long id) {
		Attachment result = null;

		return result;
	}

	@Override
	public List<Attachment> getDocuments(String attachmentId) {
		List<Attachment> result = null;

		return result;
	}

	@Override
	public List<Long> getDocumentIds(String attachmentId) throws DocumentRepositoryException {
		List<Long> result = null;

		return result;
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteDocument(Long id) throws DocumentRepositoryException {

	}

	@Override
	@Transactional(readOnly = false)
	public void deleteDocuments(String attachmentId) throws DocumentRepositoryException {

	}
}
