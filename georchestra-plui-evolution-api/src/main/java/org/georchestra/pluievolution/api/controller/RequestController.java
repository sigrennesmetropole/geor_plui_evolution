/**
 * 
 */
package org.georchestra.pluievolution.api.controller;

import java.util.UUID;

import javax.validation.Valid;

import org.georchestra.pluievolution.api.RequestApi;
import org.georchestra.pluievolution.core.dto.Attachment;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author FNI18300
 *
 */
public class RequestController implements RequestApi {

	@Override
	public ResponseEntity<Void> deleteDocument(UUID uuid, Long attachmentId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<Resource> downloadDocument(UUID uuid, Long attachmentId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<Attachment> uploadDocument(UUID uuid, @Valid MultipartFile file) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
