/**
 * 
 */
package org.georchestra.pluievolution.api.controller;

import io.swagger.annotations.Api;
import org.georchestra.pluievolution.api.AttachmentApi;
import org.georchestra.pluievolution.core.dto.AttachmentConfiguration;
import org.georchestra.pluievolution.service.sm.PluiRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author FNI18300
 *
 */
@RestController
@Api(tags = "attachment")
public class AttachmentController implements AttachmentApi {
	
	@Autowired
	private PluiRequestService pluiRequestService;

	@Override
	public ResponseEntity<AttachmentConfiguration> getAttachmentConfiguration() throws Exception {
		return ResponseEntity.ok(pluiRequestService.getAttachmentConfiguration());
	}

}
