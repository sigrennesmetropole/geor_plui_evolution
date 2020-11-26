package org.georchestra.pluievolution.service.mapper;

import com.vividsolutions.jts.geom.Geometry;
import org.georchestra.pluievolution.core.dto.GeographicArea;
import org.georchestra.pluievolution.core.dto.GeometryType;
import org.georchestra.pluievolution.core.dto.Point;
import org.georchestra.pluievolution.core.dto.Point2D;
import org.georchestra.pluievolution.core.entity.acl.GeographicAreaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

import java.math.BigDecimal;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GeographicAreaMapper extends AbstractMapper<GeographicAreaEntity, GeographicArea> {
    @Override
    @Mappings(
            value = {
                    @Mapping(source = "geographicAreaEntity.geometry", target = "localisation", qualifiedByName = "convertToLocalisation")
            }
    )
    GeographicArea entityToDto(GeographicAreaEntity geographicAreaEntity);

    /**
     * Converti la geometrie de GeographicAreaEntity en une un point
     * @param geometry
     * @return
     */
    static Point convertToLocalisation(Geometry geometry) {
        Point2D coords = new Point2D();
        coords.add(BigDecimal.valueOf(geometry.getCoordinate().x));
        coords.add(BigDecimal.valueOf(geometry.getCoordinate().y));
        Point point = new Point();
        point.setCoordinates(coords);
        point.setType(GeometryType.POINT);
        return point;
    }
}
