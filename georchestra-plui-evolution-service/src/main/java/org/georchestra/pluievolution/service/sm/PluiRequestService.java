/**
 * 
 */
package org.georchestra.pluievolution.service.sm;

import java.util.List;
import java.util.UUID;

import org.georchestra.pluievolution.core.common.DocumentContent;
import org.georchestra.pluievolution.core.dto.*;
import org.georchestra.pluievolution.service.exception.ApiServiceException;
import org.georchestra.pluievolution.service.exception.DocumentRepositoryException;

/**
 * @author FNI18300
 *
 */
public interface PluiRequestService {

	
	/**
	 * Ajoute un document sur un signalement
	 * 
	 * @param reportingUuid
	 * @param content
	 * @return
	 * @throws DocumentRepositoryException
	 */
	Attachment addAttachment(UUID reportingUuid, DocumentContent content) throws DocumentRepositoryException, ApiServiceException;

	/**
	 * Retourne la description d'un attachment pour un signalement et un id
	 * 
	 * @param reportingUuid
	 * @param attachmentId
	 * @return
	 * @throws DocumentRepositoryException
	 */
	Attachment getAttachment(UUID reportingUuid, Long attachmentId) throws DocumentRepositoryException;

	/**
	 * Retourne le document pour un signalement et un id
	 * 
	 * @param reportingUuid
	 * @param attachmentId
	 * @return
	 * @throws DocumentRepositoryException
	 */
	DocumentContent getAttachmentContent(UUID reportingUuid, Long attachmentId) throws DocumentRepositoryException;

	/**
	 * Supprime un document attaché à un signalement
	 * 
	 * @param reportingUuid
	 * @param attachmentId
	 * @throws DocumentRepositoryException
	 */
	void removeAttachment(UUID reportingUuid, Long attachmentId) throws DocumentRepositoryException;

	/**
	 * Retourne la liste des attachements d'un signalement
	 * 
	 * @param reportingUuid
	 * @return
	 */
	List<Attachment> getAttachments(UUID reportingUuid);

	/**
	 * Retourne la configuration associé à la gestion des documents
	 * 
	 * @return
	 */
	AttachmentConfiguration getAttachmentConfiguration();

	/**
	 * Crée une nouvelle demande
	 * @param pluiRequest
	 * @return
	 */
	PluiRequest createPluiRequest(PluiRequest pluiRequest) throws ApiServiceException;

	void deletePluiRequestByUuid(UUID uuid) throws ApiServiceException;

	/**
	 * Permet d'envoyer une pièce jointe à la demande de uuid @pluiRequestUuid directement sur Redmine
	 * @param pluiRequestUuid
	 * @param documentContent
	 * @return
	 * @throws ApiServiceException
	 */
	Attachment sendAttachment(UUID pluiRequestUuid, DocumentContent documentContent) throws ApiServiceException;

}
