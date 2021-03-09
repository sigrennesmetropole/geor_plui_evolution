package org.georchestra.pluievolution.service.mapper;

import org.georchestra.pluievolution.core.dto.GeographicEtablissement;
import org.georchestra.pluievolution.core.entity.acl.GeographicEtablissementEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = LocalizedMapper.class)
public interface GeographicEtablissementMapper extends AbstractMapper<GeographicEtablissementEntity, GeographicEtablissement> {
    @Override
    @Mappings(
            value = {
                    @Mapping(source = "geometry", target = "localisation")
            }
    )
    GeographicEtablissement entityToDto(GeographicEtablissementEntity geographicEtablissementEntity);

    @Override
    @InheritInverseConfiguration
    @Mappings(
            value = {
                    @Mapping(source = "localisation", target = "geometry")
            }
    )
    GeographicEtablissementEntity dtoToEntity(GeographicEtablissement geographicEtablissement);

    @Override
    @Mappings(
            value = {
                    @Mapping(source = "localisation", target = "geometry")
            }
    )
    void toEntity(GeographicEtablissement s, @MappingTarget GeographicEtablissementEntity entity);
}
