package org.georchestra.pluievolution.service.helper.request;

import com.taskadapter.redmineapi.*;
import com.taskadapter.redmineapi.bean.*;
import com.taskadapter.redmineapi.internal.Transport;
import org.georchestra.pluievolution.core.common.DocumentContent;
import org.georchestra.pluievolution.core.dto.PluiRequestType;
import org.georchestra.pluievolution.core.entity.request.PluiRequestEntity;
import org.georchestra.pluievolution.service.acl.GeographicAreaService;
import org.georchestra.pluievolution.service.acl.GeographicEtablissementService;
import org.georchestra.pluievolution.service.exception.ApiServiceException;
import org.georchestra.pluievolution.service.helper.authentification.AuthentificationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;


@Component
public class RedmineHelper {

    @Value("${redmine.uri}")
    private String uri;

    @Value("${redmine.api.access.key}")
    private String apiAccessKey;


    private static final String CUSTOM_COLUMN_INITIATEUR = "initiateur";
    private static final String FICTIVE_INTERCO_AREA_NAME = "Interco";
    private static final String CODE_INSEE_RM = "243500139";

    @Autowired
    AuthentificationHelper authentificationHelper;

    @Autowired
    GeographicEtablissementService geographicEtablissementService;

    @Autowired
    GeographicAreaService geographicAreaService;

    /**
     * Permet de créer un ticket concernat la pluirequest dans redmine
     * @param pluiRequest
     * @return
     * @throws RedmineException
     * @throws ApiServiceException
     */
    public PluiRequestEntity createPluiRequestIssue(PluiRequestEntity pluiRequest) throws RedmineException, ApiServiceException {
        Project sousProjet = null;
        // On recupere le projet ou le sous projet vers lequel on veut envoyer la demande
        try {
            String identifiant = null;
            if (pluiRequest.getType() == PluiRequestType.COMMUNE) {
                // si type commune alors sous projet remine de la commune
                identifiant = pluiRequest.getArea().getIdentifiantRedmine();
            } else if (pluiRequest.getType() == PluiRequestType.INTERCOMMUNE) {
                // si type intercommune, sous projet redmine pour les demandes intercommunes
                identifiant = geographicAreaService.getGeographicAreaByNom(FICTIVE_INTERCO_AREA_NAME).getIdentifiantRedmine();
            } else if (pluiRequest.getType() == PluiRequestType.METROPOLITAIN) {
                // si type metropolitain alors sous projet redmine pour les demandes metropolitaines
                identifiant = geographicAreaService.getGeographicAreaByCodeInsee(CODE_INSEE_RM).getIdentifiantRedmine();
            }

            sousProjet = getProjectManager().getProjectByKey(identifiant);
        } catch (RedmineException e) {
            throw new RedmineException(e.getMessage());
        }

        // Creation de l'issue
        Issue issue = new Issue(getTransport(), sousProjet.getId())
                .setSubject(pluiRequest.getSubject())
                .setCreatedOn(pluiRequest.getCreationDate())
                .setDescription(pluiRequest.getObject());

        // Ajout des valeurs des champs customs
        // Ajout de l'initiateur de la demande
        CustomFieldDefinition cfd = getCustomFieldByName(CUSTOM_COLUMN_INITIATEUR);
        if ( cfd != null ) {
            issue.addCustomField(CustomFieldFactory.create(cfd.getId(), cfd.getName(), pluiRequest.getInitiator()));
        }

        // Creation de l'issue dans redmine
        try {
            Issue sent = issue.create();
            // recuperation de l'id du ticket créé dans redmine
            pluiRequest.setRedmineId(sent.getId());
            return pluiRequest;
        } catch (RedmineException e) {
            throw new ApiServiceException(e.getMessage());
        }
    }


    /**
     * Permet de recuperer une issue redmine à partir de son id
     * @param redmineId
     * @return
     * @throws RedmineException
     * @throws ApiServiceException
     */
    private Issue getIssueByRedmineId(Integer redmineId) throws RedmineException, ApiServiceException {
        try {
            return getRedmineManager().getIssueManager().getIssueById(redmineId);
        } catch (RedmineException e) {
            throw new ApiServiceException(e.getMessage());
        }

    }

    /**
     * Permet de supprimer un ticket dans redmine
     * @param id
     * @return
     */
    public void deleteIssueById(Integer id) throws RedmineException, ApiServiceException {
        Issue issue = getIssueByRedmineId(id);
        try {
            issue.delete();
        } catch (RedmineException e) {
            throw new ApiServiceException(e.getMessage(), e);
        }
    }

    /**
     * Permet de trouver une customfield definition par son nom
     * @param name
     * @return
     * @throws RedmineException
     * @throws ApiServiceException
     */
    private CustomFieldDefinition getCustomFieldByName(String name) throws RedmineException, ApiServiceException {
        List<CustomFieldDefinition> customFieldDefinitions = null;
        try {
            customFieldDefinitions = getRedmineManager().getCustomFieldManager().getCustomFieldDefinitions();
        } catch (RedmineException e) {
            throw new ApiServiceException(e.getMessage());
        }
        for (CustomFieldDefinition cfd: customFieldDefinitions) {
            if (cfd.getName().equals(name)) {
                return cfd;
            }
        }
        return null;
    }

    /**
     * Obtenir l'obtenir de transport vers le redmine
     * @return
     */
    private Transport getTransport() {
        RedmineManager mgr = getRedmineManager();
        return mgr.getTransport();
    }

    /**
     * Obtenir le redmine manager
     * @return
     */
    private RedmineManager getRedmineManager() {
        return RedmineManagerFactory.createWithApiKey(uri, apiAccessKey);
    }

    /**
     * Obtenir le project manager
     * @return
     */
    private ProjectManager getProjectManager() {
        RedmineManager mgr = getRedmineManager();
        return mgr.getProjectManager();
    }

    private AttachmentManager getAttachmentManager() {
        RedmineManager mgr = getRedmineManager();
        return mgr.getAttachmentManager();
    }

    public DocumentContent addAttachmentToIssue(Integer issueId, DocumentContent documentContent) throws RedmineException, ApiServiceException, IOException {
        Attachment attachment = null;
        Issue issue = getIssueByRedmineId(issueId);
        try {
            // on crée la pièce jointe dans redmine
            attachment = getAttachmentManager().uploadAttachment(
                    documentContent.getFileName(),
                    documentContent.getContentType(),
                    documentContent.getFileStream()
            );
            documentContent.setContentType(attachment.getContentType());
            documentContent.setUrl(attachment.getContentURL());
            documentContent.setFileName(attachment.getFileName());
        } catch (IOException | RedmineException e) {
            throw new ApiServiceException(e.getMessage(), e);
        }

        try {
            // on associe la piece jointe a la demande souhaitee
            issue.addAttachment(attachment);
            issue.update();
        } catch (RedmineException e) {
            throw new ApiServiceException(e.getMessage(), e);
        } catch (Exception e) {
            //ignore
            //il demande forcement qu'il y'ai une reponse donc erreur sur les methodes renvoyant void; meme si l'operation se deroule bien sur le redmine
            // du coup nous allons ignorer les exceptions autres que RedmineException et IOException
        }

        return documentContent;
    }
}
