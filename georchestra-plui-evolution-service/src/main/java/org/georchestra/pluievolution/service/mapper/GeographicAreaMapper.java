package org.georchestra.pluievolution.service.mapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.georchestra.pluievolution.core.dto.GeographicArea;
import org.georchestra.pluievolution.core.dto.GeometryType;
import org.georchestra.pluievolution.core.entity.acl.GeographicAreaEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

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
            List<BigDecimal> pt = new ArrayList<>();
            pt.add(BigDecimal.valueOf(coordinate.x));
            pt.add(BigDecimal.valueOf(coordinate.y));
            return pt;
        }).toList());
        polygonDto.setType(GeometryType.POLYGON);
        dto.setLocalisation(polygonDto);
    }
}
