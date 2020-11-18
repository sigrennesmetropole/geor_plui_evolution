/**
 * 
 */
package org.georchestra.pluievolution.service.st.repository.impl;

import java.util.List;

import org.georchestra.pluievolution.core.common.DocumentContent;
import org.georchestra.pluievolution.core.dto.Attachment;
import org.georchestra.pluievolution.service.exception.DocumentRepositoryException;
import org.georchestra.pluievolution.service.st.repository.DocumentRepositoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author FNI18300
 *
 */
@Service
@Transactional(readOnly = true)
public class DocumentRepositoryServiceImpl implements DocumentRepositoryService {

	@Override
	@Transactional(readOnly = false)
	public Long createDocument(List<String> attachmentIds, DocumentContent documentContent)
			throws DocumentRepositoryException {
		return null;
	}

	@Override
	public DocumentContent getDocumentContent(Long id) throws DocumentRepositoryException {
		DocumentContent result = null;

		return result;
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
