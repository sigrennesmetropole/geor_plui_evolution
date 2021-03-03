package org.georchestra.pluievolution.service.mapper;

import org.georchestra.pluievolution.core.dto.GeographicEtablissement;
import org.georchestra.pluievolution.core.dto.PluiRequest;
import org.georchestra.pluievolution.core.entity.acl.GeographicEtablissementEntity;
import org.georchestra.pluievolution.core.entity.request.PluiRequestEntity;
import org.mapstruct.*;

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
