package org.georchestra.pluievolution.service.mapper;

import org.georchestra.pluievolution.core.dto.GeographicArea;
import org.georchestra.pluievolution.core.dto.PluiRequest;
import org.georchestra.pluievolution.core.entity.acl.GeographicAreaEntity;
import org.georchestra.pluievolution.core.entity.request.PluiRequestEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = LocalizedMapper.class)
public interface GeographicAreaMapper extends AbstractMapper<GeographicAreaEntity, GeographicArea> {
    @Override
    @Mappings(
            value = {
                    @Mapping(source = "geographicAreaEntity.geometry", target = "localisation")
            }
    )
    GeographicArea entityToDto(GeographicAreaEntity geographicAreaEntity);

    @Override
    @InheritInverseConfiguration
    @Mappings(
            value = {
                    @Mapping(source = "geographicArea.localisation", target = "geometry")
            }
    )
    GeographicAreaEntity dtoToEntity(GeographicArea geographicArea);

    @Override
    GeographicAreaEntity toEntity(GeographicArea s, @MappingTarget GeographicAreaEntity entity);

    @AfterMapping
    default void afterMapping(PluiRequest s, @MappingTarget PluiRequestEntity entity) {
        if (entity.getGeometry() == null && s.getLocalisation() != null) {
            entity.setGeometry(new LocalizedMapperImpl().dtoToEntity(s.getLocalisation()));
        }
    }


}
