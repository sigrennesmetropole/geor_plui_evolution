package org.georchestra.pluievolution.service.mapper;

import org.georchestra.pluievolution.core.dto.*;
import org.georchestra.pluievolution.core.entity.request.PluiRequestEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {PluiRequestTypeMapper.class, PluiRequestStatusMapper.class})
public interface PluiRequestMapper extends AbstractMapper<PluiRequestEntity, PluiRequest> {
    @Override
    @InheritInverseConfiguration
    PluiRequestEntity dtoToEntity(PluiRequest pluiRequest);

    @Override
    PluiRequest entityToDto(PluiRequestEntity pluiRequestEntity);

    @Override
    PluiRequestEntity toEntity(PluiRequest s, @MappingTarget PluiRequestEntity entity);
}
