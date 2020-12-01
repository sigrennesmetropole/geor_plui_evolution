package org.georchestra.pluievolution.service.mapper;

import org.georchestra.pluievolution.core.dto.Etablissement;
import org.georchestra.pluievolution.core.dto.PluiRequest;
import org.georchestra.pluievolution.core.entity.acl.EtablissementEntity;
import org.georchestra.pluievolution.core.entity.request.PluiRequestEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = LocalizedMapper.class)
public interface EtablissementMapper extends AbstractMapper<EtablissementEntity, Etablissement> {
    @Override
    @Mappings(
            value = {
                    @Mapping(source = "etablissementEntity.geometry", target = "localisation")
            }
    )
    Etablissement entityToDto(EtablissementEntity etablissementEntity);

    @Override
    @InheritInverseConfiguration
    @Mappings(
            value = {
                    @Mapping(source = "etablissement.localisation", target = "geometry")
            }
    )
    EtablissementEntity dtoToEntity(Etablissement etablissement);

    @Override
    EtablissementEntity toEntity(Etablissement s, @MappingTarget EtablissementEntity entity);

    @AfterMapping
    default void afterMapping(PluiRequest s, @MappingTarget PluiRequestEntity entity) {
        if (entity.getGeometry() == null && s.getLocalisation() != null) {
            entity.setGeometry(new LocalizedMapperImpl().dtoToEntity(s.getLocalisation()));
        }
    }


}
