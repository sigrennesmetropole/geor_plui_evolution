package org.georchestra.pluievolution.service.sm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.apache.http.client.methods.HttpGet;
import org.georchestra.pluievolution.StarterSpringBootTestApplication;
import org.georchestra.pluievolution.core.dto.GeographicArea;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.util.factory.GeoTools;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = StarterSpringBootTestApplication.class)
@ComponentScan({ "org.georchestra.pluievolution.api", "org.georchestra.pluievolution.service",
        "org.georchestra.pluievolution.core" })
@TestPropertySource(value = { "classpath:plui-evolution.properties", "classpath:plui-evolution-common.properties" })
class GeoserverServiceTest {

    @Autowired
    public GeoserverService geoserverService;
    @Test
    void testFunctionCql() throws CQLException {
        String filter1 = "strToLowerCase(object) LIKE '*test*'";
        Filter ecqlFilter1 = ECQL.toFilter(filter1);
        assertNotNull(ecqlFilter1);
        String decodedFilter1 = ECQL.toCQL(ecqlFilter1);
        assertNotNull(decodedFilter1);
        assertEquals(filter1, decodedFilter1);

        String filter2 = "codeinsee = '35238'";
        Filter ecqlFilter2 = ECQL.toFilter(filter2);
        assertNotNull(ecqlFilter2);
        String decodedFilter2 = ECQL.toCQL(ecqlFilter2);
        assertNotNull(decodedFilter2);
        assertEquals(filter2, decodedFilter2);

        FilterFactory ff = CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints());

        Filter ecqlFilter = ff.and(ecqlFilter1, ecqlFilter2);
        assertNotNull(ecqlFilter);

        String filter = ECQL.toCQL(ecqlFilter);
        assertEquals(filter1 + " AND " + filter2, filter);


    }

    @Test
    void test_buildGeoserserGet_RM() {
        String queryParams = "SERVICE=WMS&VERSION=1.3.0&REQUEST=GetMap&FORMAT=image/png&TRANSPARENT=true&LAYERS=app:plui_evolution_detailed_plui_request&STYLES=&SRS=EPSG:3857&CRS=EPSG:3857&TILED=true&_v_=1705567922551&exceptions=application/vnd.ogc.se_xml&CQL_FILTER=(%22initiator%22=%27Pierre%20GARNIER%27)&WIDTH=256&HEIGHT=256&BBOX=-185894.8527895473,6124746.202434603,-181002.882979296,6129638.172244854";
        GeographicArea area = new GeographicArea();
        area.setCodeInsee("243500139");
        area.setNom("Rennes Métropole");
        area.setId(1L);
        String baseUrl = "http://localhost:8095/geoserver/pluievolution";
        try {
            HttpGet httpGet = geoserverService.buildGeoserverHttpGet(baseUrl,
                    area, queryParams , MediaType.APPLICATION_JSON_VALUE, "UTF-8");
            assertNotNull(httpGet);
        } catch (Exception e) {
            Assertions.fail("echec de la requete");
        }

    }


    @Test
    void test_buildGeoserserGet_Rennes() {
        String queryParams = "SERVICE=WMS&VERSION=1.3.0&REQUEST=GetMap&FORMAT=image/png&TRANSPARENT=true&LAYERS=app:plui_evolution_detailed_plui_request&STYLES=&SRS=EPSG:3857&CRS=EPSG:3857&TILED=true&_v_=1705567922551&exceptions=application/vnd.ogc.se_xml&CQL_FILTER=(%22initiator%22=%27Pierre%20GARNIER%27)&WIDTH=256&HEIGHT=256&BBOX=-185894.8527895473,6124746.202434603,-181002.882979296,6129638.172244854";
        GeographicArea area = new GeographicArea();
        area.setCodeInsee("35238");
        area.setNom("Rennes");
        area.setId(1L);
        String baseUrl = "http://localhost:8095/geoserver/pluievolution";
        try {
            HttpGet httpGet = geoserverService.buildGeoserverHttpGet(baseUrl,
                    area, queryParams , MediaType.APPLICATION_JSON_VALUE, "UTF-8");
            assertNotNull(httpGet);
        } catch (Exception e) {
            Assertions.fail("echec de la requete");
        }

    }



}
