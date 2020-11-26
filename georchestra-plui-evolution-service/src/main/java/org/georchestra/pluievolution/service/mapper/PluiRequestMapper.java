package org.georchestra.pluievolution.service.mapper;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import org.georchestra.pluievolution.core.dto.*;
import org.georchestra.pluievolution.core.entity.request.PluiRequestEntity;
import org.mapstruct.*;

import java.math.BigDecimal;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {PluiRequestTypeMapper.class, PluiRequestStatusMapper.class})
public interface PluiRequestMapper extends AbstractMapper<PluiRequestEntity, PluiRequest> {
    @Override
    @InheritInverseConfiguration
    @Mappings(
            value = {
                    @Mapping(source = "pluiRequest.localisation", target = "geometry", qualifiedByName = "convertToGeometry")
            }
    )
    PluiRequestEntity dtoToEntity(PluiRequest pluiRequest);

    @Override
    @Mappings(
            value = {
                    @Mapping(source = "pluiRequestEntity.geometry", target = "localisation", qualifiedByName = "convertToLocalisation")
            }
    )
    PluiRequest entityToDto(PluiRequestEntity pluiRequestEntity);

    @Override
    @Mappings(
            value = {
                    @Mapping(source = "s.localisation", target = "geometry", qualifiedByName = "convertToGeometry")
            }
    )
    PluiRequestEntity toEntity(PluiRequest s, @MappingTarget PluiRequestEntity entity);

    static Geometry convertToGeometry(Point localisation) {
        GeometryFactory gf = new GeometryFactory();
        Coordinate coordinate = new Coordinate(localisation.getCoordinates().get(0).doubleValue(),
                localisation.getCoordinates().get(1).doubleValue());
        return gf.createPoint(coordinate);
    }

    static Point convertToLocalisation(Geometry geometry) {
        Point2D pt2D = new Point2D();
        pt2D.add(BigDecimal.valueOf(geometry.getCoordinate().x));
        pt2D.add(BigDecimal.valueOf(geometry.getCoordinate().y));
        Point point = new Point();
        point.setCoordinates(pt2D);
        point.setType(GeometryType.POINT);
        return point;
    }
}
