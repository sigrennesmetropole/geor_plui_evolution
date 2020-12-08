/**
 * 
 */
package org.georchestra.pluievolution.service.sm.impl;

import java.util.*;

import com.taskadapter.redmineapi.RedmineException;
import org.georchestra.pluievolution.core.common.DocumentContent;
import org.georchestra.pluievolution.core.dao.request.PluiRequestDao;
import org.georchestra.pluievolution.core.dto.*;
import org.georchestra.pluievolution.core.entity.acl.GeographicAreaEntity;
import org.georchestra.pluievolution.core.entity.request.PluiRequestEntity;
import org.georchestra.pluievolution.service.acl.GeographicAreaService;
import org.georchestra.pluievolution.service.exception.ApiServiceException;
import org.georchestra.pluievolution.service.exception.DocumentRepositoryException;
import org.georchestra.pluievolution.service.helper.authentification.AuthentificationHelper;
import org.georchestra.pluievolution.service.helper.request.AttachmentHelper;
import org.georchestra.pluievolution.service.helper.request.RedmineHelper;
import org.georchestra.pluievolution.service.mapper.GeographicAreaMapper;
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
	private AttachmentHelper attachmentHelper;

	@Autowired
	PluiRequestMapper pluiRequestMapper;

	@Autowired
	PluiRequestDao pluiRequestDao;

	@Autowired
	GeographicAreaService geographicAreaService;

	@Autowired
	GeographicAreaMapper geographicAreaMapper;

	@Autowired
	AuthentificationHelper authentificationHelper;

	private static final String CODE_INSEE_RENNES = "35238";
	private static final String CODE_INSEE_RM = "243500139";

	@Autowired
	RedmineHelper redmineHelper;

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
	public PluiRequest updatePluiRequest(PluiRequest pluiRequest) throws ApiServiceException {
		// On recupere la demande en base
		PluiRequestEntity entityInDb = null;
		if (pluiRequest.getUuid() != null) {
			entityInDb = pluiRequestDao.findByUuid(pluiRequest.getUuid());
		} else {
			throw new ApiServiceException("UUID non fourni");
		}

		if (entityInDb == null) {
			throw new ApiServiceException("Entité non trouvée en base");
		}

		// On converti le dto en entité
		PluiRequestEntity pluiRequestEntity = pluiRequestMapper.dtoToEntity(pluiRequest);

		// On defini le statut de  la demande si non defini ou different de nouveau
		if (pluiRequestEntity.getStatus() == null && entityInDb.getStatus() == null) {
			pluiRequestEntity.setStatus(PluiRequestStatus.NOUVEAU);
		} else {
			entityInDb.setStatus(pluiRequestEntity.getStatus());
		}

		// On defini la date de la demande
		if (entityInDb.getCreationDate() == null && pluiRequestEntity.getCreationDate() == null) {
			entityInDb.setCreationDate(new Date());
		} else if (entityInDb.getCreationDate() == null && pluiRequestEntity.getCreationDate() != null) {
			entityInDb.setCreationDate(pluiRequestEntity.getCreationDate());
		}

		if (entityInDb.getInitiator() == null) {
			entityInDb.setInitiator(authentificationHelper.getUsername());
		}

		// On trouve la geograohic area associée à la demande et on l'ajoute à la demande
		if (!entityInDb.getGeometry().equals(pluiRequestEntity.getGeometry())) {
			entityInDb.setArea(getPluiRequestArea(pluiRequestEntity));
		}

		// TODO On met à jour les info dans le redmine
		// Lever une exception si echec de l'envoi à redmine

		// On enregistre la demande dans la bdd après lui avoir ajouté le redmine id retourné de léa précédente opération
		try {
			return this.pluiRequestMapper.entityToDto(
					this.pluiRequestDao.save(
							entityInDb
					)
			);
		} catch (DataAccessException e) {
			LOGGER.error("Erreur lors de l'enregistrement de la demande dans la BDD");
			throw new ApiServiceException(e.getMessage(), e);
		}
	}

	private GeographicAreaEntity getPluiRequestArea(PluiRequestEntity pluiRequest) throws ApiServiceException {
		GeographicAreaEntity area = null;
		if (pluiRequest.getType() == PluiRequestType.COMMUNE) {
			// trouver la geographic area a partir des coordonnees
			area = geographicAreaService.getGeographicAreaByPoint(pluiRequest.getGeometry());
		} else if (pluiRequest.getType() == PluiRequestType.INTERCOMMUNE) {
			area = geographicAreaService.getGeographicAreaEntityByCodeInsee(CODE_INSEE_RENNES);
		} else if (pluiRequest.getType() == PluiRequestType.METROPOLITAIN) {
			area = geographicAreaService.getGeographicAreaEntityByCodeInsee(CODE_INSEE_RM);

		} else {
			throw new ApiServiceException("Le type de demande doit être précisé");
		}

		if (area == null) {
			throw new ApiServiceException("Organisation inconnue", "404");
		}
		return area;
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
	public PluiRequest createPluiRequest(PluiRequest pluiRequest) throws ApiServiceException, RedmineException {

		// On defini le statut de  la demande si non defini ou different de nouveau
		if (pluiRequest.getStatus() == null || pluiRequest.getStatus() != PluiRequestStatus.NOUVEAU) {
			pluiRequest.setStatus(PluiRequestStatus.NOUVEAU);
		}

		// On converti le dto en entité
		PluiRequestEntity pluiRequestEntity = pluiRequestMapper.dtoToEntity(pluiRequest);

		// On defini la date de la demande
		pluiRequestEntity.setCreationDate(new Date());

		// On trouve la geograohic area associée à la demande et on l'ajoute à la demande
		String initiator = authentificationHelper.getUsername();

		// Peut etre en fonction de l'area trouvé et de l'organisation de l'initiateur dire si oui ou non il est autorisé à créer cette demande

		// On ajoute l'initiateur de la demande
		pluiRequestEntity.setInitiator(initiator);
		pluiRequestEntity.setArea(getPluiRequestArea(pluiRequestEntity));

		// On ajoute un UUID à la demande
		pluiRequestEntity.setUuid(UUID.randomUUID());

		// TODO On envoie à Redmine et on recupere l'id redmine
		// Lever une exception si echec de l'envoi à redmine
		pluiRequestEntity = redmineHelper.createPluiRequestIssue(pluiRequestEntity);

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
