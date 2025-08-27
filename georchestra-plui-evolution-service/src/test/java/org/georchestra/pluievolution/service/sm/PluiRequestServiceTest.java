package org.georchestra.pluievolution.service.sm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.taskadapter.redmineapi.IssueManager;
import com.taskadapter.redmineapi.Params;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.bean.CustomField;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.internal.ResultsWrapper;
import org.georchestra.pluievolution.StarterSpringBootTestApplication;
import org.georchestra.pluievolution.core.dao.request.PluiRequestDao;
import org.georchestra.pluievolution.core.dto.GeometryType;
import org.georchestra.pluievolution.core.dto.PluiRequest;
import org.georchestra.pluievolution.core.dto.PluiRequestStatus;
import org.georchestra.pluievolution.core.dto.PluiRequestType;
import org.georchestra.pluievolution.core.dto.Point;
import org.georchestra.pluievolution.core.entity.request.PluiRequestEntity;
import org.georchestra.pluievolution.service.exception.ApiServiceException;
import org.georchestra.pluievolution.service.helper.request.RedmineHelper;
import org.georchestra.pluievolution.service.mapper.PluiRequestMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = StarterSpringBootTestApplication.class)
@ComponentScan({ "org.georchestra.pluievolution.api", "org.georchestra.pluievolution.service",
		"org.georchestra.pluievolution.core" })
@EntityScan({ "org.georchestra.pluievolution.core.entity" })
@TestPropertySource(value = { "classpath:plui-evolution.properties", "classpath:plui-evolution-common.properties" })
class PluiRequestServiceTest {
	@Autowired
	PluiRequestService pluiRequestService;

	@Autowired
	PluiRequestMapper pluiRequestMapper;

	@Autowired
	PluiRequestDao pluiRequestDao;

	@Autowired
	@SpyBean
	RedmineHelper redmineHelper;

	PluiRequest pluiRequest;

	PluiRequest pluiRequest1;

	@MockBean
	RedmineManager redmineManager;

	@BeforeEach
	void init() {
		pluiRequest = new PluiRequest();
		pluiRequest.setType(PluiRequestType.COMMUNE);
		pluiRequest.setObject("Ici l'objet de la demande");
		pluiRequest.setSubject("Ici le sujet de la demande");
		pluiRequest.setRedmineId(2);
		List<BigDecimal> pt2D = new ArrayList<>();
		pt2D.add(BigDecimal.valueOf(48.104594766322386));
		pt2D.add(BigDecimal.valueOf(-1.666802393510429));
		Point point = new Point();
		point.setCoordinates(pt2D);
		point.setType(GeometryType.POINT);
		pluiRequest.setLocalisation(point);
		pluiRequest.setCreationDate(new Date());
		pluiRequest.setUuid(UUID.randomUUID());
		pluiRequest.setStatus(PluiRequestStatus.NOUVEAU);
	}

	@Test
	void mappingPluiRequest() throws ApiServiceException {
		PluiRequestEntity pluiRequestEntity = pluiRequestMapper.dtoToEntity(pluiRequest);
		assertNull(pluiRequestEntity.getCreationDate(), "La date de creation doit etre nulle");
		pluiRequestEntity.setCreationDate(new Date());
		pluiRequest1 = pluiRequestMapper.entityToDto(pluiRequestEntity);
		assertNotNull(pluiRequest1.getUuid(), "Le uuid ne doit être nul");
		assertNotNull(pluiRequest1.getLocalisation(), "La localisation ne doit etre nulle");
		assertEquals(pluiRequest.getLocalisation().hashCode(), pluiRequest1.getLocalisation().hashCode(),
				"La localisation doit etre la meme que celle rensignee");
		assertNotNull(pluiRequest1.getCreationDate(), "La date de creation ne doit etre nulle");
		assertNotNull(pluiRequest1.getType(), "Le type de demande doit etre renseigné");
		assertNotNull(pluiRequest1.getStatus(), "Le status de demande doit etre renseigné");

		assertEquals(pluiRequest.getObject(), pluiRequest1.getObject(),
				"L'objet de la demande doit le meme que celui renseigné");
		assertEquals(pluiRequest.getSubject(), pluiRequest1.getSubject(),
				"Le sujet de la demade doit etre le meme que celui renseigné");

	}

	@Test
	void synchroRedmine() throws ApiServiceException, RedmineException {
		initRedmine();
		PluiRequestEntity entity = pluiRequestMapper.dtoToEntity(pluiRequest);
		entity.setCreationDate(pluiRequest.getCreationDate());
        entity = pluiRequestDao.save(entity);
        assertNull(entity.getConcertation());
		assertNull(entity.getApprobation());
		redmineHelper.updatePluiRequestsFromRedmine();
		entity = pluiRequestDao.findByUuid(entity.getUuid());
		assertEquals("Une concertation", entity.getConcertation());
		assertEquals("C'est approuvé", entity.getApprobation());
	}

	private void initRedmine() throws RedmineException {
		List<Issue> issues = new ArrayList<>();
		Issue issueNonSuivie = new Issue();
		issueNonSuivie.setId(1);
		CustomField cf = new CustomField();
		cf.setId(9);
		cf.setValue("blabla");
		issueNonSuivie.addCustomField(cf);
		issues.add(issueNonSuivie);
		Issue issueSuivie = new Issue();
		issueSuivie.setId(2);
		CustomField concertationField = new CustomField();
		concertationField.setId(9);
		concertationField.setValue("Une concertation");
		issueSuivie.addCustomField(concertationField);
		CustomField approbationField = new CustomField();
		approbationField.setId(87);
		approbationField.setValue("C'est approuvé");
		issueSuivie.addCustomField(approbationField);
		issues.add(issueSuivie);
		ResultsWrapper<Issue> resultsWrapper = new ResultsWrapper<>(2, 2, 2, issues);
		Mockito.when(redmineHelper.getRedmineManager()).thenReturn(redmineManager);
		Mockito.when(redmineManager.getIssueManager()).thenReturn(Mockito.mock(IssueManager.class));
		Mockito.when(redmineManager.getIssueManager().getIssues(Mockito.any(Params.class))).thenReturn(resultsWrapper);
	}
}
