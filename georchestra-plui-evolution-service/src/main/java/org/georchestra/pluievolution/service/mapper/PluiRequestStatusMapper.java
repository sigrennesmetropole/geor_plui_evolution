package org.georchestra.pluievolution.service.mapper;

import org.georchestra.pluievolution.core.dto.*;
import org.georchestra.pluievolution.core.entity.ref.PluiRequestStatusEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PluiRequestStatusMapper extends AbstractMapper<PluiRequestStatusEntity, PluiRequestStatus>{
    @Override
    @InheritInverseConfiguration
    @Mappings(
            value = {
                    @Mapping(source = "pluiRequestStatus.value", target = "value", qualifiedByName = "convertToStatusEntityEnum")
            }
    )
    PluiRequestStatusEntity dtoToEntity(PluiRequestStatus pluiRequestStatus);

    @Override
    @Mappings(
            value = {
                    @Mapping(source = "pluiRequestStatusEntity.value", target = "value", qualifiedByName = "convertToStatusEnum")
            }
    )
    PluiRequestStatus entityToDto(PluiRequestStatusEntity pluiRequestStatusEntity);

    @Override
    @Mappings(
            value = {
                    @Mapping(source = "s.value", target = "value", qualifiedByName = "convertToStatusEntityEnum")
            }
    )
    PluiRequestStatusEntity toEntity(PluiRequestStatus s, @MappingTarget PluiRequestStatusEntity entity);

    /**
     * Conversion de PluiRequestStatusEnum à PluiRequestStatusEntityEnum
     * @param statusEnum
     * @return
     */
    static PluiRequestStatusEntity.PluiRequestStatusEntityEnum convertToStatusEntityEnum(PluiRequestStatusEnum statusEnum) {
        if (statusEnum != null) {
            return PluiRequestStatusEntity.fromValue(statusEnum.toString());
        }
        return null;
    }

    /**
     * Conversion de PluiRequestStatusEntityEnum à PluiRequestStatusEnum
     * @param statusEntityEnum
     * @return
     */
    static PluiRequestStatusEnum convertToStatus(PluiRequestStatusEntity.PluiRequestStatusEntityEnum statusEntityEnum) {
        if (statusEntityEnum != null) {
            PluiRequestStatusEnum.fromValue(statusEntityEnum.toString());
        }
        return null;
    }
}
