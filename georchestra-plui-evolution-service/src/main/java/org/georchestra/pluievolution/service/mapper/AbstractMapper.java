package org.georchestra.pluievolution.service.mapper;

import org.georchestra.pluievolution.service.exception.ApiServiceException;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * @param <E> entity
 * @param <D> DTO
 */
public interface AbstractMapper<E, D> {

    /**
     * @param dto dto to transform to entity
     * @return entity
     */
    E dtoToEntity(D dto) throws ApiServiceException;

    /**
     * @param entity entity to transform to dto
     * @return dto
     */
    D entityToDto(E entity);

    /**
     * Permet de mettre à jour une entité à partir du dto
     * @param s
     * @param entity
     */
    void toEntity(D s, @MappingTarget E entity);

    /**
     * Permet de convertir une liste de entities à dto
     * @param entities
     * @return
     */
    List<D> entitiesToDto(List<E> entities);

}
