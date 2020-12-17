package org.georchestra.pluievolution.service.sm;

import com.taskadapter.redmineapi.RedmineException;
import org.georchestra.pluievolution.core.common.DocumentContent;
import org.georchestra.pluievolution.core.dto.Attachment;
import org.georchestra.pluievolution.core.dto.AttachmentConfiguration;
import org.georchestra.pluievolution.core.dto.PluiRequest;
import org.georchestra.pluievolution.service.exception.ApiServiceException;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

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

	/**
	 * Récupération des pièces jointes d'une demande plui
	 * @param uuid identifiant de la demande
	 * @return liste des pièces jointes
	 * @throws ApiServiceException erreur lors de la récupérationd des pièces jointes
	 */
    List<Attachment> getAttachments(UUID uuid) throws ApiServiceException;
}
