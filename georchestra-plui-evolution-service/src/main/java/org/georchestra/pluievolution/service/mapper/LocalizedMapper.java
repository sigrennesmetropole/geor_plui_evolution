package org.georchestra.pluievolution.service.mapper;

import org.georchestra.pluievolution.core.dto.GeometryType;
import org.georchestra.pluievolution.core.dto.Point;
import org.georchestra.pluievolution.service.exception.ApiServiceException;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class LocalizedMapper {

    @Value("${pluievolution.geoserver.layerSRID}")
    private int layerSRID;

    public Geometry dtoToEntity(Point point) throws ApiServiceException {
        if (point == null) {
            return null;
        }
        GeometryFactory gf = new GeometryFactory();
        org.locationtech.jts.geom.Geometry pointGeom = null;
        try {
            // Si la geomeytrie a une projection definie et qui n'est pas en 3948 alors on la transforme vers 3948
            if (point.getProjection() != null && layerSRID != point.getProjection()) {
                CoordinateReferenceSystem sourceProjection = CRS.decode("EPSG:" + point.getProjection());

                CoordinateReferenceSystem targetProjection = CRS.decode("EPSG:" + layerSRID);

                MathTransform transform = CRS.findMathTransform(sourceProjection, targetProjection);
                pointGeom = JTS.transform(gf.createPoint(new Coordinate(point.getCoordinates().get(1).doubleValue(),
                        point.getCoordinates().get(0).doubleValue())), transform);
            } else {
                pointGeom = gf.createPoint(new Coordinate(point.getCoordinates().get(0).doubleValue(),
                        point.getCoordinates().get(1).doubleValue()));
            }

        } catch (FactoryException e) {
            throw new ApiServiceException("Impossible de parser la projection 3948", e);
        } catch (TransformException e) {
            throw new ApiServiceException("Impossible de parser reprojeter la geometrie", e);
        }

        pointGeom.setSRID(layerSRID);
        return pointGeom;
    }

    public Point entityToDto(Geometry geometry) {
        if (geometry == null) {
            return null;
        }
        List<BigDecimal> pt2D = new ArrayList<>();
        pt2D.add(BigDecimal.valueOf(geometry.getCoordinate().x));
        pt2D.add(BigDecimal.valueOf(geometry.getCoordinate().y));
        Point point = new Point();
        point.setCoordinates(pt2D);
        point.setType(GeometryType.POINT);
        return point;
    }
}
