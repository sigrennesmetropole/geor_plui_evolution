package org.georchestra.pluievolution.service.mapper;

import org.georchestra.pluievolution.core.dto.GeometryType;
import org.georchestra.pluievolution.core.dto.Point;
import org.georchestra.pluievolution.core.dto.Point2D;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class LocalizedMapper {

    @Value("${pluievolution.geoserver.layerSRID}")
    private int layerSRID;

    public Geometry dtoToEntity(Point point) {
        if (point == null) {
            return null;
        }
        GeometryFactory gf = new GeometryFactory();
        Coordinate coordinate = new Coordinate(point.getCoordinates().get(0).doubleValue(),
                point.getCoordinates().get(1).doubleValue());
        org.locationtech.jts.geom.Point pointGeom = gf.createPoint(coordinate);
        pointGeom.setSRID(layerSRID);
        return pointGeom;
    }

    public Point entityToDto(Geometry geometry) {
        if (geometry == null) {
            return null;
        }
        Point2D pt2D = new Point2D();
        pt2D.add(BigDecimal.valueOf(geometry.getCoordinate().x));
        pt2D.add(BigDecimal.valueOf(geometry.getCoordinate().y));
        Point point = new Point();
        point.setCoordinates(pt2D);
        point.setType(GeometryType.POINT);
        return point;
    }

    public Geometry toEntity(Point s, @MappingTarget Geometry entity) {
        if (s != null) {
            return dtoToEntity(s);
        }
        return entity;
    }
}
