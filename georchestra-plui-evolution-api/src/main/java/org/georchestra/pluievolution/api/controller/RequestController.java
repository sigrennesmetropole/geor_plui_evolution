/**
 * 
 */
package org.georchestra.pluievolution.api.controller;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.georchestra.pluievolution.api.RequestApi;
import org.georchestra.pluievolution.core.dto.Attachment;
import org.georchestra.pluievolution.core.dto.PluiRequest;
import org.georchestra.pluievolution.core.dto.PluiRequestType;
import org.georchestra.pluievolution.core.dto.Status;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author FNI18300
 *
 */
public class RequestController implements RequestApi {

	@Override
	public ResponseEntity<PluiRequest> createPluiRequest(@Valid PluiRequest pluiRequest) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<Void> deletePluiRequest(UUID uuid) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<PluiRequest> getPluiRequestByUuid(UUID uuid) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<List<Status>> getPluiRequestStatus() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<List<PluiRequestType>> getPluiRequestTypes() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<Attachment> uploadDocument(UUID uuid, @Valid MultipartFile file) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
