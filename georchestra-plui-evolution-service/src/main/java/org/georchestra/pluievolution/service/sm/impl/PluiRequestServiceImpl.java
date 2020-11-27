/**
 * 
 */
package org.georchestra.pluievolution.service.sm.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.georchestra.pluievolution.core.common.DocumentContent;
import org.georchestra.pluievolution.core.dao.request.PluiRequestDao;
import org.georchestra.pluievolution.core.dto.*;
import org.georchestra.pluievolution.core.entity.request.PluiRequestEntity;
import org.georchestra.pluievolution.service.exception.ApiServiceException;
import org.georchestra.pluievolution.service.exception.DocumentRepositoryException;
import org.georchestra.pluievolution.service.helper.authentification.AuthentificationHelper;
import org.georchestra.pluievolution.service.helper.request.AttachmentHelper;
import org.georchestra.pluievolution.service.mapper.PluiRequestMapper;
import org.georchestra.pluievolution.service.sm.PluiRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author FNI18300
 *
 */
@Component
@Transactional(readOnly = true)
public class PluiRequestServiceImpl implements PluiRequestService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PluiRequestServiceImpl.class);
	
	@Autowired
	private AuthentificationHelper authentificationHelper;

	@Autowired
	private AttachmentHelper attachmentHelper;

	@Autowired
	PluiRequestMapper pluiRequestMapper;

	@Autowired
	PluiRequestDao pluiRequestDao;



	@Override
	@Transactional(readOnly = false)
	public Attachment addAttachment(UUID reportingUuid, DocumentContent documentContent)
			throws DocumentRepositoryException {
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public Attachment sendAttachment(UUID pluiRequestUuid, DocumentContent documentContent) throws ApiServiceException {
		PluiRequestEntity pluiRequestEntity = pluiRequestDao.findByUuid(pluiRequestUuid);
		if (pluiRequestEntity == null) {
			throw new ApiServiceException("Cette demande n'a pas encore été renseignée");
		}
		// On recupère le redmine id
		String redmineId = pluiRequestEntity.getRedmineId();

		// On verifie que le redmineId est valable
		if (redmineId == null || redmineId.equals("")) {
			throw new ApiServiceException("Cette demande n'a pas encore été envoyée à redmine");
		}

		// On envoie la pièce jointe au redmine

		Attachment result = new Attachment();
		result.setMimeType(documentContent.getContentType());
		result.setName(documentContent.getFileName());
		return result;
	}

	@Override
	public List<PluiRequestStatus> getAllRequestStatus() {
		return Arrays.asList(PluiRequestStatus.values());
	}

	@Override
	public List<PluiRequestType> getAllRequestType() {
		return Arrays.asList(PluiRequestType.values());
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
		//
	}


	@Override
	public AttachmentConfiguration getAttachmentConfiguration() {
		return attachmentHelper.getAttachmentConfiguration();
	}

	@Override
	@Transactional(readOnly = false)
	public PluiRequest createPluiRequest(PluiRequest pluiRequest) throws ApiServiceException {

		// On defini le statut de  la demande si non defini ou different de nouveau
		if (pluiRequest.getStatus() == null || pluiRequest.getStatus() != PluiRequestStatus.NOUVEAU) {
			pluiRequest.setStatus(PluiRequestStatus.NOUVEAU);
		}

		// On converti le dto en entité
		PluiRequestEntity pluiRequestEntity = pluiRequestMapper.dtoToEntity(pluiRequest);

		// On ajoute l'initiateur de la demande
		pluiRequestEntity.setInitiator(authentificationHelper.getUsername());
		// On defini la date de la demande
		pluiRequestEntity.setCreationDate(new Date());

		// TODO On envoie à Redmine et on recupere l'id redmine
		// Lever une exception si echec de l'envoi à redmine


		// On ajoute un UUID à la demande
		pluiRequestEntity.setUuid(UUID.randomUUID());
		// On enregistre la demande dans la bdd après lui avoir ajouté le redmine id retourné de léa précédente opération
		try {
			return this.pluiRequestMapper.entityToDto(
					this.pluiRequestDao.save(
							pluiRequestEntity
					)
			);
		} catch (DataAccessException e) {
			LOGGER.error("Erreur lors de l'enregistrement de la demande dans la BDD");
			throw new ApiServiceException(e.getMessage(), e);
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void deletePluiRequestByUuid(UUID uuid) throws ApiServiceException {
		try {
			pluiRequestDao.deleteByUuid(uuid);
		} catch (DataAccessException e) {
			LOGGER.error("Erreur lors de la suppression de la demande dans la BDD");
			throw new ApiServiceException(e.getMessage(), e);
		}
	}




}
