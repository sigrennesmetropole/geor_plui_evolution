package org.georchestra.pluievolution.service.mapper;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import org.georchestra.pluievolution.core.dto.GeometryType;
import org.georchestra.pluievolution.core.dto.Point;
import org.georchestra.pluievolution.core.dto.Point2D;
import org.mapstruct.*;

import java.math.BigDecimal;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class LocalizedMapper {

    static Geometry  dtoToEntity(Point point) {
        GeometryFactory gf = new GeometryFactory();
        Coordinate coordinate = new Coordinate(point.getCoordinates().get(0).doubleValue(),
                point.getCoordinates().get(1).doubleValue());
        return gf.createPoint(coordinate);
    }

    static Point entityToDto(Geometry geometry) {
        Point2D pt2D = new Point2D();
        pt2D.add(BigDecimal.valueOf(geometry.getCoordinate().x));
        pt2D.add(BigDecimal.valueOf(geometry.getCoordinate().y));
        Point point = new Point();
        point.setCoordinates(pt2D);
        point.setType(GeometryType.POINT);
        return point;
    }

    static Geometry toEntity(Point s, @MappingTarget Geometry entity) {
        if (s != null) {
            return dtoToEntity(s);
        }
        return entity;
    }
}
