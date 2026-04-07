/**
 * 
 */
package org.georchestra.pluievolution.api.controller;

import org.georchestra.pluievolution.api.AttachmentApi;
import org.georchestra.pluievolution.api.controller.common.AbstractExportDocumentApi;
import org.georchestra.pluievolution.core.dto.AttachmentConfiguration;
import org.georchestra.pluievolution.service.sm.PluiRequestService;
import org.georchestra.pluievolution.service.st.repository.DocumentRepositoryService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

/**
 * @author FNI18300
 *
 */
@RestController
@RequiredArgsConstructor
public class AttachmentController extends AbstractExportDocumentApi implements AttachmentApi {
	
	
	private final PluiRequestService pluiRequestService;

	
	private final DocumentRepositoryService documentRepositoryService;

	@Override
	public ResponseEntity<AttachmentConfiguration> getAttachmentConfiguration() throws Exception {
		return ResponseEntity.ok(pluiRequestService.getAttachmentConfiguration());
	}

	@Override
	public ResponseEntity<Resource> downLoadAttachment(Long idAttachment) throws Exception {
		return downloadDocument(documentRepositoryService.getDocumentContent(idAttachment));
	}

}
