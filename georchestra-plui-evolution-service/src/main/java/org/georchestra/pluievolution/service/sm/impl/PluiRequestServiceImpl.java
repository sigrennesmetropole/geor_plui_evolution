/**
 * 
 */
package org.georchestra.pluievolution.service.sm.impl;

import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.bean.Issue;
import org.georchestra.pluievolution.core.common.DocumentContent;
import org.georchestra.pluievolution.core.dao.request.PluiRequestDao;
import org.georchestra.pluievolution.core.dto.Attachment;
import org.georchestra.pluievolution.core.dto.AttachmentConfiguration;
import org.georchestra.pluievolution.core.dto.PluiRequest;
import org.georchestra.pluievolution.core.dto.PluiRequestStatus;
import org.georchestra.pluievolution.core.dto.PluiRequestType;
import org.georchestra.pluievolution.core.entity.acl.GeographicAreaEntity;
import org.georchestra.pluievolution.core.entity.acl.GeographicEtablissementEntity;
import org.georchestra.pluievolution.core.entity.request.PluiRequestEntity;
import org.georchestra.pluievolution.service.acl.GeographicAreaService;
import org.georchestra.pluievolution.service.acl.GeographicEtablissementService;
import org.georchestra.pluievolution.service.exception.ApiServiceException;
import org.georchestra.pluievolution.service.helper.authentification.AuthentificationHelper;
import org.georchestra.pluievolution.service.helper.request.AttachmentHelper;
import org.georchestra.pluievolution.service.helper.request.RedmineHelper;
import org.georchestra.pluievolution.service.mapper.GeographicAreaMapper;
import org.georchestra.pluievolution.service.mapper.GeographicEtablissementMapper;
import org.georchestra.pluievolution.service.mapper.PluiRequestMapper;
import org.georchestra.pluievolution.service.sm.PluiRequestService;
import org.locationtech.jts.geom.Geometry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;



/**
 * @author FNI18300
 *
 */
@Service
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
	GeographicEtablissementService geographicEtablissementService;

	@Autowired
	GeographicAreaMapper geographicAreaMapper;

	@Autowired
	GeographicEtablissementMapper geographicEtablissementMapper;

	@Autowired
	AuthentificationHelper authentificationHelper;

	private static final String CODE_INSEE_RM = "243500139";
	private static final String COMMUNAL_REQUEST_LOCALISATION_NOT_FOUND = "La localisation de la demande communale ne peut etre vide";
	private static final String REQUEST_TYPE_NOT_FOUND = "Le type de demande doit etre fourni";

	@Autowired
	RedmineHelper redmineHelper;

	@Override
	@Transactional(readOnly = true)
	public Attachment sendAttachment(UUID pluiRequestUuid, DocumentContent documentContent) throws ApiServiceException, IOException, RedmineException {
		PluiRequestEntity pluiRequestEntity = pluiRequestDao.findByUuid(pluiRequestUuid);
		if (pluiRequestEntity == null) {
			throw new ApiServiceException("Cette demande n'a pas encore été renseignée");
		}
		// Verification de la piece jointe
			// Verification du mimetype
		if (!getAttachmentConfiguration().getMimeTypes().contains(documentContent.getContentType())) {
			throw new ApiServiceException(String.format("Les fichiers de types %s ne sont pas autorisés", documentContent.getContentType()));
		}
			// Verification de la taille du fichier
		if (getAttachmentConfiguration().getMaxSize() < documentContent.getFileSize()) {
			throw new ApiServiceException("Taille du fichier superieure à la taille maximale");
		}
		// On recupère le redmine id
		Integer redmineId = pluiRequestEntity.getRedmineId();

		// On verifie que le redmineId est valable
		if (redmineId == null) {
			throw new ApiServiceException("Cette demande n'a pas encore été envoyée à redmine");
		}

		// On envoie la pièce jointe au redmine
		documentContent = redmineHelper.addAttachmentToIssue(redmineId, documentContent);

		Attachment result = new Attachment();
		result.setMimeType(documentContent.getContentType());
		result.setName(documentContent.getFileName());
		return result;
	}

	@Override
	@Transactional(readOnly = false)
	public PluiRequest updatePluiRequest(PluiRequest pluiRequest) throws ApiServiceException, RedmineException {
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

		// On s'assure que le type de la demande a bien ete renseigné
		if (pluiRequest.getType() == null) {
			throw new ApiServiceException(REQUEST_TYPE_NOT_FOUND);
		}

		// On defini le statut de  la demande si non defini ou different de nouveau
		if (pluiRequest.getStatus() == null || pluiRequest.getStatus() != PluiRequestStatus.NOUVEAU) {
			pluiRequest.setStatus(PluiRequestStatus.NOUVEAU);
		}

		// on met à nul les champs dont on ne veut pas recuperer les values
		pluiRequest.setCreationDate(null);
		pluiRequest.setInitiator(null);

		// On converti le dto en entité
		pluiRequestMapper.toEntity(pluiRequest, entityInDb);

		updatePositionAndArea(entityInDb, pluiRequest.getCodeInsee());

		// On ajoute un UUID à la demande
		entityInDb.setUuid(UUID.randomUUID());

		// On enregistre la demande dans la bdd après lui avoir ajouté le redmine id retourné de léa précédente opération
		try {
			pluiRequest = this.pluiRequestMapper.entityToDto(
					this.pluiRequestDao.save(
							entityInDb
					)
			);
			redmineHelper.updatePluiRequestIssue(entityInDb);
			return pluiRequest;
		} catch (DataAccessException e) {
			LOGGER.error("Erreur lors de la mise à jour de la demande dans la BDD");
			throw new ApiServiceException(e.getMessage(), e);
		}
	}

	@Override
	public List<Attachment> getAttachments(UUID uuid) throws ApiServiceException {
		if (uuid == null) {
			throw new ApiServiceException("UUID non fourni");
		}
		PluiRequestEntity pluiRequestEntity = pluiRequestDao.findByUuid(uuid);
		if (pluiRequestEntity == null) {
			throw new ApiServiceException("Entité non trouvée en base");
		}
		Issue redmineIssue = redmineHelper.getIssueByRedmineId(pluiRequestEntity.getRedmineId(), true);
		if (redmineIssue == null) {
			throw new ApiServiceException("La demande plui id = " + pluiRequestEntity.getId() + " n'existe pas dans Redmine.");
		}
		return redmineIssue.getAttachments().stream()
				.map(redmineAttachment -> {
					Attachment attachment = new Attachment();
					attachment.setId(redmineAttachment.getId().longValue());
					attachment.setName(redmineAttachment.getFileName());
					attachment.setMimeType(redmineAttachment.getContentType());
					return attachment;
				})
				.collect(Collectors.toList());
	}

	private void updatePositionAndArea(PluiRequestEntity pluiRequest, String codeInsee) throws ApiServiceException {
		// Si type communale alors position de la demande ajoutée depuis le front
		if (pluiRequest.getType() == PluiRequestType.COMMUNE && pluiRequest.getGeometry() == null) {
			throw new ApiServiceException(COMMUNAL_REQUEST_LOCALISATION_NOT_FOUND);
		}
		// Si type intercommunal, position de la demande a fixer a partir de la position de l'etablissement de l'organisation a laquelle appartient le user
		// Si type metropolitain, position de la demande a fixer à partir de la position de l'hotel rennes metropole
		// cas special des agents RM, code insee fourni pour demande metropolitaine et intercommunale, commune detecte si demande communale
		if (pluiRequest.getType() == PluiRequestType.INTERCOMMUNE || pluiRequest.getType() == PluiRequestType.METROPOLITAIN) {
			pluiRequest.setGeometry(getGeographicPosition(pluiRequest, codeInsee));
		}

		// On associe a la demande la geographicArea correspondante
		pluiRequest.setArea(getGeographicArea(pluiRequest, codeInsee));
	}

	/**
	 * Il s'agit positionner la demande sur le bon emplacement
	 * @param pluiRequest
	 * @param codeInsee
	 * @return
	 * @throws ApiServiceException
	 */
	private Geometry getGeographicPosition(PluiRequestEntity pluiRequest, String codeInsee) throws ApiServiceException {
		GeographicEtablissementEntity geographic = null;
		if (pluiRequest.getType() == PluiRequestType.INTERCOMMUNE) {
			// Si l'utilisateur est un agent Rennes Metropole
			if (geographicAreaService.getCurrentUserArea().getCodeInsee().equals(CODE_INSEE_RM)) {
				geographic = geographicEtablissementMapper.dtoToEntity(
						geographicEtablissementService.getGeographicEtablissementByCodeInsee(codeInsee)
				);
				if (geographic == null) {
					throw new ApiServiceException("Le code insee fourni n'a pas de geographic area correspondante");
				}
			} else {
				// on place la demande sur l'etablissement de l'organisation a laquelle appartient l'utilisateur
				geographic = geographicEtablissementMapper.dtoToEntity(
						geographicEtablissementService.getCurrentUserEtablissement());
			}
		} else if (pluiRequest.getType() == PluiRequestType.METROPOLITAIN) {
			// On place la demande sur hotel rennes metropole
			geographic = geographicEtablissementMapper.dtoToEntity(
					geographicEtablissementService.getGeographicEtablissementByCodeInsee(CODE_INSEE_RM));

		}

		if (geographic == null) {
			throw new ApiServiceException("Organisation inconnue", "404");
		}

		return geographic.getGeometry();
	}

	/**
	 * Permet de recuperer la geographic area a associer à une demande
	 * @param pluiRequest
	 * @return
	 */
	private GeographicAreaEntity getGeographicArea(PluiRequestEntity pluiRequest, String codeInsee) throws ApiServiceException {
		// On associe a la demande la geographicArea de l'utilisateur courant
		GeographicAreaEntity area = null;
		if (geographicAreaService.getCurrentUserArea().getCodeInsee().equals(CODE_INSEE_RM)) {
			// Si utilisateur est un agent rennes metropole
			if (pluiRequest.getType() == PluiRequestType.COMMUNE) {
				// Si demande de type communale, alors on localise la commune a laquelle appartient le point
				area = geographicAreaService.getGeographicAreaByPoint(pluiRequest.getGeometry());
			} else {
				// on lui attribue la geographic area associée au code insee fourni pour le type commune et intercommune
				area = geographicAreaMapper.dtoToEntity(geographicAreaService.getGeographicAreaByCodeInsee(codeInsee));
			}
		} else {
			// sinon si agent non RM, on lui associe la geographic area de son organisation
			area = geographicAreaMapper.dtoToEntity(geographicAreaService.getCurrentUserArea());
		}
		if (area == null) {
			throw new ApiServiceException("Area non trouvée pour cette demande");
		}
		return area;
	}

	@Override
	public AttachmentConfiguration getAttachmentConfiguration() {
		return attachmentHelper.getAttachmentConfiguration();
	}

	@Override
	@Transactional(readOnly = false)
	public PluiRequest createPluiRequest(PluiRequest pluiRequest) throws ApiServiceException, RedmineException {

		// On s'assure que le type de la demande a bien ete renseigné
		if (pluiRequest.getType() == null) {
			throw new ApiServiceException(REQUEST_TYPE_NOT_FOUND);
		}

		// On defini le statut de  la demande si non defini ou different de nouveau
		if (pluiRequest.getStatus() == null || pluiRequest.getStatus() != PluiRequestStatus.NOUVEAU) {
			pluiRequest.setStatus(PluiRequestStatus.NOUVEAU);
		}

		// On converti le dto en entité
		PluiRequestEntity pluiRequestEntity = pluiRequestMapper.dtoToEntity(pluiRequest);

		// On defini la date de creation de la demande
		pluiRequestEntity.setCreationDate(new Date());

		// On trouve la geograohic area associée à la demande et on l'ajoute à la demande
		String initiator = authentificationHelper.getUsername();

		// On ajoute l'initiateur de la demande
		pluiRequestEntity.setInitiator(initiator);

		updatePositionAndArea(pluiRequestEntity, pluiRequest.getCodeInsee());

		// On ajoute un UUID à la demande
		pluiRequestEntity.setUuid(UUID.randomUUID());

		// on envoie la pluirequest au redminehelper et on la recupere
		// Si le processus d'envoi réussi alors on recoit la pluirequest avec le redmineId renseigné
		// Sinon une exception est levée
		pluiRequestEntity = redmineHelper.createPluiRequestIssue(pluiRequestEntity);

		// On enregistre la demande dans la bdd après lui avoir ajouté le redmine id retourné de léa précédente opération
		try {
			return this.pluiRequestMapper.entityToDto(
					this.pluiRequestDao.save(
							pluiRequestEntity
					)
			);
		} catch (DataAccessException e) {
			// On supprime le ticket dans redmine si la demande n'a pu etre enregistrée ici dans la base de donnée
			redmineHelper.deleteIssueById(pluiRequestEntity.getRedmineId());
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
