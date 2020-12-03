package org.georchestra.pluievolution.service.mapper;

import org.georchestra.pluievolution.core.dto.GeographicArea;
import org.georchestra.pluievolution.core.dto.Point2D;
import org.georchestra.pluievolution.core.entity.acl.GeographicAreaEntity;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GeographicAreaMapper extends AbstractMapper<GeographicAreaEntity, GeographicArea> {

    @Override
    GeographicArea entityToDto(GeographicAreaEntity geographicAreaEntity);

    @AfterMapping
    default void polygonToLocalisation(GeographicAreaEntity entity, @MappingTarget GeographicArea dto) {
        if (entity.getGeometry() == null) {
            return;
        }
        org.georchestra.pluievolution.core.dto.Polygon polygonDto = new org.georchestra.pluievolution.core.dto.Polygon();
        polygonDto.addCoordinatesItem(Arrays.stream(entity.getGeometry().getCoordinates()).map(coordinate -> {
            Point2D pt = new Point2D();
            pt.add(BigDecimal.valueOf(coordinate.x));
            pt.add(BigDecimal.valueOf(coordinate.y));
            return pt;
        }).collect(Collectors.toList()));
        dto.setLocalisation(polygonDto);
    }
}
