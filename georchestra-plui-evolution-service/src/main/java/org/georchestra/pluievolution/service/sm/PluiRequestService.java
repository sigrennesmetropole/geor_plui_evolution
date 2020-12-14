/**
 * 
 */
package org.georchestra.pluievolution.service.sm;

import java.io.IOException;
import java.util.UUID;

import com.taskadapter.redmineapi.RedmineException;
import org.georchestra.pluievolution.core.common.DocumentContent;
import org.georchestra.pluievolution.core.dto.*;
import org.georchestra.pluievolution.service.exception.ApiServiceException;

/**
 * @author FNI18300
 *
 */
public interface PluiRequestService {

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
	PluiRequest createPluiRequest(PluiRequest pluiRequest) throws ApiServiceException, RedmineException;

	void deletePluiRequestByUuid(UUID uuid) throws ApiServiceException;

	/**
	 * Permet d'envoyer une pièce jointe à la demande de uuid @pluiRequestUuid directement sur Redmine
	 * @param pluiRequestUuid
	 * @param documentContent
	 * @return
	 * @throws ApiServiceException
	 */
	Attachment sendAttachment(UUID pluiRequestUuid, DocumentContent documentContent) throws ApiServiceException, IOException, RedmineException;

	/**
	 * Permet de mettre à jour une pluiRequest
	 * @param pluiRequest
	 * @return
	 * @throws ApiServiceException
	 */
	PluiRequest updatePluiRequest(PluiRequest pluiRequest) throws ApiServiceException, RedmineException;

}
