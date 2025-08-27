package org.georchestra.pluievolution.service.helper.request;

import static org.georchestra.pluievolution.service.common.constant.CommuneParams.CODE_INSEE_RM;
import static org.georchestra.pluievolution.service.common.constant.CommuneParams.FICTIVE_INTERCO_AREA_NAME;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.taskadapter.redmineapi.Params;
import com.taskadapter.redmineapi.internal.ResultsWrapper;
import org.apache.commons.collections4.CollectionUtils;
import org.georchestra.pluievolution.core.common.DocumentContent;
import org.georchestra.pluievolution.core.dao.config.ConfigurationDao;
import org.georchestra.pluievolution.core.dao.request.PluiRequestDao;
import org.georchestra.pluievolution.core.dto.PluiRequestType;
import org.georchestra.pluievolution.core.entity.configuration.ConfigurationEntity;
import org.georchestra.pluievolution.core.entity.request.PluiRequestEntity;
import org.georchestra.pluievolution.service.acl.GeographicAreaService;
import org.georchestra.pluievolution.service.exception.ApiServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.taskadapter.redmineapi.AttachmentManager;
import com.taskadapter.redmineapi.Include;
import com.taskadapter.redmineapi.ProjectManager;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.bean.Attachment;
import com.taskadapter.redmineapi.bean.CustomFieldDefinition;
import com.taskadapter.redmineapi.bean.CustomFieldFactory;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.Project;
import com.taskadapter.redmineapi.bean.Tracker;
import com.taskadapter.redmineapi.internal.Transport;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class RedmineHelper {

	@Value("${redmine.uri}")
	private String uri;

	@Value("${redmine.api.access.key}")
	private String apiAccessKey;

	@Value("${redmine.custom.column.initiateur}")
	private String customColumnInitiateur;

	@Value("${redmine.tracker.communal}")
	private String trackerCommunal;

	@Value("${redmine.tracker.intercommunal}")
	private String trackerIntercommunal;

	@Value("${redmine.tracker.metropolitain}")
	private String trackerMetropolitain;

	@Value("${redmine.issue.priority:}")
	private Integer priority;

	@Value("${redmine.conf.last-synchro-code:REDMINE_LAST_SYNCHRO}")
	private String redmineConfLastSynchroCode;

	@Value("${redmine.conf.concertation.id:9}")
	private Integer concertationId;

	@Value("${redmine.conf.approbation.id:87}")
	private Integer approbationId;

	private final GeographicAreaService geographicAreaService;

	private final ConfigurationDao configurationDao;

	private final PluiRequestDao pluiRequestDao;

	/**
	 * Permet de créer un ticket concernant la pluirequest dans redmine
	 * 
	 * @param pluiRequest
	 * @return
	 * @throws RedmineException
	 * @throws ApiServiceException
	 */
	public PluiRequestEntity createPluiRequestIssue(PluiRequestEntity pluiRequest)
			throws RedmineException, ApiServiceException {
		Project sousProjet = null;
		Tracker tracker = null;
		log.info("createPluiRequestIssue {}", pluiRequest);
		// On recupere le projet ou le sous projet vers lequel on veut envoyer la
		// demande
		try {
			String identifiant = null;
			if (pluiRequest.getType() == PluiRequestType.COMMUNE) {

				// si type commune alors sous projet redmine de la commune
				identifiant = pluiRequest.getArea().getIdentifiantRedmine();

				// mettre le tracker demande communale
				tracker = getTracker(trackerCommunal);
			} else if (pluiRequest.getType() == PluiRequestType.INTERCOMMUNE) {
				// mettre le tracker demande intercommunale
				tracker = getTracker(trackerIntercommunal);
				// si type intercommune sur Rennes Metropole alors sous projet interco
				if (pluiRequest.getArea().getCodeInsee().equalsIgnoreCase(CODE_INSEE_RM)) {
					identifiant = geographicAreaService.getGeographicAreaByNom(FICTIVE_INTERCO_AREA_NAME)
							.getIdentifiantRedmine();
				} else {
					// si type intercommune sur Commune X alors sous projet de la commune X
					identifiant = pluiRequest.getArea().getIdentifiantRedmine();
				}
			} else if (pluiRequest.getType() == PluiRequestType.METROPOLITAIN) {
				// mettre le tracker demande metropolitaine
				tracker = getTracker(trackerMetropolitain);
				// si type metropolitain alors sous projet redmine pour les demandes
				// metropolitaines
				identifiant = pluiRequest.getArea().getIdentifiantRedmine();
			}

			sousProjet = getProjectManager().getProjectByKey(identifiant);
		} catch (RedmineException e) {
			throw new RedmineException(e.getMessage(), e);
		}

		// Creation de l'issue
		Issue issue = new Issue(getTransport(), sousProjet.getId()).setSubject(pluiRequest.getSubject())
				.setCreatedOn(pluiRequest.getCreationDate()).setTracker(tracker)
				.setDescription(pluiRequest.getObject());
		if (priority != null) {
			issue.setPriorityId(priority);
		}

		// Ajout des valeurs des champs customs
		// Ajout de l'initiateur de la demande
		CustomFieldDefinition cfd = getCustomFieldByName(customColumnInitiateur);
		if (cfd != null) {
			issue.addCustomField(CustomFieldFactory.create(cfd.getId(), cfd.getName(), pluiRequest.getInitiator()));
		}

		// Creation de l'issue dans redmine
		try {
			Issue sent = issue.create();
			// recuperation de l'id du ticket créé dans redmine
			pluiRequest.setRedmineId(sent.getId());
			return pluiRequest;
		} catch (RedmineException e) {
			throw new ApiServiceException(e.getMessage(), e);
		}
	}

	/**
	 * Permet de recuperer une issue redmine à partir de son id
	 * 
	 * @param redmineId
	 * @return
	 * @throws RedmineException
	 * @throws ApiServiceException
	 */
	private Issue getIssueByRedmineId(Integer redmineId) throws ApiServiceException {
		try {
			return getRedmineManager().getIssueManager().getIssueById(redmineId);
		} catch (RedmineException e) {
			throw new ApiServiceException(e.getMessage(), e);
		}

	}

	/**
	 * Permet de recuperer une issue redmine à partir de son id
	 * 
	 * @param redmineId      identifiant redmine
	 * @param withAttachment true si avec les fichiers joints
	 * @return issue redmine
	 * @throws RedmineException    erreur lors de la récupération de l'issue
	 * @throws ApiServiceException erreur lors de la récupération de l'issue
	 */
	public Issue getIssueByRedmineId(Integer redmineId, boolean withAttachment) throws ApiServiceException {
		try {
			if (!withAttachment) {
				return getIssueByRedmineId(redmineId);
			} else {
				return getRedmineManager().getIssueManager().getIssueById(redmineId, Include.attachments);
			}
		} catch (RedmineException e) {
			throw new ApiServiceException(e.getMessage(), e);
		}

	}

	private Tracker getTracker(String nomDuTracker) throws ApiServiceException {
		Tracker tracker = null;
		try {
			List<Tracker> trackers = getRedmineManager().getIssueManager().getTrackers();
			for (Tracker t : trackers) {
				if (t.getName().equalsIgnoreCase(nomDuTracker)) {
					tracker = t;
					break;
				}
			}
		} catch (RedmineException re) {
			throw new ApiServiceException("Erreur lors de la recuperation du tracker", re);
		}
		return tracker;
	}

	/**
	 * Permet de supprimer un ticket dans redmine
	 * 
	 * @param id
	 * @return
	 */
	public void deleteIssueById(Integer id) throws RedmineException, ApiServiceException {
		Issue issue = getIssueByRedmineId(id);
		try {
			issue.delete();
		} catch (RedmineException e) {
			throw new ApiServiceException(e.getMessage(), e);
		} catch (Exception e) {
			// Ignore
		}
	}

	/**
	 * Téléchargement du contenu d'un pièce jointe
	 * 
	 * @param attachment pièce jointe
	 * @return contenu
	 * @throws ApiServiceException erreur lors de la récupération du contenu de la
	 *                             pièce jointe
	 */
	public byte[] downloadAttachment(Attachment attachment) throws ApiServiceException {
		try {
			return getAttachmentManager().downloadAttachmentContent(attachment);
		} catch (RedmineException e) {
			throw new ApiServiceException(e.getMessage(), e);
		}
	}

	/**
	 * Récupération d'un pièce jointe redmine via son id
	 * 
	 * @param attachmentId identifiant redmine de la pièce jointe
	 * @return pièce jointe redmine
	 * @throws ApiServiceException erreur lors de la récupération de la pièce jointe
	 */
	public Attachment getAttachmentById(Integer attachmentId) throws ApiServiceException {
		try {
			return getAttachmentManager().getAttachmentById(attachmentId);
		} catch (RedmineException e) {
			throw new ApiServiceException(e.getMessage(), e);
		}
	}

	/**
	 * Permet de trouver une customfield definition par son nom
	 * 
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
		for (CustomFieldDefinition cfd : customFieldDefinitions) {
			if (cfd.getName().equals(name)) {
				return cfd;
			}
		}
		return null;
	}

	/**
	 * Obtenir l'obtenir de transport vers le redmine
	 * 
	 * @return
	 */
	private Transport getTransport() {
		RedmineManager mgr = getRedmineManager();
		return mgr.getTransport();
	}

	/**
	 * Obtenir le redmine manager
	 * 
	 * @return
	 */
    public RedmineManager getRedmineManager() {
		return RedmineManagerFactory.createWithApiKey(uri, apiAccessKey);
	}

	/**
	 * Obtenir le project manager
	 * 
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

	public DocumentContent addAttachmentToIssue(Integer issueId, DocumentContent documentContent)
			throws RedmineException, ApiServiceException, IOException {
		Attachment attachment = null;
		Issue issue = getIssueByRedmineId(issueId);
		try {
			// on crée la pièce jointe dans redmine
			attachment = getAttachmentManager().uploadAttachment(documentContent.getFileName(),
					documentContent.getContentType(), documentContent.getFileStream());
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
			// ignore
			// il demande forcement qu'il y'ai une reponse donc erreur sur les methodes
			// renvoyant void; meme si l'operation se deroule bien sur le redmine
			// du coup nous allons ignorer les exceptions autres que RedmineException et
			// IOException
		}

		return documentContent;
	}

	/**
	 * Permet de mettre à jour une demande
	 * 
	 * @param pluiRequest demande plui
	 * @throws ApiServiceException erreur lors la mise à jour
	 */
	public void updatePluiRequestIssue(PluiRequestEntity pluiRequest) throws ApiServiceException {
		if (pluiRequest.getRedmineId() == null) {
			throw new ApiServiceException("issue id might not be null");
		}
		Issue issue = getIssueByRedmineId(pluiRequest.getRedmineId());
		issue.setSubject(pluiRequest.getSubject()).setCreatedOn(pluiRequest.getCreationDate())
				.setDescription(pluiRequest.getObject());
		try {
			issue.update();
		} catch (RedmineException e) {
			throw new ApiServiceException(e.getMessage(), e);
		}
	}

	@Scheduled(fixedRateString = "${redmine.synchro.rate:300}", timeUnit = TimeUnit.SECONDS)
	@Transactional(readOnly = false)
	public void updatePluiRequestsFromRedmine() throws ApiServiceException {
		log.info("[SCHEDULED] Start synchro Redmine");
		ConfigurationEntity lastSynchro = configurationDao.findByCode(redmineConfLastSynchroCode);
		if (lastSynchro == null) {
			lastSynchro = new ConfigurationEntity();
			lastSynchro.setCode(redmineConfLastSynchroCode);
			lastSynchro.setValeur(null);
		}

		Params params = new Params();
		if (lastSynchro.getValeur() != null) {
			params.add("op[updated_on]", ">=");
			params.add("v[updated_on]", lastSynchro.getValeur());
		}
        try {
			ResultsWrapper<Issue> issues = getRedmineManager().getIssueManager().getIssues(params);
			processIssues(issues.getResults());
			lastSynchro.setValeur(LocalDate.now().toString());
			configurationDao.save(lastSynchro);
        } catch (RedmineException e) {
            throw new ApiServiceException(e.getMessage(), e);
        } finally {
			log.info("[SCHEDULED] End synchro Redmine");
		}
    }

	private void processIssues(List<Issue> results) {
		int count = 0;
		if (CollectionUtils.isNotEmpty(results)) {
			for (Issue issue : results) {
				if (processIssue(issue)) {
					count++;
				}
			}
			log.info("Found {} Redmine issues, {} PLUIRequests were synchronized", results.size() ,count);
		} else {
			log.warn("No issues were found");
		}
	}

	private boolean processIssue(Issue issue) {
		PluiRequestEntity pluiRequest = pluiRequestDao.findByRedmineId(issue.getId());
		if (pluiRequest != null) {
			pluiRequest.setConcertation(issue.getCustomFieldById(concertationId) != null
					? issue.getCustomFieldById(concertationId).getValue() : null);
			pluiRequest.setApprobation(issue.getCustomFieldById(approbationId) != null
					? issue.getCustomFieldById(approbationId).getValue() : null);
			pluiRequestDao.save(pluiRequest);
			return true;
		}
		return false;
	}
}
