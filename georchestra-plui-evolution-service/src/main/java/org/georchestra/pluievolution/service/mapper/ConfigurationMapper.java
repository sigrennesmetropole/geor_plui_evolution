package org.georchestra.pluievolution.service.mapper;


import org.georchestra.pluievolution.core.dto.ConfigurationData;
import org.georchestra.pluievolution.service.bean.Configuration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ConfigurationMapper extends AbstractMapper<Configuration, ConfigurationData> {

    @Override
    @InheritInverseConfiguration
   Configuration dtoToEntity(ConfigurationData dto);

    /**
     * Converti un dossier en DossierDto.
     *
     * @param entity entity to transform to dto
     * @return DossierDto
     */
    @Override
    ConfigurationData entityToDto(Configuration entity);
}

