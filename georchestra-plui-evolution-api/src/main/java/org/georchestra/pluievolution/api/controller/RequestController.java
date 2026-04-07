/**
 * 
 */
package org.georchestra.pluievolution.api.controller;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.georchestra.pluievolution.api.RequestApi;
import org.georchestra.pluievolution.core.common.DocumentContent;
import org.georchestra.pluievolution.core.dto.Attachment;
import org.georchestra.pluievolution.core.dto.PluiRequest;
import org.georchestra.pluievolution.service.sm.PluiRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

/**
 * @author FNI18300
 *
 */
@RestController
@RequiredArgsConstructor
public class RequestController implements RequestApi {

	private final PluiRequestService pluiRequestService;

	@Override
	public ResponseEntity<PluiRequest> createPluiRequest(PluiRequest pluiRequest) throws Exception {
		return new ResponseEntity<>(pluiRequestService.createPluiRequest(pluiRequest), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<PluiRequest> updatePluiRequest(PluiRequest pluiRequest) throws Exception {
		return new ResponseEntity<>(pluiRequestService.updatePluiRequest(pluiRequest), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Attachment> uploadDocument(UUID uuid, MultipartFile file) throws Exception {

		File document = java.io.File.createTempFile(UUID.randomUUID().toString(), ".doc");
		FileUtils.copyInputStreamToFile(file.getInputStream(), document);
		DocumentContent content = new DocumentContent(file.getOriginalFilename(), file.getContentType(), document);
		return new ResponseEntity<>(pluiRequestService.sendAttachment(uuid, content), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Void> deletePluiRequest(UUID uuid) throws Exception {
		pluiRequestService.deletePluiRequestByUuid(uuid);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<Attachment>> getAttachementsByPluiRequestUuid(UUID uuid) throws Exception {
		return new ResponseEntity<>(pluiRequestService.getAttachments(uuid), HttpStatus.OK);
	}


	@Override
	public ResponseEntity<PluiRequest> getPluiRequestByUuid(UUID uuid) throws Exception {
		return ResponseEntity.ok(pluiRequestService.getPluiRequestByUuid(uuid));
	}

}
