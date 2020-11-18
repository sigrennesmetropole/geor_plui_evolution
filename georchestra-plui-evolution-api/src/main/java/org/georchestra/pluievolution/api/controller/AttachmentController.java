/**
 * 
 */
package org.georchestra.pluievolution.api.controller;

import org.georchestra.pluievolution.api.AttachmentApi;
import org.georchestra.pluievolution.core.dto.AttachmentConfiguration;
import org.georchestra.pluievolution.service.sm.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

/**
 * @author FNI18300
 *
 */
public class AttachmentController implements AttachmentApi {
	
	@Autowired
	private RequestService requestService;

	@Override
	public ResponseEntity<AttachmentConfiguration> getAttachmentConfiguration() throws Exception {
		return ResponseEntity.ok(requestService.getAttachmentConfiguration());
	}

}
