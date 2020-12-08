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
                    @Mapping(source = "geographicEtablissementEntity.geometry", target = "localisation")
            }
    )
    GeographicEtablissement entityToDto(GeographicEtablissementEntity geographicEtablissementEntity);

    @Override
    @InheritInverseConfiguration
    @Mappings(
            value = {
                    @Mapping(source = "geographicEtablissement.localisation", target = "geometry")
            }
    )
    GeographicEtablissementEntity dtoToEntity(GeographicEtablissement geographicEtablissement);

    @Override
    GeographicEtablissementEntity toEntity(GeographicEtablissement s, @MappingTarget GeographicEtablissementEntity entity);

    @AfterMapping
    default void afterMapping(PluiRequest s, @MappingTarget PluiRequestEntity entity) {
        if (entity.getGeometry() == null && s.getLocalisation() != null) {
            entity.setGeometry(new LocalizedMapperImpl().dtoToEntity(s.getLocalisation()));
        }
    }


}
