package org.georchestra.pluievolution.service.helper.request;

import com.taskadapter.redmineapi.*;
import com.taskadapter.redmineapi.bean.*;
import com.taskadapter.redmineapi.internal.Transport;
import org.georchestra.pluievolution.core.dto.PluiRequestStatus;
import org.georchestra.pluievolution.core.dto.PluiRequestType;
import org.georchestra.pluievolution.core.entity.request.PluiRequestEntity;
import org.georchestra.pluievolution.service.exception.ApiServiceException;
import org.georchestra.pluievolution.service.helper.authentification.AuthentificationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.text.Normalizer;
import java.util.List;


@Component
public class RedmineHelper {

    @Value("${redmine.uri}")
    String uri;

    @Value("${redmine.api.access.key}")
    String apiAccessKey;

    @Value("${redmine.project.key}")
    String projectKey;

    private static String CUSTOM_COLUMN_COMMENTAIRE = "commentaire"; // inutile
    private static String CUSTOM_COLUMN_INITIATEUR = "initiateur";
    private static String CUSTOM_COLUMN_TYPE = "type"; // inutile

    @Autowired
    AuthentificationHelper authentificationHelper;

    /**
     * Permet de créer un ticket concernat la pluirequest dans redmine
     * @param pluiRequest
     * @return
     * @throws RedmineException
     * @throws ApiServiceException
     */
    public PluiRequestEntity createPluiRequestIssue(PluiRequestEntity pluiRequest) throws RedmineException, ApiServiceException {
        Project projectByKey = null;
        // On recupere le projet ou le sous projet vers lequel on veut envoyer la demande
        try {
            String key = transformToIdentifiantFormat(pluiRequest.getArea().getNom());
            projectByKey = getProjectManager().getProjectByKey(key);
        } catch (RedmineException e) {
            throw new RedmineException(e.getMessage());
        }

        // Creation de l'issue
        Issue issue = new Issue(getTransport(), projectByKey.getId())
                .setSubject(pluiRequest.getSubject())
                .setCreatedOn(pluiRequest.getCreationDate())
                .setStatusName(pluiRequest.getStatus().toString())
                .setDescription(pluiRequest.getObject());
        // Ajout des valeurs des champs customs

        // Ajout du type de demande
        CustomFieldDefinition cfd = getCustomFieldByName(CUSTOM_COLUMN_TYPE);
        if ( cfd != null ) {
            issue.addCustomField(CustomFieldFactory.create(cfd.getId(), cfd.getName(), pluiRequest.getType().toString()));
        }

        // Ajout de l'initiateur de la demande
        cfd = getCustomFieldByName(CUSTOM_COLUMN_INITIATEUR);
        if ( cfd != null ) {
            issue.addCustomField(CustomFieldFactory.create(cfd.getId(), cfd.getName(), pluiRequest.getInitiator()));
        }

        // Creation de l'issue dans redmine
        Issue sent = issue.create();

        // recuperation de l'id du ticket créé dans redmine
        if (sent != null) {
            pluiRequest.setRedmineId(sent.getId().toString());
        }

        return pluiRequest;
    }

    /**
     * Permet de synchroniser une demande avec les infos contenue dans son ticket redmine
     * @param pluiRequest
     * @return
     * @throws RedmineException
     * @throws ApiServiceException
     */
    public PluiRequestEntity updatePluiRequestFromRedmine(PluiRequestEntity pluiRequest) throws RedmineException, ApiServiceException {
        if (pluiRequest.getRedmineId() == null) {
            throw new ApiServiceException("Aucun ticket correspondant dans redmine");
        }
        Issue issue = getIssueByRedmineId(Integer.valueOf(pluiRequest.getRedmineId()));
        pluiRequest.setSubject(issue.getSubject());
        pluiRequest.setObject(issue.getDescription());
        pluiRequest.setStatus(PluiRequestStatus.fromValue(issue.getStatusName()));
        pluiRequest.setCreationDate(issue.getCreatedOn());
        pluiRequest.setType(PluiRequestType.fromValue(issue.getCustomFieldByName(CUSTOM_COLUMN_TYPE).getValue()));
        pluiRequest.setInitiator(issue.getCustomFieldByName(CUSTOM_COLUMN_INITIATEUR).getValue());
        pluiRequest.setComment(issue.getCustomFieldByName(CUSTOM_COLUMN_COMMENTAIRE).getValue());

        return pluiRequest;
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

    /**
     * Transforme le nom de la geographic area en un format compatible avec les identifiants a-z-_
     * @param nom
     * @return
     */
    private String transformToIdentifiantFormat(String nom) {
        // On remplace tous les caracteres avec accents par leurs equivalent en caracteres ascii
        String identifiant = Normalizer
                .normalize(nom, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
        // On remplace espaces et apostrophes par des -
        identifiant = identifiant.replace('\'', '-');
        identifiant = identifiant.replace(' ', '-');
        return identifiant.toLowerCase(); // on la lowercase de chaine de caracteres
    }



}
