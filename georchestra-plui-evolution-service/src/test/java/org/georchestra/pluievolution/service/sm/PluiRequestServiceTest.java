package org.georchestra.pluievolution.service.sm;

import org.georchestra.pluievolution.StarterSpringBootTestApplication;
import org.georchestra.pluievolution.core.dto.*;
import org.georchestra.pluievolution.service.exception.ApiServiceException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StarterSpringBootTestApplication.class)
@ComponentScan({ "org.georchestra.pluievolution.api", "org.georchestra.pluievolution.service",
        "org.georchestra.pluievolution.core" })
@TestPropertySource(value = { "classpath:plui-evolution.properties", "classpath:plui-evolution-common.properties" })
public class PluiRequestServiceTest {
    @Autowired
    PluiRequestService pluiRequestService;

    PluiRequest pluiRequest;

    PluiRequest pluiRequest1;

    @Before
    public void init() {
        pluiRequest = new PluiRequest();
        pluiRequest.setType(PluiRequestType.COMMUNE);
        pluiRequest.setObject("Ici l'objet de la demande");
        pluiRequest.setSubject("Ici le sujet de la demande");
        Point2D pt2D = new Point2D();
        pt2D.add(BigDecimal.valueOf(48.104594766322386));
        pt2D.add(BigDecimal.valueOf(-1.666802393510429));
        Point point = new Point();
        point.setCoordinates(pt2D);
        point.setType(GeometryType.POINT);
        pluiRequest.setLocalisation(point);
    }

    @Test
    public void createPluiRequest() throws ApiServiceException {
        pluiRequest1 = pluiRequestService.createPluiRequest(pluiRequest);
        Assert.assertNotNull("Le uuid ne doit être nul", pluiRequest1.getUuid());
        Assert.assertNotNull("La localisation ne doit etre nulle", pluiRequest1.getLocalisation());
        Assert.assertEquals("La localisation doit etre la meme que celle rensignee", pluiRequest.getLocalisation().hashCode(), pluiRequest1.getLocalisation().hashCode());
        Assert.assertNotNull("La date de creation ne doit etre nulle", pluiRequest1.getCreationDate());
        Assert.assertNotNull("Le type de demande doit etre renseigné", pluiRequest1.getType());
        Assert.assertNotNull("Le status de demande doit etre renseigné", pluiRequest1.getStatus());

        // Seulement si authentifié
        // Assert.assertNotNull("L'initiateur de la demande doit avoir ete renseigné", pluiRequest1.getInitiator());
        
        Assert.assertEquals("L'objet de la demande doit le meme que celui renseigné", pluiRequest.getObject(), pluiRequest1.getObject());
        Assert.assertEquals("Le sujet de la demade doit etre le meme que celui renseigné", pluiRequest.getSubject(), pluiRequest1.getSubject());

    }
}
