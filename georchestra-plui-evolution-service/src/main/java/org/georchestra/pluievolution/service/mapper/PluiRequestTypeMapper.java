package org.georchestra.pluievolution.service.mapper;

import org.georchestra.pluievolution.core.dto.PluiRequestType;
import org.georchestra.pluievolution.core.dto.PluiRequestTypeEnum;
import org.georchestra.pluievolution.core.entity.ref.PluiRequestTypeEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PluiRequestTypeMapper extends AbstractMapper<PluiRequestTypeEntity, PluiRequestType> {
    @Override
    @InheritInverseConfiguration
    @Mappings(
            value = {
                    @Mapping(source = "pluiRequestType.value", target = "value", qualifiedByName = "convertToTypeEntityEnum")
            }
    )
    PluiRequestTypeEntity dtoToEntity(PluiRequestType pluiRequestType);

    @Override
    @Mappings(
            value = {
                    @Mapping(source = "pluiRequestTypeEntity.value", target = "value", qualifiedByName = "convertToTypeEnum")
            }
    )
    PluiRequestType entityToDto(PluiRequestTypeEntity pluiRequestTypeEntity);

    @Override
    @Mappings(
            value = {
                    @Mapping(source = "s.value", target = "value", qualifiedByName = "convertToTypeEntityEnum")
            }
    )
    PluiRequestTypeEntity toEntity(PluiRequestType s, @MappingTarget PluiRequestTypeEntity entity);

    /**
     * Conversion de PluiRequestStatusEnum à PluiRequestStatusEntityEnum
     * @param typeEnum
     * @return
     */
    static PluiRequestTypeEntity.PluiRequestTypeEntityEnum convertToTypeEntityEnum(PluiRequestTypeEnum typeEnum) {
        if (typeEnum != null) {
            return PluiRequestTypeEntity.fromValue(typeEnum.toString());
        }
        return null;
    }

    /**
     * Conversion de PluiRequestStatusEntityEnum à PluiRequestStatusEnum
     * @param typeEntityEnum
     * @return
     */
    static PluiRequestTypeEnum convertToType(PluiRequestTypeEntity.PluiRequestTypeEntityEnum typeEntityEnum) {
        if (typeEntityEnum != null) {
            PluiRequestTypeEnum.fromValue(typeEntityEnum.toString());
        }
        return null;
    }
}
