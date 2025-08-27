package org.georchestra.pluievolution.service.mapper;

import org.georchestra.pluievolution.core.dto.PluiRequest;
import org.georchestra.pluievolution.core.entity.request.PluiRequestEntity;
import org.georchestra.pluievolution.service.exception.ApiServiceException;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {LocalizedMapper.class}, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface PluiRequestMapper extends AbstractMapper<PluiRequestEntity, PluiRequest> {

    @Override
    @InheritInverseConfiguration
    @Mappings(
            value = {
                    @Mapping(source = "localisation", target = "geometry"),
                    @Mapping(ignore = true, target = "creationDate"),
                    @Mapping(ignore = true, target = "approbation"),
                    @Mapping(ignore = true, target = "concertation")
            }
    )
    PluiRequestEntity dtoToEntity(PluiRequest pluiRequest) throws ApiServiceException;

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
                    @Mapping(source = "localisation", target = "geometry"),
                    @Mapping(ignore = true, target = "creationDate"),
                    @Mapping(ignore = true, target = "approbation"),
                    @Mapping(ignore = true, target = "concertation")
            }
    )
    void toEntity(PluiRequest s, @MappingTarget PluiRequestEntity entity);
}
