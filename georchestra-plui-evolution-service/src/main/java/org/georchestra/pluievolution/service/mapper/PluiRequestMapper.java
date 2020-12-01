package org.georchestra.pluievolution.service.mapper;

import org.georchestra.pluievolution.core.dto.*;
import org.georchestra.pluievolution.core.entity.request.PluiRequestEntity;
import org.mapstruct.*;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {LocalizedMapper.class})
public interface PluiRequestMapper extends AbstractMapper<PluiRequestEntity, PluiRequest> {

    @Override
    @InheritInverseConfiguration
    @Mappings(
            value = {
                    @Mapping(source = "pluiRequest.localisation", target = "geometry")
            }
    )
    PluiRequestEntity dtoToEntity(PluiRequest pluiRequest);

    @Override
    @Mappings(
            value = {
                    @Mapping(source = "pluiRequestEntity.geometry", target = "localisation")
            }
    )
    PluiRequest entityToDto(PluiRequestEntity pluiRequestEntity);

    @Override
    PluiRequestEntity toEntity(PluiRequest s, @MappingTarget PluiRequestEntity entity);

    @AfterMapping
    default void afterMapping(PluiRequest s, @MappingTarget PluiRequestEntity entity) {
        if (entity.getGeometry() == null && s.getLocalisation() != null) {
            entity.setGeometry(new LocalizedMapperImpl().dtoToEntity(s.getLocalisation()));
        }
    }
}
