package org.georchestra.pluievolution.service.mapper;

import org.georchestra.pluievolution.core.dto.*;
import org.georchestra.pluievolution.core.entity.request.PluiRequestEntity;
import org.mapstruct.*;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {LocalizedMapper.class}, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface PluiRequestMapper extends AbstractMapper<PluiRequestEntity, PluiRequest> {

    @Override
    @InheritInverseConfiguration
    @Mappings(
            value = {
                    @Mapping(source = "localisation", target = "geometry")
            }
    )
    PluiRequestEntity dtoToEntity(PluiRequest pluiRequest);

    @Override
    @Mappings(
            value = {
                    @Mapping(source = "geometry", target = "localisation")
            }
    )
    PluiRequest entityToDto(PluiRequestEntity pluiRequestEntity);

    @Override
    @Mappings(
            value = {
                    @Mapping(source = "localisation", target = "geometry")
            }
    )
    void toEntity(PluiRequest s, @MappingTarget PluiRequestEntity entity);
}
