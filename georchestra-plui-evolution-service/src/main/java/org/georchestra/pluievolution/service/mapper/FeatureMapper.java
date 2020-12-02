package org.georchestra.pluievolution.service.mapper;

import com.vividsolutions.jts.geom.Geometry;
import org.georchestra.pluievolution.core.dto.Feature;
import org.georchestra.pluievolution.core.dto.Point;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.opengis.feature.simple.SimpleFeature;
import java.util.UUID;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FeatureMapper {
    default Feature simpleFeatureToFeature(SimpleFeature simpleFeature) {
        Geometry geom = (Geometry) simpleFeature.getDefaultGeometry();
        Point point =  new LocalizedMapperImpl().entityToDto(geom);
        UUID uuid = UUID.fromString(simpleFeature.getProperty("uuid").getValue().toString());

        Feature feature =new Feature();
        feature.setGeometry(point);
        feature.setType(Feature.TypeEnum.FEATURE);
        feature.setProperties(simpleFeature.getProperties());
        feature.setId(uuid);
        return feature;
    }
}
