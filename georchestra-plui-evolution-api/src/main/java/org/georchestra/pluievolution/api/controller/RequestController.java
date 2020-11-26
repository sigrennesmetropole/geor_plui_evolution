/**
 * 
 */
package org.georchestra.pluievolution.api.controller;

import java.io.File;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import io.swagger.annotations.Api;
import org.apache.commons.io.FileUtils;
import org.georchestra.pluievolution.api.RequestApi;
import org.georchestra.pluievolution.core.common.DocumentContent;
import org.georchestra.pluievolution.core.dto.GeographicArea;
import org.georchestra.pluievolution.core.dto.PluiRequest;
import org.georchestra.pluievolution.core.dto.PluiRequestStatus;
import org.georchestra.pluievolution.core.dto.PluiRequestType;
import org.georchestra.pluievolution.service.acl.GeographicAreaService;
import org.georchestra.pluievolution.service.ref.RefService;
import org.georchestra.pluievolution.service.sm.PluiRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author FNI18300
 *
 */
@RestController
@Api(tags = "request")
public class RequestController implements RequestApi {

	@Autowired
	PluiRequestService pluiRequestService;

	@Autowired
	GeographicAreaService geographicAreaService;

	@Autowired
	RefService refService;

	@Override
	public ResponseEntity<PluiRequest> createPluiRequest(@Valid PluiRequest pluiRequest) throws Exception {
		return new ResponseEntity<>(pluiRequestService.createPluiRequest(pluiRequest), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Boolean> uploadDocument(UUID uuid, @Valid MultipartFile file) throws Exception {

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
	public ResponseEntity<List<GeographicArea>> getAllPluiRequestGeographicArea() throws Exception {
		return new ResponseEntity<>(geographicAreaService.getAllGeographicArea(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<PluiRequest> getPluiRequestByUuid(UUID uuid) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<List<PluiRequestStatus>> getPluiRequestStatus() throws Exception {
		return new ResponseEntity<>(refService.getAllRequestStatus(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<PluiRequestType>> getPluiRequestTypes() throws Exception {
		return new ResponseEntity<>(refService.getAllRequestType(), HttpStatus.OK);
	}

}
