package org.georchestra.pluievolution.service.sm;

import org.georchestra.pluievolution.StarterSpringBootTestApplication;
import org.georchestra.pluievolution.service.exception.ApiServiceException;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.util.factory.GeoTools;
import org.junit.jupiter.api.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = StarterSpringBootTestApplication.class)
@ComponentScan({ "org.georchestra.pluievolution.api", "org.georchestra.pluievolution.service",
        "org.georchestra.pluievolution.core" })
@TestPropertySource(value = { "classpath:plui-evolution.properties", "classpath:plui-evolution-common.properties" })
class GeoserverServiceTest {

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
}
