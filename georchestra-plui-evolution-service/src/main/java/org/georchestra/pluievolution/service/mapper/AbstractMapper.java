package org.georchestra.pluievolution.service.mapper;

/**
 * @param <E> entity
 * @param <D> DTO
 */
public interface AbstractMapper<E, D> {

    /**
     * @param dto dto to transform to entity
     * @return entity
     */
    E dtoToEntity(D dto);

    /**
     * @param entity entity to transform to dto
     * @return dto
     */
    D entityToDto(E entity);

}
